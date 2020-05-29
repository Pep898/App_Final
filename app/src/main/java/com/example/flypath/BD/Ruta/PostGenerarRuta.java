package com.example.flypath.BD.Ruta;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
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

public class PostGenerarRuta extends AsyncTask {
    Context context;
    ProgressBar progressBar;
    JSONObject jsonObject= new JSONObject();

    public PostGenerarRuta(Context context, ProgressBar progressBar) {
        this.context = context.getApplicationContext();
        this.progressBar=progressBar;
    }
    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] strings) {
        String urlString = "http://192.168.100.11:45455/generarRuta";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urlString);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Nom", strings[0].toString()));
            nameValuePairs.add(new BasicNameValuePair("Descripcio", strings[1].toString()));
            nameValuePairs.add(new BasicNameValuePair("Creador", strings[2].toString()));
            nameValuePairs.add(new BasicNameValuePair("Info_Ruta", strings[3].toString()));
            nameValuePairs.add(new BasicNameValuePair("Estat", obtenirEstat(strings[4].toString())));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            StringBuilder sb = new StringBuilder();
            if(!sb.equals("")){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                jsonObject = new JSONObject(sb.toString());
                return jsonObject;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public String obtenirEstat(String estat){
        String nEstat = "0";
        if(estat.equals("privat")){
            nEstat="1";
        }else if(estat.equals("public")){
            nEstat="2";
        }else if(estat.equals("nomes amics")){
            nEstat="3";
        }
        return nEstat;
    }
}
