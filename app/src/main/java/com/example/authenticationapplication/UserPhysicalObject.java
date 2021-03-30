package com.example.authenticationapplication;

public class UserPhysicalObject {
    String weight, height;

    public UserPhysicalObject(String weight, String height) {
        this.weight = weight;
        this.height = height;
    }
    public UserPhysicalObject(){

    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
