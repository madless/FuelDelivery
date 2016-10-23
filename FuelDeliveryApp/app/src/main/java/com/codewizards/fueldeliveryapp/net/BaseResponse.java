package com.codewizards.fueldeliveryapp.net;

/**
 * Created by madless on 23.10.2016.
 */
public class BaseResponse {
    private Integer status;
    private String message;

    public BaseResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
