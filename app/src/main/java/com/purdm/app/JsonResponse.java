package com.purdm.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class JsonResponse {

    private static final String DATA_TYPE_OBJECT = "object";
    private static final String DATA_TYPE_ARRAY = "array";
    private String status = "";
    private String message = "";
    private JsonObject data = null;
    private JsonArray data2 = null;
    private String dataType = "object";

    public JsonResponse(JsonObject resp){
        try{
            JsonObject response = resp.getAsJsonObject(Constants.SERVER_RESPONSE_TAG);
            if(response.has("status")){
                this.status = response.get("status").getAsString();
            }
            if(response.has("message")){
                this.message = response.get("message").getAsString();
            }
            if(response.has("data")){
                if(this.dataType == DATA_TYPE_OBJECT ){
                    this.data = response.getAsJsonObject("data");
                }else if(this.dataType == DATA_TYPE_ARRAY){
                    this.data2 = response.getAsJsonArray("data");
                }
            }
        }catch (Exception e){

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
}
