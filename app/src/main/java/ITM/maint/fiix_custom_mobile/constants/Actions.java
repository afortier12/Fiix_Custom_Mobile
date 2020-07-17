package ITM.maint.fiix_custom_mobile.constants;

public enum Actions {

    cause("Action Code"),
    description("Description");

    private final String field;

    private Actions(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
