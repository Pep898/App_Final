package com.example.flypath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flypath.BD.Usuari.GetObtenirUsuari;
import java.sql.Connection;
import java.sql.DriverManager;

public class Login extends AppCompatActivity {
    EditText editUsername, editPassword;
    Button btnLogin,btnRegistrar;
    ProgressBar progressBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editUsername = (EditText) findViewById(R.id.editUsernameFollow);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        context=getApplicationContext();

        //entra a l'activtiy de loggejar
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =editUsername.getText().toString();
                String password=editPassword.getText().toString();
                    String[] parametres = {username,password};
                    new GetObtenirUsuari(context,progressBar).execute(parametres);
            }
        });
        // entra a l'activitat de registrar-se
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Login.this,Registrar.class));
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
