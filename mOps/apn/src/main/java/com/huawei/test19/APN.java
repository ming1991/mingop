package com.huawei.test19;

public class APN {
    private String id;

    private String apn;

    private String type;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getApn() {
        return apn;
    }


    public void setApn(String apn) {
        this.apn = apn;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "APN{" +
                "id='" + id + '\'' +
                ", apn='" + apn + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
