package com.matjar.usermanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class ResponseError {
    private String reason;
    private String message;
    private Object details;

    public HashMap<String, Object> getResponse(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("reason", reason);
        if (!message.isEmpty())
            result.put("message", message);
        if(details!=null)
            result.put("details", details);
        return result;
    }
}