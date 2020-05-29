package com.example.flypath.BD.Social;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeleteSeguidor extends AsyncTask<Void,Void,Void> {
    int idSeguidor,idUnfollow;

    public DeleteSeguidor(String idUnfollow,int idSeguidor) {
        this.idUnfollow=Integer.parseInt(idUnfollow);
        this.idSeguidor=idSeguidor;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String urlString = "http://192.168.100.11:45455/deixarSeguir/"+idUnfollow+"/"+idSeguidor;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");

            StringBuilder sb = new StringBuilder();
            if(!sb.equals("")){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

}
