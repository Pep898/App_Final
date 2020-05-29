package com.example.flypath.BD.Usuari;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;

public class Usuari {
    public int ID;
    public String Email;
    public String Username;
    public String Password;

    public Usuari(int ID, String email, String username, String password) {
        this.ID = ID;
        this.Email = email;
        this.Username = username;
        this.Password = password;
    }







    public int getID() {
        return ID;
    }

    public void setID(int ID) {

        this.ID = ID;
    }

    public String getEmail()
    {
        return Email;
    }

    public String getEmailfromJSON(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("Email");
    }

    public void setEmail(String email) {

        Email = email;
    }

    public String getUsername() {

        return Username;
    }

    public String getUsernamefromJSON(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("Username");
    }

    public void setUsername(String username) {

        Username = username;
    }

    public String getPassword()
    {
        return Password;
    }

    public String getPasswordfromJSON(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("Password");
    }

    public void setPassword(String password) {
        Password = password;
    }



}
