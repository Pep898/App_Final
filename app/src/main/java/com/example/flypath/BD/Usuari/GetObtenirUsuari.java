package com.example.flypath.BD.Usuari;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.Login;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.Rutes.RutesSimples;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetObtenirUsuari extends AsyncTask<Object,Void, Object> {
    JSONObject jsonObject= new JSONObject();
    Context context;
    ProgressBar progressBar;

    public GetObtenirUsuari(Context context, ProgressBar progressBar) {
        this.context = context.getApplicationContext();
        this.progressBar=progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String urlString = "http://192.168.100.11:45455/usuariLogin/"+objects[0].toString()+"/"+objects[1].toString();
        try {
            //StringBuilder resultat = new StringBuilder();
            //HttpGet httpGet = new HttpGet(urlString);

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

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
        if (o != null) {
            try{
                Intent myIntent = new Intent(context, MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("Usuari", jsonObject.toString());//envia l'usuari en forma STR al MAIN
                context.startActivity(myIntent);
            }catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("errorPost", ex.getMessage());
            }
        }else{
            Toast.makeText(context, "Les dades no son correctes", Toast.LENGTH_SHORT).show();
        }
    }
}
