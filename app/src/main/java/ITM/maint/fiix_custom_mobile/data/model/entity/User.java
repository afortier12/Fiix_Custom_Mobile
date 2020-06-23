package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_table")
public class User extends FiixObject{

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private int id;
    @SerializedName("strFullName")
    @Expose
    @ColumnInfo(name="name")
    private String name;
    @SerializedName("intUserStatusID")
    @Expose
    @ColumnInfo(name="status")
    private int statusID;
    @SerializedName("strTelephone2")
    @Expose
    @ColumnInfo(name="mobile")
    private String mobile;
    @SerializedName("strEmailAddress")
    @Expose
    @ColumnInfo(name="email")
    private String email;
    @SerializedName("strUserTitle")
    @Expose
    @ColumnInfo(name="title")
    private String title;
    @SerializedName("strPersonnelCode")
    @Expose
    @ColumnInfo(name="code")
    private String code;
    @SerializedName("strUserName")
    @Expose
    @ColumnInfo(name="username")
    private String username;
    @SerializedName("strTelephone")
    @Expose
    @ColumnInfo(name="phone")
    private String phone;
    @SerializedName("strNotes")
    @Expose
    @ColumnInfo(name="notes")
    private String notes;
    @SerializedName("strRequestNotes")
    @Expose
    @ColumnInfo(name="dateCreated")
    private String dateCreated;
    @SerializedName("bolGroup")
    @Expose
    @ColumnInfo(name="isGroup")
    private int isGroup;
    @SerializedName("cf_intSiteIDs")
    @Expose
    @ColumnInfo(name="siteID")
    private int siteID;

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }
}
