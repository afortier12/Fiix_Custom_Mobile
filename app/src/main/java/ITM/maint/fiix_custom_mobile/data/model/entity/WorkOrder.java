package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "work_order_table")
public class WorkOrder {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private int id;
    @SerializedName("strAssignedUserIds")
    @Expose
    @ColumnInfo(name="assignedUserIds")
    private String assignedUserIds;
    @SerializedName("intPriorityID")
    @Expose
    @ColumnInfo(name="priorityId")
    private String priorityId;
    @SerializedName("intWorkOrderStatusID")
    @Expose
    @ColumnInfo(name="statusId")
    private int statusId;
    @SerializedName("strAssets")
    @Expose
    @ColumnInfo(name="assets")
    private String assets;
    @SerializedName("intSiteID")
    @Expose
    @ColumnInfo(name="siteId")
    private int siteId;
    @SerializedName("strAssignedUsers")
    @Expose
    @ColumnInfo(name="assignedUsers")
    private String assignedUsers;
    @SerializedName("intRequestedByUserID")
    @Expose
    @ColumnInfo(name="requestedByUserId")
    private int requestedByUserId;

}
