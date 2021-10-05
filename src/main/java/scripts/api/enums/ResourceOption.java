package scripts.api.enums;

public enum ResourceOption {

    BANK("Bank"),
    DROP("Drop");

    private final String option;

    ResourceOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
