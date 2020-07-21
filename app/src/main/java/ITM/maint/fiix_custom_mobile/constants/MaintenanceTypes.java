package ITM.maint.fiix_custom_mobile.constants;

public enum MaintenanceTypes {

    id("id"),
    name("strName"),
    color("strColor"),
    sysCode("intSysCode"),
    updated("intUpdated");

    private final String field;

    private MaintenanceTypes(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }

}
