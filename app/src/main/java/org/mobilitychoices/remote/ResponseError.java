package org.mobilitychoices.remote;

public class ResponseError {
    private String code;
    private String message;
    private String target;

    public ResponseError(String code, String message, String target) {
        this.code = code;
        this.message = message;
        this.target = target;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getTarget() {
        return target;
    }
}
