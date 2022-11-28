package com.example.medisantventas.config;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final String HOST = "192.168.1.72";
    public static final String URL = "http://"+HOST+"/proyecto-9no-web/medisant-admin/public/";

    //public static final String URL = "https://medisant.mx/";

    public Map<String, String> getHeaders(String token){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
