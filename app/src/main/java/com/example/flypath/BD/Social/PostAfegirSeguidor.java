package com.example.flypath.BD.Social;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.flypath.Login;
import com.example.flypath.MainActivity;

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

public class PostAfegirSeguidor extends AsyncTask<Object, Void, Integer>  {
    Context context;
    JSONObject jsonObject= new JSONObject();

    public PostAfegirSeguidor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected Integer doInBackground(Object[] objects) {
        String urlString = "http://192.168.100.11:45455/afegirSeguidor";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urlString);

        try{
            //objecta que s'enviara amb les dades del POST
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Username", objects[0].toString()));
            nameValuePairs.add(new BasicNameValuePair("Seguidor", objects[1].toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            //obte code de la resposta de l'api
            final int statusCode = response.getStatusLine().getStatusCode();

                if(statusCode==200) {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");

                    StringBuilder sb = new StringBuilder();
                    if (!sb.equals("")) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        jsonObject = new JSONObject(sb.toString());
                    }
                    return statusCode;
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
}


