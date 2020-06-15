package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "part_table")
public class Part {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private int id;

    public Part(int id){
        this.id = id;
    }

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="make")
    private String make;

    @ColumnInfo(name="model")
    private String model;

    @ColumnInfo(name="storage_id")
    private int storage_id;

    @ColumnInfo(name="barcode")
    private String barcode;

    @ColumnInfo(name="count")
    private int count;

    @ColumnInfo(name="category_id")
    private int category_id;

    @ColumnInfo(name="site_id")
    private int site_id;

    @ColumnInfo(name="cf_intDefaultImageFileThumbnailID")
    private int thumbnail;

    @ColumnInfo(name="unspcCode")
    private String unspcCode;

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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }
}
