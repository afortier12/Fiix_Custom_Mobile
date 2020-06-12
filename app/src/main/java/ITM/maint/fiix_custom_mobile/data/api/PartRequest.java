package ITM.maint.fiix_custom_mobile.data.api;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Part;


@Entity(tableName = "part_table")
public class PartRequest {

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
    private List<Part> parts = null;
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

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public Integer getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(Integer totalObjects) {
        this.totalObjects = totalObjects;
    }

    private class ServerVersion {

        @SerializedName("major")
        @Expose
        @Ignore
        private Integer major;
        @SerializedName("minor")
        @Expose
        @Ignore
        private Integer minor;
        @SerializedName("patch")
        @Expose
        @Ignore
        private Integer patch;
        @SerializedName("preRelease")
        @Ignore
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

    private class Sync {

        @SerializedName("revision")
        @Expose
        @Ignore
        private Integer revision;
        @SerializedName("needToSync")
        @Expose
        @Ignore
        private Boolean needToSync;
        @SerializedName("dateLastUpdated")
        @Expose
        @Ignore
        private Integer dateLastUpdated;

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

        public Integer getDateLastUpdated() {
            return dateLastUpdated;
        }

        public void setDateLastUpdated(Integer dateLastUpdated) {
            this.dateLastUpdated = dateLastUpdated;
        }

    }

}