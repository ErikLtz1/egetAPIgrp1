package org.acme.post;

import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "t_post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private UUID postUserUUID;
    @NotNull
    private String imgUrl;
    @NotEmpty(message = "Du måste ange ett namn.")
    private String name;
    @Min(value = 1, message = "Value must be greater than or equal to 1")
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPostUserUUID() {
        return postUserUUID;
    }

    public void setPostUserUUID(UUID postUserUUID) {
        this.postUserUUID = postUserUUID;
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
