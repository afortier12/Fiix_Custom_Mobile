package ITM.maint.fiix_custom_mobile.data.api.requests;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.Storage;

public class PartRequest extends FiixRequest {

    private List<String> fieldNames = new ArrayList<>(Arrays.asList(
            "id",
            "strName",
            "strDescription",
            "strMake",
            "strModel",
            "strCode",
            "strAisle",
            "strRow",
            "strBin",
            "intCategoryID",
            "intSiteID"
    ));

    @SerializedName("className")
    @Expose
    @Ignore
    private String className;

    @SerializedName("fields")
    @Expose
    @Ignore
    private String fields = fieldNames.toString();

    @SerializedName("object")
    @Expose
    @Ignore
    private RequestObject requestObject;


    private class RequestObject{

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("strName")
        @Expose
        private String name;

        @SerializedName("strDescription")
        @Expose
        private String description;

        @SerializedName("strMake")
        @Expose
        private String make;

        @SerializedName("strModel")
        @Expose
        private String model;

        @SerializedName("strCode")
        @Expose
        private String partNumber;

        @SerializedName("strBarcode")
        @Expose
        private String barcode;

        @SerializedName("intCategoryID")
        @Expose
        private int categoryID;


    }
}
