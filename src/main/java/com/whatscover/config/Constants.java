package com.whatscover.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    private Constants() {
    }
    
    // Field name for Entity
    // AGENT PROFILE
    public static final String AGENT_PROFILE_FIRST_NAME = "first_name";
    public static final String AGENT_PROFILE_MIDDLE_NAME = "middle_name"; 
    public static final String AGENT_PROFILE_LAST_NAME = "last_name";
    public static final String AGENT_PROFILE_INSURANCE_COMPANY = "insuranceCompany.name";
    public static final String AGENT_PROFILE_INSURANCE_AGENCY = "insuranceAgency.name";
    public static final String AGENT_EMAIL_SUBJECT_REGISTRATION = "Create New Agent Profiles";
    public static final String AGENT_EMAIL_CONTENT_REGISTRATION = "Your user registration has been created. Please click link below to activate your account.";
    
    // INSURANCE AGENCY
    public static final String INSURANCE_AGENCY_NAME = "name";
}
