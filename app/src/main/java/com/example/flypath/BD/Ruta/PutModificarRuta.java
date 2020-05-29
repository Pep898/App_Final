package com.example.flypath.BD.Ruta;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.ui.tools.ToolsFragment;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PutModificarRuta extends AsyncTask<Object, Void, Integer> {
    String idRuta,nom,descripcio,info,estat;
    Context context;
    ProgressBar progressBar;

    public PutModificarRuta(String idRuta,String nom,String descripcio,String info,String estat,Context context, ProgressBar progressBar) {
        this.idRuta=idRuta;
        this.nom=nom;
        this.descripcio=descripcio;
        this.info=info;
        this.estat=estat;
        this.context = context.getApplicationContext();
        this.progressBar=progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(Object[] objects) {
        String urlString = "http://192.168.100.11:45455/modificarRuta";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(urlString);
        HttpResponse response;

        try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ID", idRuta));
                nameValuePairs.add(new BasicNameValuePair("Nom", nom));
                nameValuePairs.add(new BasicNameValuePair("Descripcio", descripcio));
                nameValuePairs.add(new BasicNameValuePair("Info_Ruta", info));
                nameValuePairs.add(new BasicNameValuePair("Estat", obtenirEstat(estat)));
                httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httpPut);

                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

                StringBuilder sb = new StringBuilder();
                if(!sb.equals("")) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public String obtenirEstat(String estat){
        String nEstat = "0";
        if(estat.equals("privat")){
            nEstat="1";
        }else if(estat.equals("public")){
            nEstat="2";
        }else if(estat.equals("compartit")){
            nEstat="3";
        }
        return nEstat;
    }

}
