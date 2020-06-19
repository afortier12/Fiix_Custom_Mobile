package ITM.maint.fiix_custom_mobile.data.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
