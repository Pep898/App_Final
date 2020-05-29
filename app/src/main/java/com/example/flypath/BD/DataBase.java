package com.example.flypath.BD;

import android.os.AsyncTask;
import org.json.JSONObject;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class DataBase extends AsyncTask<Void, Void, String> {

    private String linkAPI="http://192.168.100.11:45455/";

    @Override
    protected String doInBackground(Void... voids) {
        URL urlAPI = null;
        try {
            urlAPI = new URL(linkAPI.toString());
            HttpURLConnection myConnection =(HttpURLConnection) urlAPI.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("error: ",e.getMessage());
        }catch (IOException e) {
            e.printStackTrace();
            Log.i("error: ",e.getMessage());
        }
        return null;
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }


}
