package com.example.flypath.ui.tools;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.flypath.BD.Usuari.PutCanviConfigUsuari;
import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.MainActivity;
import com.example.flypath.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ToolsFragment extends Fragment {

    View vista;
    String usuariSTR;
    Usuari usuariOld,usuariNew;
    JSONObject jsonObject;
    EditText editEmail, editUsername, editNouPassword,editRepetirNouPassword,editPasswordConfirmacio;
    Button btnGuardarCanvis,btnCanviPassword;
    LinearLayout linearLayPassChange;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista= inflater.inflate(R.layout.fragment_tools, container, false);
        //obte l'usuari del MainActivty
        usuariSTR=((MainActivity) getActivity()).getUsuariJSONcomSTR();
        //transforam l'usuari en object Usuari
        try {
            jsonObject= new JSONObject(usuariSTR);
            usuariOld=new Usuari(jsonObject.getInt("ID"),jsonObject.getString("Email"),
                    jsonObject.getString("Username"),jsonObject.getString("Password"));
            usuariNew=usuariOld;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editEmail= vista.findViewById(R.id.editEmail);
        editEmail.setText(usuariOld.Email);
        editUsername= vista.findViewById(R.id.editUsernameFollow);
        editUsername.setText(usuariOld.Username);
        editPasswordConfirmacio= vista.findViewById(R.id.editPasswordConfirmacio);
        editNouPassword= vista.findViewById(R.id.editNouPassword);
        editRepetirNouPassword= vista.findViewById(R.id.editRepetirNouPassword);
        btnGuardarCanvis= vista.findViewById(R.id.btnGuardarCanvis);
        btnCanviPassword= vista.findViewById(R.id.btnCanviPassword);
        linearLayPassChange= vista.findViewById(R.id.linearLayPassChange);

        //boto que activa la possibilitat de canviar el password
        btnCanviPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayPassChange.setVisibility(View.VISIBLE);
            }
        });
        // guarda els canvis de la configuracio
        btnGuardarCanvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPasswordConfirmacio.getText().toString().equals(usuariOld.getPassword())){
                    if(linearLayPassChange.getVisibility()==View.VISIBLE){
                        //Canvia la contrasenya
                        if(editNouPassword.getText().toString().equals(editRepetirNouPassword.getText().toString())){

                            usuariNew.Email=editEmail.getText().toString();
                            usuariNew.Username=editUsername.getText().toString();
                            usuariNew.Password=editNouPassword.getText().toString();
                            new PutCanviConfigUsuari(usuariOld,usuariNew,true).execute();
                        }else{
                            Toast.makeText(getActivity(), "Els nous passwords no concideixen", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //Guarda els canvis pero no canvia la contrasenya
                        usuariNew.Email=editEmail.getText().toString();
                        usuariNew.Username=editUsername.getText().toString();
                        new PutCanviConfigUsuari(usuariOld,usuariNew, false).execute();
                    }
                }else{
                    Toast.makeText(getActivity(), "Requereix inserir el password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return vista;
    }
}