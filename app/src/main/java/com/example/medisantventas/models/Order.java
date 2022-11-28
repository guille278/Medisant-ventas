package com.example.medisantventas.models;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.medisantventas.config.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class Order {

    public static final int RECIVED = 1;
    public static final int SHIPPED = 2;
    public static final int DELIVERED = 3;

    private int id;
    private int status;
    private double total;
    private String delivered;
    private String cancelled;
    private int userId;
    private String createdAt;
    private String updatedAt;
    //private List<Order> orders;


    public Order() {
    }

    public Order(int id, int status, double total, String createdAt) {
        this.id = id;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public JsonArrayRequest getOrders(Response.Listener listener, Response.ErrorListener error, int status, String token) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Config.URL + "api/admin/orders?status=" + status,
                null,
                listener,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }

    public JsonObjectRequest getOrder(Response.Listener listener, Response.ErrorListener error, String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Config.URL + "api/admin/orders/" + this.getId(),
                null,
                listener,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }
    public JsonObjectRequest updateOrder(Response.Listener listener, Response.ErrorListener error, String token, JSONObject data) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                Config.URL + "api/admin/orders/" + this.getId(),
                data,
                listener,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }

    public JsonObjectRequest deleteOrder(Response.Listener listener, Response.ErrorListener error, String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                Config.URL + "api/admin/orders/" + this.getId(),
                null,
                listener,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }
}
