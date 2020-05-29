package com.example.flypath.BD.Usuari;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.flypath.Login;
import com.example.flypath.MainActivity;
import com.example.flypath.ui.tools.ToolsFragment;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

public class PutCanviConfigUsuari extends AsyncTask<Object, Void, Integer>  {
    Context context;
    Usuari usuariOld, usuarioNew;
    boolean canviPassword;
    Gson gson= new Gson();

    public PutCanviConfigUsuari(Usuari usuariOld,Usuari usuarioNew,boolean canviPassword) {
        this.usuariOld=usuariOld;
        this.usuarioNew=usuarioNew;
        this.canviPassword=canviPassword;
    }

    @Override
    protected Integer doInBackground(Object[] objects) {
        String urlString = "http://192.168.100.11:45455/usuariCanvis";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(urlString);
        String ID=""+usuariOld.ID;
        HttpResponse response;
        try {
            if(canviPassword){
                //objecta que s'enviara amb les dades del POST
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ID", ID));
                nameValuePairs.add(new BasicNameValuePair("Email", usuarioNew.Email));
                nameValuePairs.add(new BasicNameValuePair("Username", usuarioNew.Username));
                nameValuePairs.add(new BasicNameValuePair("Password", usuarioNew.Password));
                httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httpPut);

            }else{
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ID", ID));
                nameValuePairs.add(new BasicNameValuePair("Email", usuarioNew.Email));
                nameValuePairs.add(new BasicNameValuePair("Username", usuarioNew.Username));
                httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httpPut);
            }
            final int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode)
            {
                case 200:
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
                        String json = gson.toJson(usuarioNew); //convert
                        try{
                            Intent myIntent = new Intent(context, ToolsFragment.class);
                            myIntent.putExtra("Usuari",json);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(myIntent);
                        }catch (Exception ex) {
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("errorPost", ex.getMessage());
                        }
                    }
                    break;
                default:
                    //Toast.makeText(context, "Les dades son incorrectas", Toast.LENGTH_SHORT).show();
                    return statusCode;
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

    @Override
    protected void onPostExecute(Integer code) {
        super.onPostExecute(code);
        switch (code)
        {
            case 500:
                Toast.makeText(context, "El servidor no esta disponible actualment", Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(context, "Ja existeix un usuari amb aquestes dades", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Toast.makeText(context, "El correo no es valid", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
