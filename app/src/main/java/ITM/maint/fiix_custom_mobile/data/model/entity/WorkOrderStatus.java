package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "status_table")
public class WorkOrderStatus {


    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private Integer id;
    @SerializedName("intControlID")
    @Expose
    @ColumnInfo(name="controlId")
    private Integer intControlID;
    @SerializedName("strName")
    @Expose
    @ColumnInfo(name="name")
    private String strName;
    @SerializedName("strDefaultLabel")
    @Expose
    @ColumnInfo(name="label")
    private String strDefaultLabel;
    @SerializedName("intSysCode")
    @Expose
    @ColumnInfo(name="code")
    private Integer intSysCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIntControlID() {
        return intControlID;
    }

    public void setIntControlID(Integer intControlID) {
        this.intControlID = intControlID;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDefaultLabel() {
        return strDefaultLabel;
    }

    public void setStrDefaultLabel(String strDefaultLabel) {
        this.strDefaultLabel = strDefaultLabel;
    }

    public Integer getIntSysCode() {
        return intSysCode;
    }

    public void setIntSysCode(Integer intSysCode) {
        this.intSysCode = intSysCode;
    }
}
