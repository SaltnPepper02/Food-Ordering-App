package com.project.mdpeats.Models;

import java.util.List;

public class OrderRequest {
    private String studentID;
    private String foodStallID;
    private String name;
    private String total;

    private String status;
    private List<CartOrder> foods;

    public OrderRequest() {
    }

    public OrderRequest(String studentID,String foodStallID, String name, String total, List<CartOrder> foods) {
        this.studentID = studentID;
        this.foodStallID = foodStallID;
        this.name = name;
        this.total = total;
        this.foods = foods;
        this.status = "0";
    }

    public String getFoodStallID() {
        return foodStallID;
    }

    public void setFoodStallID(String foodStallID) {
        this.foodStallID = foodStallID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CartOrder> getFoods() {
        return foods;
    }

    public void setFoods(List<CartOrder> foods) {
        this.foods = foods;
    }
}
