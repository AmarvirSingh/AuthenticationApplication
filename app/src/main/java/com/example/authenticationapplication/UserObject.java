package com.example.authenticationapplication;

public class UserObject {
    private String name, Age, email, movie;

    public UserObject(String name, String age, String email) {
        this.name = name;
        Age = age;
        this.email = email;
    }

    public UserObject(String name, String age, String email, String movie) {
        this.name = name;
        Age = age;
        this.email = email;
        this.movie = movie;
    }


    public UserObject(String movie){
        this.movie = movie;
    }

    public UserObject(){

    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
