package org.mobilitychoices.remote;

public class Response<T> {
    private int code;
    private T data;
    private ResponseError error;

    public Response(int code, T data, ResponseError error){
        this.code = code;
        this.data = data;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public ResponseError getError() {
        return error;
    }
}
