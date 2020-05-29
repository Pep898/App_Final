package com.example.flypath.ui.slideshow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.ui.gallery.FragLlistaPubliques;
import com.example.flypath.ui.gallery.ItemLlista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FragLlistaSeguidors extends Fragment {
    View vistaSeguidors;
    Context context;
    RecyclerView recyclerSeguidors;
    Usuari usuariSeguidors;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // "infla/obte" el disseny d’aquest fragment
        vistaSeguidors = inflater.inflate(R.layout.fragment_seguidors, container, false);
        context=getActivity();
        recyclerSeguidors= (RecyclerView) vistaSeguidors.findViewById(R.id.recycleIdFollow);
        recyclerSeguidors.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Obte l'usuari de la MainActivty
        usuariSeguidors=((MainActivity) getActivity()).getUsuari();

        new OmplirLlista(usuariSeguidors).execute();
        return vistaSeguidors;
    }


    //Obte els elements de la llista que ha d'omplir i omple la llista
    private class OmplirLlista extends AsyncTask<Void,Void, ArrayList<ItemFollowers>> {

        Usuari usuari;

        public OmplirLlista(Usuari usuari) {
            this.usuari=usuari;
        }

        @Override
        protected ArrayList<ItemFollowers> doInBackground(Void... voids) {
            ArrayList<ItemFollowers> result = new ArrayList<ItemFollowers>();
            String urlString = "http://192.168.100.11:45455/seguidors/"+usuari.ID;
            JSONArray jsonArraySeguidors;
            JSONObject jsonObjectSeguidors;
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                if (!sb.equals("")) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    jsonArraySeguidors = new JSONArray(sb.toString());
                    for (int i = 0; i < jsonArraySeguidors.length(); i++) {
                        try {
                            jsonObjectSeguidors = jsonArraySeguidors.getJSONObject(i);
                            //omple linea a linea de la resposta de l'API
                            result.add(new ItemFollowers(jsonObjectSeguidors.getInt("ID"),jsonObjectSeguidors.getString("Username"),R.mipmap.icono_usuari));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemFollowers> itemFollowers) {
            super.onPostExecute(itemFollowers);
            //inicilaitzar amb l'adaptador amb al llista compelta
            AdaptadorLlistaFollowers adap= new AdaptadorLlistaFollowers(itemFollowers);
            recyclerSeguidors.setAdapter(adap);
        }
    }
}
