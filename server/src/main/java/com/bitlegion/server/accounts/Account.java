package com.bitlegion.server.accounts;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.bitlegion.server.uploads.Upload;
import com.fasterxml.jackson.annotation.JsonIgnore;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.HashData;

@Entity // This tells Hibernate to make a table out of this class
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    private String bio;

    @Column(nullable = false)
    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        HashData s = BCrypt.withDefaults().hashRaw(6, "abcdabcdabcdabcd".getBytes(),
                password.getBytes(StandardCharsets.UTF_8));
        System.out.println(s);
        try {
            this.password = PasswordHash.createHash(password).toString();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyPassword(String password) {
        try {
            return PasswordHash.validatePassword(password, this.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Collection<Upload> files;

    public Integer getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Collection<Upload> getFiles() {
        return this.files;
    }

    public void setFiles(Collection<Upload> fileModels) {
        this.files = fileModels;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", email='" + getEmail() + "'" + ", bio='"
                + getBio() + "'" + ", fileModels='" + getFiles() + "'" + "}";
    }

}
