package ITM.maint.fiix_custom_mobile.constants;

public enum Assets {
    id("id"),
    name( "strName"),
    description("strDescription"),
    make("strMake"),
    model("strModel"),
    minStock("qtyMinStockCount"),
    city("strCity"),
    shippingTerms("strShippingTerms"),
    address("strAddress"),
    notes("strNotes"),
    province("strProvince"),
    countryId("intCountryID"),
    invCode("strInventoryCode"),
    inStock("qtyStockCount"),
    siteID("intSiteID"),
    shelf("strRow"),
    tray("strAisle"),
    bin("strBinNumber"),
    categoryId("intCategoryID"),
    postalCode("strPostalCode"),
    serialNumber("strSerialNumber"),
    code("strCode"),
    latitude("dblLatitude"),
    longitude("dblLongitude"),
    partNumber("strUnspcCode"),
    lastPrice("dblLastPrice"),
    isBillToFacility("bolIsBillToFacility"),
    assetLocationID("intAssetLocationID"),
    isOnline("bolIsOnline"),
    isShippingReceivingFacility("bolIsShippingOrReceivingFacility"),
    assetType("intKind"),
    quoteTerms("strQuotingTerms"),
    assetParentID("intAssetParentID"),
    isRegion("bolIsRegion"),
    isSite("bolIsSite"),
    accountID("intAccountID"),
    chargeDepartment("intChargeDepartmentID"),
    superCategorySysCode("intSuperCategorySysCode"),
    customerIds("strCustomerIds"),
    stockLocation("strStockLocation"),
    vendorIds("strVendorIds"),
    updated("intUpdated"),
    barcode("strBarcode"),
    displayAccountId("dv_intAccountID"),
    displayCategoryId("dv_intCategoryID"),
    displaySiteId("dv_intSiteID"),
    displayCountryId("dv_intCountryID"),
    displayAssetLocationId("dv_intAssetLocationID"),
    displayChargeDepartmentId("dv_intChargeDepartmentID"),
    displayAssetParentId("dv_intAssetParentID"),
    customGetLatestReading("cf_getLatestReadingsFor"),
    customDefaultFileThumbnailId("cf_intDefaultImageFileThumbnailID"),
    customDefaultImageFileID("cf_intDefaultImageFileID"),
    customAddressAssetId("cf_intAddressAssetID"),
    customAddressString("cf_assetAddressString");

    private final String field;

    private Assets(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
