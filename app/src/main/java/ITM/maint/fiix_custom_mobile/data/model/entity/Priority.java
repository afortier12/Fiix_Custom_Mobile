package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "priority_table")
public class Priority {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private int id;
    @SerializedName("strName")
    @Expose
    @ColumnInfo(name="name")
    private String name;
    @SerializedName("intOrder")
    @Expose
    @ColumnInfo(name="order")
    private int order;
    @SerializedName("intSysCode")
    @Expose
    @ColumnInfo(name="sysCode")
    private int sysCode;
    @SerializedName("intUpdated")
    @Expose
    @ColumnInfo(name="updated")
    private int updated;

    public Priority(int id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSysCode() {
        return sysCode;
    }

    public void setSysCode(int sysCode) {
        this.sysCode = sysCode;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }
}
