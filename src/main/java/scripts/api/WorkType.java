package scripts.api;

public enum WorkType {

    MOTHERLODE_MINE("Motherlode Mine");

    private final String workType;

    WorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkType() {
        return workType;
    }
}
