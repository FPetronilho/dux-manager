package com.tracktainment.duxmanager.util;

public class Constants {

    public Constants() {
        throw new IllegalStateException("Util class cannot be instantiated.");
    }

    // Default values
    public static final String DEFAULT_OFFSET = "0";
    public static final String DEFAULT_LIMIT = "10";
    public static final int MIN_OFFSET = 0;
    public static final int MIN_LIMIT = 1;
    public static final int MAX_LIMIT = 100;


    // Required fields validation
    public static final String USER_IDP_INFO_MANDATORY_MSG = "'idPInfo' is mandatory.";
    public static final String USER_PERSONAL_INFO_MANDATORY_MSG = "'personalInfo' is mandatory.";
    public static final String USER_CONTACT_MEDIUM_MANDATORY_MSG = "'contactMediumList' is mandatory.";
    public static final String USER_SUBJECT_MANDATORY_MSG = "'subject' is mandatory.";
    public static final String USER_TENANT_ID_MANDATORY_MSG = "'tenantId' is mandatory.";
    public static final String ASSET_EXTERNAL_ID_MANDATORY_MSG = "'externalId' is mandatory.";
    public static final String ASSET_TYPE_MANDATORY_MSG = "'type' is mandatory.";
    public static final String ASSET_ARTIFACT_INFO_MANDATORY_MSG = "'artifactInfo' is mandatory.";
    public static final String ASSET_PERMISSION_POLICY_MANDATORY_MSG = "'permissionPolicy' is mandatory.";
    public static final String ASSET_GROUP_ID_MANDATORY_MSG = "'groupId' is mandatory.";
    public static final String ASSET_ARTIFACT_ID_MANDATORY_MSG = "'artifactId' is mandatory.";
    public static final String ASSET_VERSION_MANDATORY_MSG = "'version' is mandatory.";


    // Regex
    public static final String GROUP_ID_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,50}";
    public static final String ARTIFACT_ID_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,50}";
    public static final String VERSION_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,50}";
    public static final String TYPE_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,30}";
    public static final String ID_REGEX = "[a-fA-F\\d\\-]{36}";
    public static final String ID_LIST_REGEX =
            "^([a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})(," +
                    "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}){0," +
                    (MAX_LIMIT-1) + "}$";
    public static final String SUB_REGEX = "^[a-zA-Z0-9_\\-\\.]+$";
    public static final String TENANT_ID_REGEX = "^[a-zA-Z0-9_\\-\\.]+$";
    public static final String FULL_NAME_REGEX = "^[A-Za-z]+(?:['-]?[A-Za-z]+)*(?: [A-Za-z]+(?:['-]?[A-Za-z]+)*)+$";
    public static final String SINGLE_NAME_REGEX = "^[A-Za-z]+(?:['-][A-Za-z]+)*$";
    public static final String COUNTRY_CODE_REGEX = "^\\+[1-9]\\d{0,3}$";
    public static final String PHONE_NUMBER_REGEX = "^(\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String GENERIC_ADDRESS_REGEX = "^[a-zA-Z0-9#.,\\-/\\s]+$";
    public static final String POSTAL_CODE_REGEX = "^[a-zA-Z0-9\\s-]{3,10}$";


    // Fields validation
    public static final String ID_INVALID_MSG = "'id' must match: " + ID_REGEX + ".";
    public static final String OFFSET_INVALID_MSG = "'offset' must be positive";
    public static final String LIMIT_INVALID_MSG = "'limit' must be in the range [" + MIN_LIMIT + ", " + MAX_LIMIT + "]";
    public static final String GROUP_ID_INVALID_MSG = "'groupId' must match: " + GROUP_ID_REGEX + ".";
    public static final String ARTIFACT_ID_INVALID_MSG = "'artifactId' must match: " + ARTIFACT_ID_REGEX + ".";
    public static final String VERSION_INVALID_MSG = "'version' must match: " + VERSION_REGEX + ".";
    public static final String TYPE_INVALID_MSG = "'type' must match: " + TYPE_REGEX + ".";
    public static final String DIGITAL_USER_ID_INVALID_MSG = "'digitalUserId' must match: " + ID_REGEX + ".";
    public static final String IDS_INVALID_MSG = "'ids' must match: " + ID_LIST_REGEX + ".";
    public static final String SUB_INVALID_MSG = "'sub' must match: " + SUB_REGEX + ".";
    public static final String EXTERNAL_ID_INVALID_MSG = "'externalId' must match: " + ID_REGEX + ".";
    public static final String TENANT_ID_INVALID_MSG = "'tenantId' must match: " + TENANT_ID_REGEX + ".";
    public static final String FULL_NAME_INVALID_MSG = "'fullName' must match: " + FULL_NAME_REGEX + ".";
    public static final String FIRST_NAME_INVALID_MSG = "'firstName' must match: " + SINGLE_NAME_REGEX + ".";
    public static final String NICKNAME_INVALID_MSG = "'nickName' must match: " + SINGLE_NAME_REGEX + ".";
    public static final String MIDDLE_NAME_INVALID_MSG = "'middleName' must match: " + SINGLE_NAME_REGEX + ".";
    public static final String FAMILY_NAME_INVALID_MSG = "'familyName' must match: " + SINGLE_NAME_REGEX + ".";
    public static final String COUNTRY_CODE_INVALID_MSG = "'countryCode' must match: " + COUNTRY_CODE_REGEX + ".";
    public static final String PHONE_NUMBER_INVALID_MSG = "'phoneNumber' must match: " + PHONE_NUMBER_REGEX + ".";
    public static final String EMAIL_INVALID_MSG = "'email' must match: " + EMAIL_REGEX + ".";
    public static final String COUNTRY_INVALID_MSG = "'country' must match: " + GENERIC_ADDRESS_REGEX + ".";
    public static final String CITY_INVALID_MSG = "'city' must match: " + GENERIC_ADDRESS_REGEX + ".";
    public static final String STATE_PROVINCE_INVALID_MSG = "'stateOrProvince' must match: " + GENERIC_ADDRESS_REGEX + ".";
    public static final String STREET_INVALID_MSG = "'street' must match: " + GENERIC_ADDRESS_REGEX + ".";
    public static final String POSTAL_CODE_INVALID_MSG = "'postalCode' must match: " + POSTAL_CODE_REGEX + ".";
    public static final String BOOLEAN_INVALID_MSG = "'preferred' must be either 'true' or 'false'.";
}
