package com.example.flypath.BD;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetAllUsuaris extends AsyncTask<Void,Void,Object> {
    @Override
    protected Object doInBackground(Void... voids) {
        String urlString = "http://192.168.100.11:45455/usuaris";
        try {
            StringBuilder resultat = new StringBuilder();

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String linea;

            while ((linea = rd.readLine()) != null) {
                resultat.append(linea);
            }
            rd.close();

            return resultat.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
