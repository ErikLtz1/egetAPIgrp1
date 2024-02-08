package org.acme.post;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private UUID postUserUUid;
    private String imgUrl;
    private String name;
    private double price;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UUID getPostUserUUid() {
        return postUserUUid;
    }
    public void setPostUserUUid(UUID postUserUUid) {
        this.postUserUUid = postUserUUid;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    
}
