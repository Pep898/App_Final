package com.example.flypath.ui.gallery;

//item de la llista de les pagines de rutes
public class ItemLlista {

    public int id;
    public String Nom;
    public String Descripcio;
    public int imatge;
    public String creador;



    public ItemLlista(int id, String nom, String descripcio, int imatge, String creador) {
        this.id=id;
        this.Nom = nom;
        this.Descripcio = descripcio;
        this.imatge = imatge;
        this.creador = creador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getDescripcio() {
        return Descripcio;
    }

    public void setDescripcio(String descripcio) {
        Descripcio = descripcio;
    }

    public int getImatge() {
        return imatge;
    }

    public void setImatge(int imatge) {
        this.imatge = imatge;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }
}
