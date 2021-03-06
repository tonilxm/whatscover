package com.whatscover.service.dto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.whatscover.config.Constants;
import com.whatscover.domain.Authority;
import com.whatscover.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO extends AbstractAuditingDTO {

	private Long id;

	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 100)
	private String login;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(max = 256)
	private String imageUrl;

	private boolean activated = false;

	@Size(min = 2, max = 5)
	private String langKey;

	private Set<String> authorities;

	public UserDTO() {
		// Empty constructor needed for Jackson.
	}

	public UserDTO(User user) {
		this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getActivated(), user.getImageUrl(), user.getLangKey(), user.getCreatedBy(), user.getCreatedDate(),
				user.getLastModifiedBy(), user.getLastModifiedDate(),
				user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
	}

	public UserDTO(Long id, String login, String firstName, String lastName, String email, boolean activated,
			String imageUrl, String langKey, String createdBy, Instant createdDate, String lastModifiedBy,
			Instant lastModifiedDate, Set<String> authorities) {

		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.activated = activated;
		this.imageUrl = imageUrl;
		this.langKey = langKey;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isActivated() {
		return activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
				+ '\'' + ", email='" + email + '\'' + ", imageUrl='" + imageUrl + '\'' + ", activated=" + activated
				+ ", langKey='" + langKey + '\'' + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy='" + lastModifiedBy + '\'' + ", lastModifiedDate=" + lastModifiedDate
				+ ", authorities=" + authorities + "}";
	}
}
