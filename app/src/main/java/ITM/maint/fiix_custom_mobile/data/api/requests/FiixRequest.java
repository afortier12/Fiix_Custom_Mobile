package ITM.maint.fiix_custom_mobile.data.api.requests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FiixRequest {

    //common for all requests
    @SerializedName("_maCn")
    @Expose
    private String requestClass;

    @SerializedName("service")
    @Expose
    private String service = "cmms";

    @SerializedName("timestamp")
    @Expose
    private long timestamp;

    @SerializedName("clientVersion")
    @Expose
    private ClientVersion clientVersion;

    public String getRequestClass() {
        return requestClass;
    }

    public String getService() {
        return service;
    }

    public long getTimestamp() {
        return timestamp;
    }

    private class ClientVersion {

        @SerializedName("major")
        @Expose
        private int major;

        @SerializedName("minor")
        @Expose
        private int minor;

        @SerializedName("patch")
        @Expose
        private int patch;

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        public int getPatch() {
            return patch;
        }
    }
}
