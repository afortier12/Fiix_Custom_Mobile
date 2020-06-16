package ITM.maint.fiix_custom_mobile.data.api.responses;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public class PartResponse {
    @SerializedName("_maCn")
    @Expose
    private String maCn;
    @SerializedName("serverVersion")
    @Expose
    private ServerVersion serverVersion;
    @SerializedName("sync")
    @Expose
    private Sync sync;
    @SerializedName("objects")
    @Expose
    private List<Part> objects = null;
    @SerializedName("totalObjects")
    @Expose
    private Integer totalObjects;

    public String getMaCn() {
        return maCn;
    }

    public void setMaCn(String maCn) {
        this.maCn = maCn;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public Sync getSync() {
        return sync;
    }

    public void setSync(Sync sync) {
        this.sync = sync;
    }

    public List<Part> getObjects() {
        return objects;
    }

    public void setObjects(List<Part> objects) {
        this.objects = objects;
    }

    public Integer getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(Integer totalObjects) {
        this.totalObjects = totalObjects;
    }

    public class ServerVersion {

        @SerializedName("major")
        @Expose
        private Integer major;
        @SerializedName("minor")
        @Expose
        private Integer minor;
        @SerializedName("patch")
        @Expose
        private Integer patch;
        @SerializedName("preRelease")
        @Expose
        private String preRelease;

        public Integer getMajor() {
            return major;
        }

        public void setMajor(Integer major) {
            this.major = major;
        }

        public Integer getMinor() {
            return minor;
        }

        public void setMinor(Integer minor) {
            this.minor = minor;
        }

        public Integer getPatch() {
            return patch;
        }

        public void setPatch(Integer patch) {
            this.patch = patch;
        }

        public String getPreRelease() {
            return preRelease;
        }

        public void setPreRelease(String preRelease) {
            this.preRelease = preRelease;
        }

    }

    public class Sync {

        @SerializedName("revision")
        @Expose
        private Integer revision;
        @SerializedName("needToSync")
        @Expose
        private Boolean needToSync;
        @SerializedName("dateLastUpdated")
        @Expose
        private Long dateLastUpdated;

        public Integer getRevision() {
            return revision;
        }

        public void setRevision(Integer revision) {
            this.revision = revision;
        }

        public Boolean getNeedToSync() {
            return needToSync;
        }

        public void setNeedToSync(Boolean needToSync) {
            this.needToSync = needToSync;
        }

        public Long getDateLastUpdated() {
            return dateLastUpdated;
        }

        public void setDateLastUpdated(Long dateLastUpdated) {
            this.dateLastUpdated = dateLastUpdated;
        }

    }
}


