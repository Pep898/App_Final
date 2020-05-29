package com.example.flypath;

public class Usuari {
    public int ID;
    public String Email;
    public String Username;
    public String Password;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public Usuari(int ID, String email, String username, String password) {
        this.ID = ID;
        Email = email;
        Username = username;
        Password = password;
    }
}
