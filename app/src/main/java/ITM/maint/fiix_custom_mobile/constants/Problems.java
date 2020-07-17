package ITM.maint.fiix_custom_mobile.constants;

public enum Problems {

    problem("Problem Code"),
    description("Description");

    private final String field;

    private Problems(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
