package com.example.telhaimarket;

public class Post {
    private String description;
    private String title;
    private String ownerUid;
    private Number price;

    public Post(String description, String title, Number price, String ownerUid) {
        this.description = description;
        this.title = title;
        this.price = price;
        this.ownerUid = ownerUid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getOwnerUid() {
        return ownerUid;
    }
}
