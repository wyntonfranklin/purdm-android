package com.purdm.app;

public class Api {

    private String request="get";
    private String key = "";
    protected Settings settings;

    public Api(){

    }

    public Api(Settings set){
        this.settings = set;
    }

    public Settings getSettings(){
        return this.settings;
    }

    public String getBaseUrl(){
        String url = this.getSettings().getString("domain","http://demo.purdm.com");
        return url;
    }

    public String getApiPath(){
        if(this.request == "get"){
            return "/api/get?action=";
        }else{
            return "/api/post?action=";
        }
    }

    public String getFinalUrl(){
        String base = this.getBaseUrl();
        String path = this.getApiPath();
        return base + path;
    }

    public String getAction(String val){
        return this.getFinalUrl() + val + this.getKey();
    }

    private String getKey(){
        return  "&pdmkey=" + this.settings.getString("apikey","");
    }
}
