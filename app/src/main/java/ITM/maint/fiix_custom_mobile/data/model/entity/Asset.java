package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity(tableName = "asset_table")
public class Asset {

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
    @SerializedName("strDescription")
    @Expose
    @ColumnInfo(name="description")
    private String description;
    @SerializedName("intKind")
    @Expose
    @ColumnInfo(name="type")
    private int type;
    @SerializedName("intCategoryID")
    @Expose
    @ColumnInfo(name="categoryId")
    private String categoryId;
    @SerializedName("strMake")
    @Expose
    @ColumnInfo(name="make")
    private String make;
    @SerializedName("strModel")
    @Expose
    @ColumnInfo(name="model")
    private String model;
    @SerializedName("strAisle")
    @Expose
    @ColumnInfo(name="storage_id")
    private int storage_id;
    @SerializedName("strBarcode")
    @Expose
    @ColumnInfo(name="barcode")
    private String barcode;
    @SerializedName("qtyStockCount")
    @Expose
    @ColumnInfo(name="count")
    private int count;
    @SerializedName("dv_intCategoryID")
    @Expose
    @ColumnInfo(name="category_id__description")
    private String dvCategoryID;
    @ColumnInfo(name="site_id_description")
    private int dvSiteId;
    @ColumnInfo(name="cf_intDefaultImageFileThumbnailID")
    private int thumbnail;
    @SerializedName("strUnspcCode")
    @Expose
    @ColumnInfo(name="unspcCode")
    private String unspcCode;
    @ColumnInfo(name="lastRefresh")
    private Date lastRefresh;

    public Asset(int id){
        this.id = id;
    }

    public String getUnspcCode() {
        return unspcCode;
    }

    public void setUnspcCode(String unspcCode) {
        this.unspcCode = unspcCode;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(int storage_id) {
        this.storage_id = storage_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDvCategoryID() {
        return dvCategoryID;
    }

    public void setDvCategoryID(String dvCategoryID) {
        this.dvCategoryID = dvCategoryID;
    }

    public int getDvSiteId() {
        return dvSiteId;
    }

    public void setDvSiteId(int dvSiteId) {
        this.dvSiteId = dvSiteId;
    }
}
