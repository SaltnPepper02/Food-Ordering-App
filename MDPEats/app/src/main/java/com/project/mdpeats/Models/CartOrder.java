package com.project.mdpeats.Models;

public class CartOrder {
    private String FoodID;
    private String FoodName;
    private String Quantity;
    private String Price;

    public CartOrder() {
    }

    public CartOrder(String foodID, String foodName, String quantity, String price) {
        FoodID = foodID;
        FoodName = foodName;
        Quantity = quantity;
        Price = price;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
