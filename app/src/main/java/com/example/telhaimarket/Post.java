package com.example.telhaimarket;

public class Post {
    private String description;
    private String title;
    private String ownerUid;
    private String price;
    private String keyNode;

    public Post(String description, String title, String price, String ownerUid) {
        this.description = description;
        this.title = title;
        this.price = price;
        this.ownerUid = ownerUid;
        this.keyNode = "";
    }
    public Post(){}

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public String getKeyNode() {
        return keyNode;
    }

    public void setKeyNode(String keyNode) {
        this.keyNode = keyNode;
    }
}
