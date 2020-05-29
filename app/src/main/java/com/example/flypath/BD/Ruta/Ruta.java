package com.example.flypath.BD.Ruta;

public class Ruta {
    public int ID;
    public String Nom;
    public String Descripcio;
    public int Creador;
    public String Punts_Ruta;
    public int Estat;



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getCreador() {
        return Creador;
    }

    public void setCreador(int creador) {
        Creador = creador;
    }

    public String getPunts_Ruta() {
        return Punts_Ruta;
    }

    public void setPunts_Ruta(String punts_Ruta) {
        Punts_Ruta = punts_Ruta;
    }

    public int getEstat() {
        return Estat;
    }

    public void setEstat(int Estat) {
        Estat = Estat;
    }

    public Ruta(int ID, String nom, String descripcio, int creador, String punts_Ruta, int estat) {
        this.ID = ID;
        Nom = nom;
        Descripcio = descripcio;
        Creador = creador;
        Punts_Ruta = punts_Ruta;
        Estat = estat;
    }
}
