package ITM.maint.fiix_custom_mobile.constants;

public enum Users {
    id("id"),
    name("strFullName"),
    statusID("intUserStatusID"),
    mobile("strTelephone2"),
    email("strEmailAddress"),
    title("strUserTitle"),
    code("strPersonnelCode"),
    username("strUserName"),
    phone("strTelephone"),
    notes("strNotes"),
    dateCreated("strRequestNotes"),
    isGroup("bolGroup"),
    isApiOnly("bolApiApplicationUser"),
    isApiManaged("bolApiManaged"),
    updated("intUpdated"),
    preferences("strPreferences"),
    localization("dv_intLocalizationID"),
    siteID("cf_intSiteIDs"),
    defaultImage("cf_intDefaultImageFileID"),
    defaultThumbnail("cf_intDefaultImageFileThumbnailID");

    private final String field;

    private Users(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
