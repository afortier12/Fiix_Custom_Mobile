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
    /*@SerializedName("intSiteID")
    @Expose
    @ColumnInfo(name="siteId")*/
    private int siteId;
    @SerializedName("strAssignedUsers")
    @Expose
    @ColumnInfo(name="assignedUsers")
    private String assignedUsers;
    @SerializedName("intRequestedByUserID")
    @Expose
    @ColumnInfo(name="requestedByUserId")
    private int requestedByUserId;
    @SerializedName("strEmailUserGuest")
    @Expose
    @ColumnInfo(name="guestEmail")
    private String guestEmail;
    @SerializedName("dtmDateCreated")
    @Expose
    @ColumnInfo(name="dateCreated")
    private String dateCreated;
    @SerializedName("strAssetIds")
    @Expose
    @ColumnInfo(name="assetIds")
    private String assetIds;
    @SerializedName("dtmDateCompleted")
    @Expose
    @ColumnInfo(name="dateCompleted")
    private String dateCompleted;
    @SerializedName("intCompletedByUserID")
    @Expose
    @ColumnInfo(name="completedByUserId")
    private int completedByUserId;
    @SerializedName("strDescription")
    @Expose
    @ColumnInfo(name="description")
    private String description;
    @SerializedName("strNameUserGuest")
    @Expose
    @ColumnInfo(name="guestName")
    private String guestName;
    @SerializedName("dtmSuggestedCompletionDate")
    @Expose
    @ColumnInfo(name="estCompletionDate")
    private String estCompletionDate;
    /*@SerializedName("strPhoneUserGuest")
    @Expose
    @ColumnInfo(name="guestPhone")*/
    private String guestPhone;
    @SerializedName("strCode")
    @Expose
    @ColumnInfo(name="code")
    private String code;
    @SerializedName("strCompletionNotes")
    @Expose
    @ColumnInfo(name="completionNotes")
    private String completionNotes;
    @SerializedName("intMaintenanceTypeID")
    @Expose
    @ColumnInfo(name="maintenanceTypeId")
    private String maintenanceTypeId;
    @SerializedName("dtmDateLastModified")
    @Expose
    @ColumnInfo(name="dateModified")
    private String StringdateModified;
    /*@SerializedName("bolRequiresSignature")
    @Expose
    @ColumnInfo(name="requiresSignature")*/
    private String requiresSignature;
    /*@SerializedName("dtmDateSigned")
    @Expose
    @ColumnInfo(name="dateSigned")*/
    private String dateSigned;
    /*@SerializedName("intSignedByUserID")
    @Expose
    @ColumnInfo(name="signedByUserId")*/
    private String signedByUserId;
    /*@SerializedName("intWorkOrderStatusGroup")
    @Expose
    @ColumnInfo(name="statusGroup")*/
    private String statusGroup;
    @SerializedName("strAdminNotes")
    @Expose
    @ColumnInfo(name="adminNotes")
    private String adminNotes;
    @SerializedName("intRCAActionID")
    @Expose
    @ColumnInfo(name="actionID")
    private int actionID;
    @SerializedName("intRCACauseID")
    @Expose
    @ColumnInfo(name="causeID")
    private int causeID;
    @SerializedName("intRCAProblemID")
    @Expose
    @ColumnInfo(name="problemID")
    private int problemID;
    @SerializedName("strCompletedByUserIds")
    @Expose
    @ColumnInfo(name="completedByUserIds")
    private String completedByUserIds;
    @SerializedName("strCompletedByUsers")
    @Expose
    @ColumnInfo(name="completedByUsers")
    private String completedByUsers;
    /*@SerializedName("strCustomerIds")
    @Expose
    @ColumnInfo(name="customerIds")*/
    private String customerIds;
    /*@SerializedName("strVendorIds")
    @Expose
    @ColumnInfo(name="vendorIds")*/
    private String vendorIds;
    @SerializedName("intUpdated")
    @Expose
    @ColumnInfo(name="updated")
    private int updated;
    @SerializedName("intScheduledMaintenanceID")
    @Expose
    @ColumnInfo(name="scheduledMaintenanceId")
    private int scheduledMaintenanceId;
    @SerializedName("dv_intPriorityID")
    @Expose
    @ColumnInfo(name="priority")
    private String priority;
    @SerializedName("dv_intRequestedByUserID")
    @Expose
    @ColumnInfo(name="requestedByUser")
    private String requestedByUser;
    @SerializedName("dv_intSiteID")
    @Expose
    @ColumnInfo(name="site")
    private String site;
    @SerializedName("dv_intCompletedByUserID")
    @Expose
    @ColumnInfo(name="completedByUser")
    private String completedByUser;
    @SerializedName("dv_intWorkOrderStatusID")
    @Expose
    @ColumnInfo(name="workOrderStatus")
    private String workOrderStatus;
    @SerializedName("dv_intMaintenanceTypeID")
    @Expose
    @ColumnInfo(name="maintenanceType")
    private String maintenanceType;
    @SerializedName("dv_intRCAActionID")
    @Expose
    @ColumnInfo(name="action")
    private String action;
    @SerializedName("dv_intRCACauseID")
    @Expose
    @ColumnInfo(name="cause")
    private String cause;
    @SerializedName("dv_intRCAProblemID")
    @Expose
    @ColumnInfo(name="problem")
    private String problem;
    @SerializedName("dv_intScheduledMaintenanceID")
    @Expose
    @ColumnInfo(name="scheduledMaintenance")
    private String scheduledMaintenance;
    /*@SerializedName("dv_intSignedByUserID")
    @Expose
    @ColumnInfo(name="signedByUser")*/
    private String signedByUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignedUserIds() {
        return assignedUserIds;
    }

    public void setAssignedUserIds(String assignedUserIds) {
        this.assignedUserIds = assignedUserIds;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(String assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public int getRequestedByUserId() {
        return requestedByUserId;
    }

    public void setRequestedByUserId(int requestedByUserId) {
        this.requestedByUserId = requestedByUserId;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAssetIds() {
        return assetIds;
    }

    public void setAssetIds(String assetIds) {
        this.assetIds = assetIds;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public int getCompletedByUserId() {
        return completedByUserId;
    }

    public void setCompletedByUserId(int completedByUserId) {
        this.completedByUserId = completedByUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getEstCompletionDate() {
        return estCompletionDate;
    }

    public void setEstCompletionDate(String estCompletionDate) {
        this.estCompletionDate = estCompletionDate;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
    }

    public String getMaintenanceTypeId() {
        return maintenanceTypeId;
    }

    public void setMaintenanceTypeId(String maintenanceTypeId) {
        this.maintenanceTypeId = maintenanceTypeId;
    }

    public String getStringdateModified() {
        return StringdateModified;
    }

    public void setStringdateModified(String stringdateModified) {
        StringdateModified = stringdateModified;
    }

    public String getRequiresSignature() {
        return requiresSignature;
    }

    public void setRequiresSignature(String requiresSignature) {
        this.requiresSignature = requiresSignature;
    }

    public String getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(String dateSigned) {
        this.dateSigned = dateSigned;
    }

    public String getSignedByUserId() {
        return signedByUserId;
    }

    public void setSignedByUserId(String signedByUserId) {
        this.signedByUserId = signedByUserId;
    }

    public String getStatusGroup() {
        return statusGroup;
    }

    public void setStatusGroup(String statusGroup) {
        this.statusGroup = statusGroup;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public int getActionID() {
        return actionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public int getCauseID() {
        return causeID;
    }

    public void setCauseID(int causeID) {
        this.causeID = causeID;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getCompletedByUserIds() {
        return completedByUserIds;
    }

    public void setCompletedByUserIds(String completedByUserIds) {
        this.completedByUserIds = completedByUserIds;
    }

    public String getCompletedByUsers() {
        return completedByUsers;
    }

    public void setCompletedByUsers(String completedByUsers) {
        this.completedByUsers = completedByUsers;
    }

    public String getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(String customerIds) {
        this.customerIds = customerIds;
    }

    public String getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(String vendorIds) {
        this.vendorIds = vendorIds;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getScheduledMaintenanceId() {
        return scheduledMaintenanceId;
    }

    public void setScheduledMaintenanceId(int scheduledMaintenanceId) {
        this.scheduledMaintenanceId = scheduledMaintenanceId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRequestedByUser() {
        return requestedByUser;
    }

    public void setRequestedByUser(String requestedByUser) {
        this.requestedByUser = requestedByUser;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCompletedByUser() {
        return completedByUser;
    }

    public void setCompletedByUser(String completedByUser) {
        this.completedByUser = completedByUser;
    }

    public String getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(String workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getScheduledMaintenance() {
        return scheduledMaintenance;
    }

    public void setScheduledMaintenance(String scheduledMaintenance) {
        this.scheduledMaintenance = scheduledMaintenance;
    }

    public String getSignedByUser() {
        return signedByUser;
    }

    public void setSignedByUser(String signedByUser) {
        this.signedByUser = signedByUser;
    }
}
