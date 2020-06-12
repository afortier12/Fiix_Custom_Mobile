package ITM.maint.fiix_custom_mobile.data.model;

import android.icu.text.SearchIterator;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ITM.maint.fiix_custom_mobile.data.api.PartRequest;


public class Part {

    @SerializedName("className")
    @Ignore
    private String className;
    @SerializedName("extraFields")
    @Expose
    private ExtraFields extraFields;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("dblWeightedPrice")
    @Ignore
    private Double dblWeightedPrice;
    @ColumnInfo(name = "strCode")
    @SerializedName("strCode")
    @Expose
    private String strCode;
    @ColumnInfo(name = "strMake")
    @SerializedName("strMake")
    @Expose
    private String strMake;
    @ColumnInfo(name = "strName")
    @SerializedName("strName")
    @Expose
    private String strName;

    @SerializedName("strAddressParsed")
    @Ignore
    private String strAddressParsed;
    @SerializedName("new")
    @Ignore
    private Boolean _new;
    @SerializedName("dirty")
    @Ignore
    private Boolean dirty;
    @SerializedName("uideleted")
    @Ignore
    private Boolean uideleted;

    private class ExtraFields {

        @ColumnInfo(name = "dv_intCategoryID")
        @SerializedName("dv_intCategoryID")
        @Expose
        private String dvIntCategoryID;

        public String getDvIntCategoryID() {
            return dvIntCategoryID;
        }

        public void setDvIntCategoryID(String dvIntCategoryID) {
            this.dvIntCategoryID = dvIntCategoryID;
        }

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ExtraFields getExtraFields() {
        return extraFields;
    }

    public void setExtraFields(ExtraFields extraFields) {
        this.extraFields = extraFields;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDblWeightedPrice() {
        return dblWeightedPrice;
    }

    public void setDblWeightedPrice(Double dblWeightedPrice) {
        this.dblWeightedPrice = dblWeightedPrice;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public String getStrMake() {
        return strMake;
    }

    public void setStrMake(String strMake) {
        this.strMake = strMake;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrAddressParsed() {
        return strAddressParsed;
    }

    public void setStrAddressParsed(String strAddressParsed) {
        this.strAddressParsed = strAddressParsed;
    }

    public Boolean getNew() {
        return _new;
    }

    public void setNew(Boolean _new) {
        this._new = _new;
    }

    public Boolean getDirty() {
        return dirty;
    }

    public void setDirty(Boolean dirty) {
        this.dirty = dirty;
    }

    public Boolean getUideleted() {
        return uideleted;
    }

    public void setUideleted(Boolean uideleted) {
        this.uideleted = uideleted;
    }
}
