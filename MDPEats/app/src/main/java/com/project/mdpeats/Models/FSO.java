package com.project.mdpeats.Models;

public class FSO {
    private String email;
    private String password;
    private String foodstall;

    private String name;
    public FSO() {
    }

    public FSO(String email, String password, String foodstall, String name) {
        this.email = email;
        this.password = password;
        this.foodstall = foodstall;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoodstall() {
        return foodstall;
    }

    public void setFoodstall(String foodstall) {
        this.foodstall = foodstall;
    }
}
