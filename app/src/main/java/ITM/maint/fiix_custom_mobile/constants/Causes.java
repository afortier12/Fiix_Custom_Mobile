package ITM.maint.fiix_custom_mobile.constants;

public enum Causes {

    cause("Cause Code"),
    description("Description");

    private final String field;

    private Causes(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }

}
