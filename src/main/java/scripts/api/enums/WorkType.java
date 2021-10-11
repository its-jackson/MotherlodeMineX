package scripts.api.enums;

public enum WorkType {

    MOTHERLODE_MINE("Motherlode Mine"),
    MINING("Mining");

    private final String workType;

    WorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkType() {
        return workType;
    }
}
