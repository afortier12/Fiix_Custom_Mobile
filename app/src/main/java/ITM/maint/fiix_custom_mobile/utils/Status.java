package ITM.maint.fiix_custom_mobile.utils;

import ITM.maint.fiix_custom_mobile.constants.StatusCodes;

public class Status {

    private StatusCodes code;
    private String source;
    private String message;

    public Status(StatusCodes code, String source, String message) {
        this.code = code;
        this.source = source;
        this.message = message;
    }

    public StatusCodes getCode() {
        return code;
    }

    public void setCode(StatusCodes code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
