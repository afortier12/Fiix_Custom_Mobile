package ITM.maint.fiix_custom_mobile.data.model;

import android.icu.text.SearchIterator;

public class Part {

    private int id;
    private String strName;
    private String strDescription;
    private String strMake;
    private String strModel;
    private Storage storage;
    private String strBarcode;
    private int qtyStockCount;
    private AssetCategory assetCategory;
    private Site site;

    public Part(String strBarcode) {
        this.strBarcode = strBarcode;
    }

    public Part(String strName, String strDescription, String strMake, String strModel, String strBarcode) {
        this.strName = strName;
        this.strDescription = strDescription;
        this.strMake = strMake;
        this.strModel = strModel;
        this.strBarcode = strBarcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrMake() {
        return strMake;
    }

    public void setStrMake(String strMake) {
        this.strMake = strMake;
    }

    public String getStrModel() {
        return strModel;
    }

    public void setStrModel(String strModel) {
        this.strModel = strModel;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String getStrBarcode() {
        return strBarcode;
    }

    public void setStrBarcode(String strBarcode) {
        this.strBarcode = strBarcode;
    }

    public int getQtyStockCount() {
        return qtyStockCount;
    }

    public void setQtyStockCount(int qtyStockCount) {
        this.qtyStockCount = qtyStockCount;
    }

    public AssetCategory getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
