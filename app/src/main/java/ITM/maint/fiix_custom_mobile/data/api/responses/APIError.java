package ITM.maint.fiix_custom_mobile.data.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("leg")
    @Expose
    private String leg;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("stackTrace")
    @Expose
    private String stackTrace;
    @SerializedName("object")
    @Expose
    private EmptyObject object;

    public String getLeg() {
        return leg;
    }

    public void setLeg(String leg) {
        this.leg = leg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public EmptyObject getObject() {
        return object;
    }

    public void setObject(EmptyObject object) {
        this.object = object;
    }

    private class EmptyObject{

        public EmptyObject() {
        }
    }

}
