package ITM.maint.fiix_custom_mobile.constants;

public enum WorkOrderStatuses {

    id("id"),
    name("strName"),
    controlId("intControlID"),
    sysCode("intSysCode"),
    updated("intUpdated");

    private final String field;

    private WorkOrderStatuses(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
