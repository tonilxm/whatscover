package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.config.Constants;
import com.whatscover.domain.User;
import com.whatscover.repository.AgentProfileRepository;
import com.whatscover.service.AgentProfileService;
import com.whatscover.service.MailService;
import com.whatscover.service.UserService;
import com.whatscover.service.dto.AgentProfileDTO;
import com.whatscover.service.exception.BusinessException;
import com.whatscover.service.util.PropertiesReader;
import com.whatscover.service.util.RandomUtil;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AgentProfile.
 */
@RestController
@RequestMapping("/api")
public class AgentProfileResource {

    private final Logger log = LoggerFactory.getLogger(AgentProfileResource.class);

    private static final String ENTITY_NAME = "agentProfile";

    private final AgentProfileService agentProfileService;

	private final AgentProfileRepository agentProfileRepository;

    private final MailService mailService;

    private final UserService userService;
    
    private final String fileUploadDir = PropertiesReader.getPropertiesValue("directory");
    
    private final String TEMP_FILE_NAME = "whatsCoverTempUploadFile.jpg";

    //private String directory = PropertiesReader.getPropertiesValue("directory");

    public AgentProfileResource(AgentProfileService agentProfileService, AgentProfileRepository agentProfileRepository, MailService mailService, UserService userService) {
        this.agentProfileService = agentProfileService;
        this.agentProfileRepository = agentProfileRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * POST  /agent-profiles : Create a new agentProfile.
     *
     * @param agentProfileDTO the agentProfileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agentProfileDTO, or with status 400 (Bad Request) if the agentProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agent-profiles")
    @Timed
    public ResponseEntity<AgentProfileDTO> createAgentProfile(@Valid @RequestBody AgentProfileDTO agentProfileDTO) throws URISyntaxException {
        log.debug("REST request to save AgentProfile : {}", agentProfileDTO);
        String email = agentProfileDTO.getEmail();

		if (agentProfileDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new Agent Profile cannot already have an ID")).body(null);
		} else if (agentProfileRepository.findOneByAgentCode(agentProfileDTO.getAgent_code()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentcodeexists", "Agent Code already in use")).body(null);
		} else if (agentProfileRepository.findOneByEmail(email).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentemailexists", "Agent Email already in use")).body(null);
		} else if (agentProfileDTO.getInsuranceCompanyId() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.insuranceCompanyIdBlank", "You have to choose Insurance Company")).body(null);
		} else if (agentProfileDTO.getInsuranceAgencyId() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.insuranceAgencyIdBlank", "You have to choose Insurance Agency")).body(null);
		}

		AgentProfileDTO result = null;

        try {
            // produce random password
            String randomPassword = RandomUtil.generatePassword();
            result = agentProfileService.create(agentProfileDTO, randomPassword);
            // Get recently created user
            User user = userService.findUserByEmail(agentProfileDTO.getEmail());
            // send activation email
            mailService.sendAgentProfileActivationEmail(user,randomPassword);
        } catch (BusinessException be) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.accountexists", "This Account already created")).body(null);
        }

        return ResponseEntity.created(new URI("/api/agent-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agent-profiles : Updates an existing agentProfile.
     *
     * @param agentProfileDTO the agentProfileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agentProfileDTO,
     * or with status 400 (Bad Request) if the agentProfileDTO is not valid,
     * or with status 500 (Internal Server Error) if the agentProfileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agent-profiles")
    @Timed
    public ResponseEntity<AgentProfileDTO> updateAgentProfile(@Valid @RequestBody AgentProfileDTO agentProfileDTO) throws URISyntaxException {
        log.debug("REST request to update AgentProfile : {}", agentProfileDTO);
        if (agentProfileDTO.getId() == null) {
            return createAgentProfile(agentProfileDTO);
        }
		userService.updateUserByEmail(agentProfileDTO.getFirst_name(), 
				agentProfileDTO.getLast_name(), agentProfileDTO.getEmail(), 
				Constants.DEFAULT_LANG_KEY, agentProfileDTO.getPhoto_dir());
		
		String photoDir = getImageName(agentProfileDTO);
		if (!processImgUpload(agentProfileDTO.getUserId(), photoDir).isEmpty()) {
			agentProfileDTO.setPhoto_dir(photoDir);
		}
		
        AgentProfileDTO result = agentProfileService.save(agentProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agentProfileDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * Rename upload image if file exist
     *  
     * @param userId
     * @param photoDir
     * @return
     */
    private String processImgUpload(Long userId, String photoDir) {
    	File oldFile = new File(fileUploadDir + File.separator + TEMP_FILE_NAME);
		File newFile = new File(photoDir);
		
		if(!oldFile.exists()) {
			return "";
		}
		
		return renameFile(oldFile, newFile);
	}
    
    /**
     * Rename file depend on old/new File input
     * 
     * @param oldFile old File
     * @param newFile new File
     * @return
     */
    private String renameFile(File oldFile, File newFile) {
    	String result = "";
    	
        if(oldFile.renameTo(newFile)) {
        	result = newFile.getPath();
        } else {
        	newFile.delete();
        	oldFile.renameTo(newFile);
        	result = newFile.getPath();
        }
        
        return result;
	}
    
    /**
     * Get upload image file name follow rule: <user_id>_<profile_id>.jpg
     * 
     * @param agentProfileDTO AgentProfileDTO
     * @return
     */
    protected String getImageName(AgentProfileDTO agentProfileDTO) {
		StringBuilder photo_dir = new StringBuilder();
		photo_dir.append(fileUploadDir)
				.append(File.separator)
				.append(agentProfileDTO.getUserId())
				.append("_")
				.append(agentProfileDTO.getId())
				.append(".JPG");
		return photo_dir.toString();
	}
    
    @GetMapping("/load-file")
    @Timed
    public ResponseEntity<byte[]> loadFile(@RequestParam(value="filePath") String filePath) {
        log.debug("REST request to load image file");
    	File file = new File(filePath); // a method that returns file for given ID
        if (!file.exists()) { // handle FNF
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        try {
            byte[] base64Bytes = Base64.encodeBase64(loadFileAsBytesArray(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("filename", file.getPath());
            headers.add("Content-Type", "image/jpeg");

            return ResponseEntity.ok().headers(headers).body(base64Bytes);
        } catch (IOException e) {
            log.error("Failed to load file ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Load a file from file system and returns the byte array of the content.
     *
     * @param file file
     * @return byte[]
     * 
     * @throws IOException 
     */
    private static byte[] loadFileAsBytesArray(File file) throws IOException {
        int fileLen = (int) file.length();
        byte[] bytes = new byte[fileLen];
        
        BufferedInputStream bufferIS = new BufferedInputStream(new FileInputStream(file));
        bufferIS.read(bytes, 0, fileLen);
        // Release file
        bufferIS.close();
        
        return bytes;
    }
    
    @PostMapping(value = "/upload-file", consumes = "multipart/form-data", produces = "text/plain")
    @Timed
    public @ResponseBody String uploadFile( @RequestParam(value="files") MultipartFile[] uploadingFiles) throws IOException {
    	log.debug("REST request to Email AgentProfile : {}", uploadingFiles.length);
    	String filePath = "";
    	File file;
        FileOutputStream fileOS;
    	for(MultipartFile uploadedFile : uploadingFiles) {
    		File dir = new File(fileUploadDir);
    		createDirectory(dir);
    		
    		if (dir.exists() && dir.isDirectory()) {
    			filePath = fileUploadDir + File.separator + TEMP_FILE_NAME;
    			
    			// Delete file if it was existed
    			file = new File(filePath);
    			if (file.exists()) {
    				file.delete();
					log.debug("Delete exist file: " + file.getPath());
    			}
    			
    			// Read and release file
	            fileOS = new FileOutputStream(filePath);
	            fileOS.write(uploadedFile.getBytes());
	            fileOS.close();
    		}
        }
    	
    	return filePath;
    }
    
    /**
     * Create directory to upload file if it is not exist
     * 
     * @param files
     */
	private void createDirectory(File files) {
		try {
			if (!files.exists()) {
				if (files.mkdirs()) {
					log.debug("Create Directory for UploadFile sucessfully: " + files.getPath());
				} else {
					log.debug("Can not create Directory for UploadFile: " + files.getPath());
				}
			}
		} catch (Exception e) {
			log.debug("Can not create Directory for UploadFile: " + e.getMessage());
			e.printStackTrace();
		}
	}

    /*@PostMapping("/upload-file")
    @Timed
    public String handleFormUpload( @RequestParam(value="uploadFile") String uploadFile) throws IOException {
    	log.debug("REST request to Email AgentProfile : {}", uploadFile.length());
        String directory = PropertiesReader.getPropertiesValue("directory");
    	File files = new File(directory);
		createDirectory(files);
		wireBase64ToNewImg(uploadFile, directory + "\\test.JPG");
    	return "";
    }*/

    /**
     * GET  /agent-profiles : get all the agentProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agentProfiles in body
     */
    @GetMapping("/agent-profiles")
    @Timed
    public ResponseEntity<List<AgentProfileDTO>> getAllAgentProfiles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AgentProfiles");
        Page<AgentProfileDTO> page = agentProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agent-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/send-email-profiles")
    @Timed
    public ResponseEntity<AgentProfileDTO> getSendEmailAgentProfiles(@Valid @RequestBody AgentProfileDTO agentProfileDTO) {
    	log.debug("REST request to Email AgentProfile : {}", agentProfileDTO);
    	if (agentProfileDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new Agent Profile cannot already have an ID")).body(null);
		} else if (agentProfileRepository.findOneByAgentCode(agentProfileDTO.getAgent_code()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentcodeexists", "Agent Code already in use"))
					.body(null);
		} else if(agentProfileRepository.findOneByEmail(agentProfileDTO.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentemailexists", "Agent Email already in use"))
					.body(null);
		}
        mailService.sendEmail(agentProfileDTO.getEmail(), Constants.AGENT_EMAIL_SUBJECT_REGISTRATION, Constants.AGENT_EMAIL_SUBJECT_REGISTRATION, false, true);
        return ResponseEntity.ok().headers(HeaderUtil.createEntitySendEmailAlert(ENTITY_NAME, "Send Email Alert")).build();
    }

    /**
     * GET  /agent-profiles/:id : get the "id" agentProfile.
     *
     * @param id the id of the agentProfileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agentProfileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agent-profiles/{id}")
    @Timed
    public ResponseEntity<AgentProfileDTO> getAgentProfile(@PathVariable Long id) {
        log.debug("REST request to get AgentProfile : {}", id);
        AgentProfileDTO agentProfileDTO = agentProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agentProfileDTO));
    }

    /**
     * DELETE  /agent-profiles/:id : delete the "id" agentProfile.
     *
     * @param id the id of the agentProfileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agent-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgentProfile(@PathVariable Long id) {
        log.debug("REST request to delete AgentProfile : {}", id);
        AgentProfileDTO agentProfileDTO = agentProfileService.findOne(id);
        agentProfileService.delete(id);
        User user = userService.findUserByEmail(agentProfileDTO.getEmail());
        userService.deleteUser(user.getLogin());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agent-profiles?query=:query : search for the agentProfile corresponding
     * to the query.
     *
     * @param queryData the query of the agentProfile search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/agent-profiles")
    @Timed
    public ResponseEntity<List<AgentProfileDTO>> searchAgentProfiles(
    		@RequestParam(value="queryData") String[] queryData,
    		@ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AgentProfiles for query {}", queryData.toString());
        Page<AgentProfileDTO> page = agentProfileService.search(queryData, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(
        		queryData.toString(), page, "/api/_search/agent-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * SEARCH  /_search-name/agent-profiles?query=:query : search for the agentProfile corresponding
     * to the query.
     *
     * @param queryData the query of the agentProfile search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search-name/agent-profiles")
    @Timed
    public ResponseEntity<List<AgentProfileDTO>> searchAgentProfilesByName(
    		@RequestParam(value="queryData") String queryData,
    		@ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AgentProfiles for query {}", queryData.toString());
        Page<AgentProfileDTO> page = agentProfileService.searchByName(queryData, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(
        		queryData.toString(), page, "/api/_search-name/agent-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
