package com.project.mdpeats.Models;

public class FoodStall {
    private String Name;
    private String Image;

    public FoodStall() {
    }

    public FoodStall(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
