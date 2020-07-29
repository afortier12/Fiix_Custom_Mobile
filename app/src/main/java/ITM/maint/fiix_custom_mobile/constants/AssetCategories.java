package ITM.maint.fiix_custom_mobile.constants;

public enum AssetCategories {

    id("id"),
    parentId("intParentID"),
    name( "strName"),
    sysCode("intSysCode"),
    overrideRules("bolOverrideRules"),
    updated("intUpdated");

    private final String field;

    private AssetCategories(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
