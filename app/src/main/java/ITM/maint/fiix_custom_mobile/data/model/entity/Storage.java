package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Storage {

    @Entity(tableName = "part_table")
    public class Part {

        @SerializedName("id")
        @Expose
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        private int id;

        @SerializedName("strName")
        @Expose
        @ColumnInfo(name="name")
        private String name;

        @SerializedName("qtyMinStockCount")
        @Expose
        @ColumnInfo(name="minStock")
        private int minStock;

        @SerializedName("strRow")
        @Expose
        @ColumnInfo(name="shelf")
        private int shelf;

        @SerializedName("strAisle")
        @Expose
        @ColumnInfo(name="tray")
        private int tray;

        @SerializedName("strBinNumber")
        @Expose
        @ColumnInfo(name="bin")
        private int bin;

        @SerializedName("strStockLocation")
        @Expose
        @ColumnInfo(name="stockLocation")
        private int stockLocation;

        @SerializedName("qtyStockCount")
        @Expose
        @ColumnInfo(name="inStock")
        private int inStock;

        @SerializedName("strInventoryCode")
        @Expose
        @ColumnInfo(name="invCode")
        private String invCode;

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

        public int getMinStock() {
            return minStock;
        }

        public void setMinStock(int minStock) {
            this.minStock = minStock;
        }

        public int getShelf() {
            return shelf;
        }

        public void setShelf(int shelf) {
            this.shelf = shelf;
        }

        public int getTray() {
            return tray;
        }

        public void setTray(int tray) {
            this.tray = tray;
        }

        public int getBin() {
            return bin;
        }

        public void setBin(int bin) {
            this.bin = bin;
        }

        public int getStockLocation() {
            return stockLocation;
        }

        public void setStockLocation(int stockLocation) {
            this.stockLocation = stockLocation;
        }

        public int getInStock() {
            return inStock;
        }

        public void setInStock(int inStock) {
            this.inStock = inStock;
        }

        public String getInvCode() {
            return invCode;
        }

        public void setInvCode(String invCode) {
            this.invCode = invCode;
        }
    }
}
