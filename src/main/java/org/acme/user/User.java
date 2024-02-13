package org.acme.user;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private UUID userUUID;
    @NotEmpty(message = "Du måste ange ett användarnamn.")
    private String username;
    @NotEmpty(message = "Du måste ange ett lösenord.")
    private String password;

    public UUID getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    
}
