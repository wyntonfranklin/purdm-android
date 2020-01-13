package com.purdm.app;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class JsonResponse {

    public static final String DATA_TYPE_OBJECT = "object";
    public static final String DATA_TYPE_ARRAY = "array";
    private String status = "";
    private String message = "";
    private JsonObject data = null;
    private JsonArray data2 = null;
    private String dataType = "object";

    public JsonResponse(){

    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public JsonResponse(JsonObject resp){
        this.loadJson(resp);
    }


    public void loadJson(JsonObject resp){
        try{
            JsonObject response = resp.getAsJsonObject(Constants.SERVER_RESPONSE_TAG);
            if(response.has("status")){
                this.status = response.get("status").getAsString();
            }
            if(response.has("message")){
                this.message = response.get("message").getAsString();
            }
            if(response.has("data")){
                if(this.dataType.equals(DATA_TYPE_OBJECT) ){
                    this.data = response.getAsJsonObject("data");
                }else if(this.dataType.equals(DATA_TYPE_ARRAY)){
                    this.data2 = response.getAsJsonArray("data");
                }
            }
        }catch (Exception e){
            Log.d("json repsonse error", e.getMessage());
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonObject getData() {
        return this.data;
    }

    public JsonArray getDataAsArray(){
        return this.data2;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public boolean hasData(){
        if(this.data != null){
            return true;
        }
        return false;
    }

    public String getDataKeyAsString(String key){
        if(this.hasData()){
            return this.getData().get(key).getAsString();
        }
        return "";
    }

    public boolean isGood(){
        if(this.status.equals("good")){
            return true;
        }
        return false;
    }

    public JsonArray getJsonArrayFromData(String tag){
        return this.getData().getAsJsonArray(tag);
    }
}
