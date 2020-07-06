package ITM.maint.fiix_custom_mobile.constants;

public enum Priorities {

    id("id"),
    name("strName"),
    order("intOrder"),
    sysCode("intSysCode"),
    updated("intUpdated");

    private final String field;

    private Priorities(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
