package scripts.api;

public enum ResourceOption {

    BANK("Bank");

    private final String bank;

    ResourceOption(String bank) {
        this.bank = bank;
    }

    public String getBank() {
        return bank;
    }
}
