package com.example.flypath.ui.slideshow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flypath.BD.Ruta.DeleteRuta;
import com.example.flypath.BD.Social.DeleteSeguidor;
import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.Rutes.VisualitzarRutesSimples;
import com.example.flypath.ui.gallery.AdaptadorLlistaRutes;
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

public class FragLlistaSeguits extends Fragment {
    View vistaSeguits;
    Context context;
    ArrayList<ItemFollowers> listDatosSeguits;
    RecyclerView recyclerSeguits;
    Usuari usuariSeguits;
    Usuari usuariGlobal;

    public FragLlistaSeguits(Context context) {
        this.context=context.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // "infla/obte" el disseny d’aquest fragment
        vistaSeguits = inflater.inflate(R.layout.fragment_seguits, container, false);
        recyclerSeguits = (RecyclerView) vistaSeguits.findViewById(R.id.recycleIdFollow);
        recyclerSeguits.setLayoutManager(new LinearLayoutManager(getActivity()));

        listDatosSeguits = new ArrayList<ItemFollowers>();

        //Obte l'usuari de la MainActivty
        usuariSeguits=((MainActivity) getActivity()).getUsuari();

        new OmplirLlista(usuariSeguits).execute();


        return vistaSeguits;
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
            String urlString = "http://192.168.100.11:45455/seguits/"+usuari.ID;
            usuariGlobal=usuari;
            JSONArray jsonArraySeguits;
            JSONObject jsonObjectSeguits;
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

                    jsonArraySeguits = new JSONArray(sb.toString());
                    for (int i = 0; i < jsonArraySeguits.length(); i++) {
                        try {
                            jsonObjectSeguits = jsonArraySeguits.getJSONObject(i);
                            result.add(new ItemFollowers(jsonObjectSeguits.getInt("ID"),jsonObjectSeguits.getString("Username"),R.mipmap.icono_usuari));
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
        protected void onPostExecute(final ArrayList<ItemFollowers> itemFollowers) {
            super.onPostExecute(itemFollowers);

            AdaptadorLlistaFollowers adap = new AdaptadorLlistaFollowers(itemFollowers);

            final CharSequence[] opcions = {"Deixar de seguir", "Cancelar"};
            final AlertDialog.Builder alertOpcions = new AlertDialog.Builder(getActivity());
            alertOpcions.setTitle("Follow opcio");

            adap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int ID=itemFollowers.get(recyclerSeguits.getChildAdapterPosition(v)).getID();
                    final String userID=String.valueOf(ID);
                    alertOpcions.setItems(opcions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int opcio) {
                            if (opcions[opcio].equals("Deixar de seguir")) {
                                new DeleteSeguidor(userID,usuari.ID).execute();
                            }
                        }
                    });
                    alertOpcions.show();

                }
            });
            recyclerSeguits.setAdapter(adap);
        }
    }




}