package org.acme.developer;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_developer")
public class Developer {
    @Id
    @GeneratedValue
    private Long id;
    private UUID apiKey;
    private String email;
    private String password;
    private String salt;

    

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UUID getApiKey() {
        return apiKey;
    }
    public void setApiKey(UUID apiKey) {
        this.apiKey = apiKey;
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
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    
}
