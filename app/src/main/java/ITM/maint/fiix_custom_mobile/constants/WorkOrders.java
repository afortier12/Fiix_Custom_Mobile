package ITM.maint.fiix_custom_mobile.constants;

public enum WorkOrders {

    id("id"),
    assignedUserIds("strAssignedUserIds"),
    priorityId("intPriorityID"),
    statusId("intWorkOrderStatusID"),
    assets("strAssets"),
    siteId("intSiteID"),
    assignedUsers("strAssignedUsers"),
    requestedByUserId("intRequestedByUserID"),
    guestEmail("strEmailUserGuest"),
    dateCreated("dtmDateCreated"),
    assetIds("strAssetIds"),
    dateCompleted("dtmDateCompleted"),
    completedByUserId("intCompletedByUserID"),
    description("strDescription"),
    guestName("strNameUserGuest"),
    estCompletionDate("dtmSuggestedCompletionDate"),
    guestPhone("strPhoneUserGuest"),
    code("strCode"),
    completionNotes("strCompletionNotes"),
    maintenanceTypeId("intMaintenanceTypeID"),
    dateModified("dtmDateLastModified"),
    requiresSignature("bolRequiresSignature"),
    dateSigned("dtmDateSigned"),
    signedByUserId("intSignedByUserID"),
    statusGroup("intWorkOrderStatusGroup"),
    adminNotes("strAdminNotes"),
    actionID("intRCAActionID"),
    causeID("intRCACauseID"),
    problemID("intRCAProblemID"),
    completedByUserIds("strCompletedByUserIds"),
    completedByUsers("strCompletedByUsers"),
    customerIds("strCustomerIds"),
    vendorIds("strVendorIds"),
    updated("intUpdated"),
    scheduledMaintenanceId("intScheduledMaintenanceID"),
    priority("dv_intPriorityID"),
    requestedByUser("dv_intRequestedByUserID"),
    site("dv_intSiteID"),
    completedByUser("dv_intCompletedByUserID"),
    workOrderStatus("dv_intWorkOrderStatusID"),
    maintenanceType("dv_intMaintenanceTypeID"),
    action("dv_intRCAActionID"),
    cause("dv_intRCACauseID"),
    problem("dv_intRCAProblemID"),
    scheduledMaintenance("dv_intScheduledMaintenanceID"),
    signedByUser("dv_intSignedByUserID");

    private final String field;

    private WorkOrders(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }

}
