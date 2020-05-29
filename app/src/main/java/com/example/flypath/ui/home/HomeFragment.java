package com.example.flypath.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.Login;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.Registrar;
import com.example.flypath.ui.gallery.ItemLlista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    TextView textViewBenvinguda,textViewUsuari,numeroSeguidors,numeroSeguits,numeroRutesCompartides;
    View vista;
    Usuari user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista= inflater.inflate(R.layout.fragment_home, container, false);

        user=((MainActivity) getActivity()).getUsuari();
        textViewBenvinguda = (TextView) vista.findViewById(R.id.textViewBenvinguda);
        textViewUsuari = (TextView) vista.findViewById(R.id.textViewUsuari);
        numeroSeguidors = (TextView) vista.findViewById(R.id.numeroSeguidors);
        numeroSeguits = (TextView) vista.findViewById(R.id.numeroSeguits);
        numeroRutesCompartides = (TextView) vista.findViewById(R.id.numeroRutesCompartides);

        textViewBenvinguda.setText("Benvingut a FlyPath");
        textViewUsuari.setText(user.Username);

        new GetNumeroSeguidorsSeguits(user.ID).execute();





        return vista;
    }

    //obte el numero de  seguidors,seguits i les rutes compartides
    public class GetNumeroSeguidorsSeguits extends AsyncTask<Integer,Void,Void> {

        int userID;
        public GetNumeroSeguidorsSeguits(int id) {
            this.userID=id;
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Integer... idUser) {
            String urlString = "http://192.168.100.11:45455/numeroSeguidorsSeguitsRutesCompartides/"+userID;
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
                    String resposta=sb.toString();
                    String[] respostaArray=resposta.split(",");
                    //estableix a cada text el numero que li correspon
                    numeroSeguits.setText(respostaArray[0]);
                    numeroSeguidors.setText(respostaArray[1]);
                    numeroRutesCompartides.setText(respostaArray[2]);
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




}