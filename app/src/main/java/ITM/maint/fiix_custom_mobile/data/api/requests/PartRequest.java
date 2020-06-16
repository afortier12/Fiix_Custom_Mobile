package ITM.maint.fiix_custom_mobile.data.api.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PartRequest {

    @SerializedName("_maCn")
    private String maCn;
    @SerializedName("clientVersion")
    private ClientVersion clientVersion;
    @SerializedName("className")
    private String className;
    @SerializedName("fields")
    private String fields;
    @SerializedName("filters")
    private List<Filter> filter;

    public PartRequest(String maCn, ClientVersion clientVersion, String className, String fields, List<Filter> filter) {
        this.maCn = maCn;
        this.clientVersion = clientVersion;
        this.className = className;
        this.fields = fields;
        this.filter = filter;
    }

    public static class ClientVersion {

        @SerializedName("major")
        private Integer major;
        @SerializedName("minor")
        private Integer minor;
        @SerializedName("patch")
        private Integer patch;

        public ClientVersion(Integer major, Integer minor, Integer patch) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }
    }

    public static class Filter {

        @SerializedName("ql")
        private String ql;
        @SerializedName("parameters")
        private List parameters;

        public Filter(String ql, List parameters) {
            this.ql = ql;
            this.parameters = parameters;
        }

    }

}