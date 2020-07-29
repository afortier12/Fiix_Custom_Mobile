package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity(tableName = "asset_category_table")
public class AssetCategory {

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
    @SerializedName("intParentID")
    @Expose
    @ColumnInfo(name="parentId")
    private int parentId;
    @SerializedName("intSysCode")
    @Expose
    @ColumnInfo(name="code")
    private int code;
    @SerializedName("bolOverrideRules")
    @Expose
    @ColumnInfo(name="overrideFlag")
    private boolean override;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
