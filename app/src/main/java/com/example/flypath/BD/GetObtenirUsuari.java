package com.example.flypath.BD;

import android.os.AsyncTask;

import com.example.flypath.Usuari;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetObtenirUsuari extends AsyncTask<Object,Void, Object> {
    JSONObject jsonObject= new JSONObject();
    Usuari usuari;
    boolean trobat=true;
    @Override
    protected Object doInBackground(Object[] objects) {
        String urlString = "http://192.168.100.11:45455/usuariLogin/"+objects[0].toString()+"/"+objects[1].toString();
        try {
            StringBuilder resultat = new StringBuilder();
            HttpGet httpGet = new HttpGet(urlString);

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            jsonObject = new JSONObject(sb.toString());
            //String e=json.getString("Username");
            return jsonObject;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
            errorConsulta();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void errorConsulta(){
        trobat= false;
    }
    public boolean getTrobat(){
        return trobat;
    }
    public Usuari obtenirJsonUser(){

        try {
            usuari= new Usuari(jsonObject.getInt("ID"),jsonObject.getString("Email"),jsonObject.getString("Username"),jsonObject.getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usuari;
    }

}
