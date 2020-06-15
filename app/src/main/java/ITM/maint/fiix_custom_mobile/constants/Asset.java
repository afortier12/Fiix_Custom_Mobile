package ITM.maint.fiix_custom_mobile.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Asset {

    private static List<String> fields = new ArrayList<>(Arrays.asList(
            "id",
            "strName",
            "strDescription",
            "strMake",
            "strModel",
            "qtyMinStockCount",
            "strCity",
            "strShippingTerms",
            "strAddress",
            "strNotes",
            "strProvince",
            "intCountryID",
            "strInventoryCode",
            "qtyStockCount",
            "intSiteID",
            "strRow",
            "strMASourceProduct",
            "strAisle",
            "strBinNumber",
            "intCategoryID",
            "strPostalCode",
            "strSerialNumber",
            "strCode",
            "dblLatitude",
            "dblLongitude",
            "strUnspcCode",
            "dblLastPrice",
            "bolIsBillToFacility",
            "intAssetLocationID",
            "bolIsOnline",
            "bolIsShippingOrReceivingFacility",
            "intKind",
            "strQuotingTerms",
            "intAssetParentID",
            "bolIsRegion",
            "bolIsSite",
            "intAccountID",
            "intChargeDepartmentID",
            "intSuperCategorySysCode",
            "strCustomerIds",
            "strStockLocation",
            "strVendorIds",
            "intUpdated",
            "strBarcode",
            "dv_intAccountID",
            "dv_intCategoryID",
            "dv_intSiteID",
            "dv_intCountryID",
            "dv_intAssetLocationID",
            "dv_intChargeDepartmentID",
            "dv_intAssetParentID",
            "cf_getLatestReadingsFor",
            "cf_intDefaultImageFileThumbnailID",
            "cf_intDefaultImageFileID",
            "cf_intAddressAssetID",
            "cf_assetAddressString"
    ));

    public static String getFields(){
        return fields.stream().collect(Collectors.joining(","));
    }
}
