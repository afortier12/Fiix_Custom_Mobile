package ITM.maint.fiix_custom_mobile.data.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.FiixObject;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public class FindResponse {

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
    private List<FiixObject> objects = null;
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

    public List<FiixObject> getObjects() {
        return objects;
    }

    public void setObjects(List<FiixObject> objects) {
        this.objects = objects;
    }

    public Integer getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(Integer totalObjects) {
        this.totalObjects = totalObjects;
    }



}
