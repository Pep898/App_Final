package com.example.flypath.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.Rutes.VisualitzarRutesSimples;
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

public class FragLlistaPubliques extends Fragment {
    private View vistaPubliques;
    RecyclerView recyclerPubliques;
    Context contextPubliques;
    Usuari usuari;

    public FragLlistaPubliques() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // "infla/obte" el disseny d’aquest fragment
        vistaPubliques = inflater.inflate(R.layout.fragment_llista_publiques, container, false);
        contextPubliques=getActivity();
        recyclerPubliques= (RecyclerView) vistaPubliques.findViewById(R.id.recycleIdLlista);
        recyclerPubliques.setLayoutManager(new LinearLayoutManager(getActivity()));

        //obte l'usuari del MainActivty
        usuari=((MainActivity) getActivity()).getUsuari();

        //executa el thread que fara al conexio per obtenir les dades i omplir la llista
        new OmplirLlista().execute();
        return vistaPubliques;
    }

    private class OmplirLlista extends AsyncTask<Void,Void, ArrayList<ItemLlista>> {

        @Override
        protected ArrayList<ItemLlista> doInBackground(Void... voids) {
            ArrayList<ItemLlista> resultPubliques = new ArrayList<ItemLlista>();
            String urlString = "http://192.168.100.11:45455/obtenirRutesPubliques";
            JSONArray jsonArrayRuta;
            JSONObject jsonObjectRuta;

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
                    jsonArrayRuta = new JSONArray(sb.toString());
                    for (int i = 0; i < jsonArrayRuta.length(); i++) {
                        try {
                            jsonObjectRuta = jsonArrayRuta.getJSONObject(i);
                            //afegeix linea per linea
                            resultPubliques.add(new ItemLlista(jsonObjectRuta.getInt("ID"),jsonObjectRuta.getString("Nom"),
                                    jsonObjectRuta.getString("Descripcio"),R.mipmap.icono_ruta,jsonObjectRuta.getString("CreadorUsername")));
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
            return resultPubliques;
        }

        @Override
        protected void onPostExecute(final ArrayList<ItemLlista> itemLlistas) {
            super.onPostExecute(itemLlistas);

            //crea un adaptador per els items de la llista
            AdaptadorLlistaRutes adap = new AdaptadorLlistaRutes(itemLlistas);
            //crea dos opcions 1.Per fer la ruta 2.Per cancelar-lo
            final CharSequence[] opcions = {"Entrar ruta", "Cancelar"};
            final AlertDialog.Builder alertOpcions = new AlertDialog.Builder(getActivity());
            alertOpcions.setTitle("Opcions ruta");
            //detecta quina opcio s'ha seleccionat
            adap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int ID=itemLlistas.get(recyclerPubliques.getChildAdapterPosition(v)).getId();
                    final String RutaID=String.valueOf(ID);
                    alertOpcions.setItems(opcions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int opcio) {
                            if (opcions[opcio].equals("Entrar ruta")) {
                                Intent intent = new Intent(getActivity(), VisualitzarRutesSimples.class);
                                intent.putExtra("RutaID", RutaID);
                                startActivity(intent);
                            }
                        }
                    });
                    alertOpcions.show();

                }
            });
            recyclerPubliques.setAdapter(adap);
        }
    }

}

