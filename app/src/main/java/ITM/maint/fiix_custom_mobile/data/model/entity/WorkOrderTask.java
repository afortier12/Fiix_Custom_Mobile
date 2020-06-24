package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "work_order_table")
public class WorkOrderTask {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private int id;
    @SerializedName("intWorkOrderID")
    @Expose
    @ColumnInfo(name="workOrderId")
    private int workOrderId;
    @SerializedName("intTaskType")
    @Expose
    @ColumnInfo(name="taskType")
    private int taskType;
    /*@SerializedName("strResult")
    @Expose
    @ColumnInfo(name="result")*/
    private String result;
    @SerializedName("intAssetID")
    @Expose
    @ColumnInfo(name="assetID")
    private int assetID;
    @SerializedName("intOrder")
    @Expose
    @ColumnInfo(name="order")
    private int order;
    @SerializedName("dtmStartDate")
    @Expose
    @ColumnInfo(name="startDate")
    private String startDate;
    @SerializedName("dtmDateCompleted")
    @Expose
    @ColumnInfo(name="completedDate")
    private String completedDate;
    @SerializedName("intCompletedByUserID")
    @Expose
    @ColumnInfo(name="completedById")
    private int completedById;
    @SerializedName("intAssignedToUserID")
    @Expose
    @ColumnInfo(name="assignedToId")
    private int assignedToId;
    @SerializedName("dblTimeEstimatedHours")
    @Expose
    @ColumnInfo(name="estimatedHours")
    private double estimatedHours;
    @SerializedName("dblTimeSpentHours")
    @Expose
    @ColumnInfo(name="timeSpentHours")
    private double timeSpentHours;
    @SerializedName("intMeterReadingUnitID")
    @Expose
    @ColumnInfo(name="meterReadingId")
    private double meterReadingId;
    @SerializedName("strDescription")
    @Expose
    @ColumnInfo(name="description")
    private String description;
    @SerializedName("strTaskNotesCompletion")
    @Expose
    @ColumnInfo(name="completionNotes")
    private String completionNotes;
    @SerializedName("intTaskGroupControlID")
    @Expose
    @ColumnInfo(name="taskGroupId")
    private int taskGroupId;
    @SerializedName("intParentWorkOrderTaskID")
    @Expose
    @ColumnInfo(name="parentWorkOrderTaskId")
    private int parentWorkOrderTaskId;
    @SerializedName("intUpdated")
    @Expose
    @ColumnInfo(name="isUpdated")
    private int isUpdated;
    @SerializedName("dv_intAssignedToUserID")
    @Expose
    @ColumnInfo(name="assignedTo")
    private String assignedTo;
    @SerializedName("dv_intCompletedByUserID")
    @Expose
    @ColumnInfo(name="completedBy")
    private String completedBy;
    @SerializedName("dv_intWorkOrderID")
    @Expose
    @ColumnInfo(name="workOrderCode")
    private String workOrderCode;
    @SerializedName("dv_intMeterReadingUnitID")
    @Expose
    @ColumnInfo(name="meterReadingUnits")
    private String meterReadingUnits;
    @SerializedName("dv_intAssetID")
    @Expose
    @ColumnInfo(name="assetName")
    private String assetName;
    @SerializedName("dv_intParentWorkOrderTaskID")
    @Expose
    @ColumnInfo(name="workOrderTaskId")
    private String workOrderTaskId;
    @SerializedName("dv_intTaskGroupControlID")
    @Expose
    @ColumnInfo(name="taskGroupName")
    private String taskGroupName;
    /*@SerializedName("dv_intTaskResultValueID")
    @Expose
    @ColumnInfo(name="taskResultId")*/
    private String taskResultId;
    @SerializedName("cf_bolIsCompleteable")
    @Expose
    @ColumnInfo(name="isCompletable")
    private String isCompletable;
}
