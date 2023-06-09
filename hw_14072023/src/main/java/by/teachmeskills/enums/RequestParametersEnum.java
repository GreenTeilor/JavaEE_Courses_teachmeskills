package by.teachmeskills.enums;

public enum RequestParametersEnum {
    NAME("name"),
    ID("id"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    PASSWORD("password"),
    BIRTH_DATE("birthDate"),
    TYPE("type"),

    COMMAND("command"),
    ADDRESS("address"),
    PHONE_NUMBER("phoneNumber"),
    SEARCH_CRITERIA("searchCriteria");

    private final String VALUE;

    RequestParametersEnum(String value) {
        this.VALUE = value;
    }

    public String getValue() {
        return VALUE;
    }
}
