package ITM.maint.fiix_custom_mobile.data.api.requests;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ChangeRequest {

    @SerializedName("_maCn")
    private String maCn;
    @SerializedName("clientVersion")
    private ClientVersion clientVersion;
    @SerializedName("className")
    private String className;
    @SerializedName("fields")
    private String fields;
    @SerializedName("changeFields")
    private String changeFields;
    @SerializedName("object")
    private JsonObject jsonObject;


    public ChangeRequest(String maCn, ClientVersion clientVersion, String className, String fields, JsonObject jsonObject) {
        this.maCn = maCn;
        this.clientVersion = clientVersion;
        this.className = className;
        this.fields = fields;
        this.jsonObject = jsonObject;
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


}