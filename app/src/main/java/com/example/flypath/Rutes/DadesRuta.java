package com.example.flypath.Rutes;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flypath.BD.Ruta.PostGenerarRuta;
import com.example.flypath.BD.Usuari.PostRegistreUsuari;
import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.R;
import com.example.flypath.Rutes.RutesSimples;
import com.mapbox.geojson.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DadesRuta extends AppCompatActivity {

    EditText editNom, editDesripcio;
    Button btnGuardar;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView txtEstat;
    Context context;
    String estatSTR;
    String usuariSTR;
    Usuari usuari;
    JSONObject jsonObjUsuari;

    String rutaSTR;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dades_ruta);

        context=getApplicationContext();
        editNom = (EditText) findViewById(R.id.editNom);
        editDesripcio = (EditText) findViewById(R.id.editDescripcio);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        radioGroup = (RadioGroup) findViewById(R.id.grup);
        txtEstat = (TextView) findViewById(R.id.txtEstat);

        //obte el parametres de la ruta i del usuari
        rutaSTR=getIntent().getStringExtra("RutaSTR");
        usuariSTR=getIntent().getStringExtra("UsuariSTR");

        //transforma usuari a objecte usuari
        try {
            jsonObjUsuari= new JSONObject(usuariSTR);
            usuari=new Usuari(jsonObjUsuari.getInt("ID"),jsonObjUsuari.getString("Email"),
                    jsonObjUsuari.getString("Username"),jsonObjUsuari.getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obte els parametres que fica el usuari
                String nom=editNom.getText().toString();
                String descripcio=editDesripcio.getText().toString();
                String estat=txtEstat.getText().toString();
                try{
                    //fa la conexio a la base de dades amb els parametres que fica l'usuari per generar una ruta+
                    if(!(nom.equals("") && descripcio.equals("") && estat.equals(""))){
                        String[] parametres={nom,descripcio, String.valueOf(usuari.getID()),rutaSTR,estatSTR};
                        new PostGenerarRuta(context,progressBar).execute(parametres);
                    }else {
                        Toast.makeText(getApplicationContext(),"Les dades no son valides",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("errorPost", ex.getMessage());
                }
            }
        });
    }

    //comprova si l'estat ha estat canviat
    public void checkBtn(View v){
        int id = radioGroup.getCheckedRadioButtonId();

        radioButton= findViewById(id);
        estatSTR=radioButton.getText().toString();
        //Toast.makeText(this, "select: "+ radioButton.getText(),Toast.LENGTH_SHORT).show();
        txtEstat.setText("Estat com a "+estatSTR);
    }



}
