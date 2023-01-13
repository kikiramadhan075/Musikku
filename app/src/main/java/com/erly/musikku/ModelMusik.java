package com.erly.musikku;

public class ModelMusik {
    private String NoId;
    private String Musik;
    private String key;

    public  ModelMusik(){

    }

    public ModelMusik(String noId, String musik) {
        NoId = noId;
        Musik = musik;
    }

    public String getNoId() {
        return NoId;
    }

    public void setNoId(String noId) {
        NoId = noId;
    }

    public String getMusik() {
        return Musik;
    }

    public void setMusik(String musik) {
        Musik = musik;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
