package ITM.maint.fiix_custom_mobile.constants;

public enum WorkOrderTasks {

    id("id"),
    workOrderId("intWorkOrderID"),
    taskType("intTaskType"),
    result("strResult"),
    assetID("intAssetID"),
    order("intOrder"),
    startDate("dtmStartDate"),
    completedDate("dtmDateCompleted"),
    completedById("intCompletedByUserID"),
    assignedToId("intAssignedToUserID"),
    estimatedHours("dblTimeEstimatedHours"),
    timeSpentHours("dblTimeSpentHours"),
    meterReadingId("intMeterReadingUnitID"),
    description("strDescription"),
    completionNotes("strTaskNotesCompletion"),
    taskGroupId("intTaskGroupControlID"),
    parentWorkOrderTaskId("intParentWorkOrderTaskID"),
    isUpdated("intUpdated"),
    assignedTo("dv_intAssignedToUserID"),
    completedBy("dv_intCompletedByUserID"),
    workOrderCode("dv_intWorkOrderID"),
    meterReadingUnits("dv_intMeterReadingUnitID"),
    assetName("dv_intAssetID"),
    workOrderTaskId("dv_intParentWorkOrderTaskID"),
    taskGroupName("dv_intTaskGroupControlID"),
    taskResultId("dv_intTaskResultValueID"),
    isCompletable("cf_bolIsCompleteable");

    private final String field;

    private WorkOrderTasks(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
