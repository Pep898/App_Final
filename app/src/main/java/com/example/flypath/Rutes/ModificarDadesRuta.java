package com.example.flypath.Rutes;

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
import com.example.flypath.BD.Ruta.PutModificarRuta;
import com.example.flypath.BD.Ruta.Ruta;
import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ModificarDadesRuta extends AppCompatActivity {

    EditText editNomModificar, editDesripcioModificar;
    Button btnGuardarModificar;
    ProgressBar progressBarModificar;
    RadioGroup radioGroupModificar;
    RadioButton radioButtonModificar,btnPrivatModificar,btnPublicModificar,btnAmicsModificar;
    TextView txtEstatModificar;
    Context context;
    String estatSTRX;
    Ruta ruta;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_dades_ruta);

        context=getApplicationContext();
        editNomModificar = (EditText) findViewById(R.id.editNomModificar);
        editDesripcioModificar = (EditText) findViewById(R.id.editDescripcioModificar);
        btnGuardarModificar = (Button) findViewById(R.id.btnGuardarModificar);
        progressBarModificar = (ProgressBar) findViewById(R.id.progressBarModificar);
        radioGroupModificar = (RadioGroup) findViewById(R.id.grupModificar);
        txtEstatModificar = (TextView) findViewById(R.id.txtEstatModificar);
        btnPrivatModificar = (RadioButton) findViewById(R.id.btnPrivatModificar);
        btnPublicModificar = (RadioButton) findViewById(R.id.btnPublicModificar);
        btnAmicsModificar = (RadioButton) findViewById(R.id.btnAmicsModificar);

        //obte els parametres de al ruta a traves de l'intent i els transforma a un objecta ruta
        final String IDSTR=getIntent().getStringExtra("RutaID");
        String Nom=getIntent().getStringExtra("RutaNom");
        String Descripcio=getIntent().getStringExtra("RutaDescripcio");
        String CreadorSTR=getIntent().getStringExtra("RutaCreador");
        final String Info=getIntent().getStringExtra("RutaInfo");
        final String EstatSTR=getIntent().getStringExtra("RutaEstat");
        ruta= new Ruta(Integer.parseInt(IDSTR),Nom,Descripcio,Integer.parseInt(CreadorSTR),Info,Integer.parseInt(EstatSTR));

        //estableix els edit amb les dades rebudes aixi al carrera la class les dadesd e al ruta son les que es mostraran
        editNomModificar.setText(Nom);
        editDesripcioModificar.setText(Descripcio);
        if(EstatSTR.equals("1")){
            btnPrivatModificar.setChecked(true);
            txtEstatModificar.setText("Estat com a privat");
        }else if(EstatSTR.equals("2")){
            btnPublicModificar.setChecked(true);
            txtEstatModificar.setText("Estat com a public");
        }else if(EstatSTR.equals("3")){
            btnAmicsModificar.setChecked(true);
            txtEstatModificar.setText("Estat com a compartit");
        }

       btnGuardarModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obte les dades dels edit siguin les mateixes o modificacades
                String nom=editNomModificar.getText().toString();
                String descripcio=editDesripcioModificar.getText().toString();
                String estat=txtEstatModificar.getText().toString();
                try{
                    //fa la conexio a l'api amb els parametres i modifica linformacio de la ruta
                    if(!(nom.equals("") && descripcio.equals("") && estat.equals(""))){
                     new PutModificarRuta(String.valueOf(ruta.ID),nom,descripcio,Info,estatSTRX,context,progressBarModificar).execute();
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

    //comproa si el estat a set modificat
    public void checkBtnModificar(View v){
        int id = radioGroupModificar.getCheckedRadioButtonId();

        radioButtonModificar= findViewById(id);
        estatSTRX=radioButtonModificar.getText().toString();
        txtEstatModificar.setText("Estat com a "+estatSTRX);
    }



}
