package ITM.maint.fiix_custom_mobile.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;

@Entity(tableName = "work_order_task_table")
public class WorkOrderTask implements Parcelable {

    public WorkOrderTask() {
    }

    public static final Creator<WorkOrder> CREATOR =
            new Creator<WorkOrder>() {
                @Override
                public WorkOrder createFromParcel(Parcel in) {
                    return new WorkOrder(in);
                }

                @Override
                public WorkOrder[] newArray(int size) {
                    return new WorkOrder[size];
                }
            };

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getAssetID() {
        return assetID;
    }

    public void setAssetID(int assetID) {
        this.assetID = assetID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public int getCompletedById() {
        return completedById;
    }

    public void setCompletedById(int completedById) {
        this.completedById = completedById;
    }

    public int getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(int assignedToId) {
        this.assignedToId = assignedToId;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public double getTimeSpentHours() {
        return timeSpentHours;
    }

    public void setTimeSpentHours(double timeSpentHours) {
        this.timeSpentHours = timeSpentHours;
    }

    public double getMeterReadingId() {
        return meterReadingId;
    }

    public void setMeterReadingId(double meterReadingId) {
        this.meterReadingId = meterReadingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
    }

    public int getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(int taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public int getParentWorkOrderTaskId() {
        return parentWorkOrderTaskId;
    }

    public void setParentWorkOrderTaskId(int parentWorkOrderTaskId) {
        this.parentWorkOrderTaskId = parentWorkOrderTaskId;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    public String getMeterReadingUnits() {
        return meterReadingUnits;
    }

    public void setMeterReadingUnits(String meterReadingUnits) {
        this.meterReadingUnits = meterReadingUnits;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getWorkOrderTaskId() {
        return workOrderTaskId;
    }

    public void setWorkOrderTaskId(String workOrderTaskId) {
        this.workOrderTaskId = workOrderTaskId;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public String getTaskResultId() {
        return taskResultId;
    }

    public void setTaskResultId(String taskResultId) {
        this.taskResultId = taskResultId;
    }

    public String getIsCompletable() {
        return isCompletable;
    }

    public void setIsCompletable(String isCompletable) {
        this.isCompletable = isCompletable;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.workOrderId);
        dest.writeInt(this.taskType);
        dest.writeInt(this.assetID);
        dest.writeInt(this.order);
        dest.writeString(this.startDate);
        dest.writeString(this.completedDate);
        dest.writeInt(this.completedById);
        dest.writeInt(this.assignedToId);
        dest.writeDouble(this.estimatedHours);
        dest.writeDouble(this.timeSpentHours);
        dest.writeDouble(this.meterReadingId);
        dest.writeString(this.description);
        dest.writeString(this.completionNotes);
        dest.writeInt(this.taskGroupId);
        dest.writeInt(this.parentWorkOrderTaskId);
        dest.writeInt(this.isUpdated);
    }
}
