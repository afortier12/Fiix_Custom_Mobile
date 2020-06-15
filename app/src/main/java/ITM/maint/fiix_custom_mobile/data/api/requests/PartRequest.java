package ITM.maint.fiix_custom_mobile.data.api.requests;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Part;


public class PartRequest {

    @SerializedName("_maCn")
    private String maCn;
    @SerializedName("clientVersion")
    private ClientVersion clientVersion;
    @SerializedName("className")
    private String className;
    @SerializedName("fields")
    private String fields;

    public PartRequest(String maCn, ClientVersion clientVersion, String className, String fields) {
        this.maCn = maCn;
        this.clientVersion = clientVersion;
        this.className = className;
        this.fields = fields;
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