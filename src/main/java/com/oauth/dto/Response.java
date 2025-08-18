package com.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response implements Serializable {
    private String status;

    private String message;

    @JsonProperty("status_code")
    private int statusCode;

    private Object data;

    LocalDateTime timeStamp;

    @JsonProperty("response_status")
    private String response_status;

    public static Response buildResponse(String status, String message, int statusCode, Object data, String response_status){
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        response.setStatusCode(statusCode);

        response.setResponse_status(response_status);
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }
}
