package com.example.flypath.ui.slideshow;

//item de la llista de seguidors
public class ItemFollowers {

    public int ID;
    public int imatge;
    public String Username;

    public ItemFollowers(int ID, String username,int imatge) {
        this.ID = ID;
        this.Username = username;
        this.imatge = imatge;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getImatge() {
        return imatge;
    }

    public void setImatge(int imatge) {
        this.imatge = imatge;
    }
}
