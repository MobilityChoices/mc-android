package org.mobilitychoices.entities;

import java.io.Serializable;

public class User implements Serializable{
    private String email;
    private String passwort; //TODO sollen ma es Passwort zum User dazu tua oder nur aufm Server speichern und do weglo?
    private String id;
    private String username;

    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
