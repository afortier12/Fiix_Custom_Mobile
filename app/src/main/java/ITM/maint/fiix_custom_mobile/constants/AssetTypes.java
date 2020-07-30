package ITM.maint.fiix_custom_mobile.constants;

public enum AssetTypes {

    code("intSysCode"),
    name("strName"),
    ALL(0),
    LOCATION(1),
    EQUIPMENT(2),
    TOOLS(3),
    PARTS(4),
    STORAGE(5),
    BUILDINGS(6),
    PLANTS(7),
    REGIONS(10),
    ROTATING_SPARES(11);

    private final String field;
    private final int value;

    private AssetTypes(String field) {
        this.field = field;
        this.value = 0;
    }
    private AssetTypes(int value) {
        this.value = value;
        this.field = "";
    }


    public String getField(){
        return field;
    }

    public int getValue() {
        return value;
    }
}
