package com.example.flypath;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flypath.BD.Usuari.PostRegistreUsuari;


public class Registrar extends AppCompatActivity {
    EditText editEmail, editUsername, editPassword;
    Button btnRegistrar;
    ProgressBar progressBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        context=getApplicationContext();
        editEmail = (EditText) findViewById(R.id.editEmail);
        editUsername = (EditText) findViewById(R.id.editUsernameFollow);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String email=editEmail.getText().toString();
                String username =editUsername.getText().toString();
                String password=editPassword.getText().toString();
                if(!(email.equals("") && username.equals("") && password.equals(""))){
                    String[] parametres = {email, username, password};
                    new PostRegistreUsuari(context,progressBar).execute(parametres);
                }else {
                    Toast.makeText(getApplicationContext(),"Les dades no estan completes",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Versio no actualitzada!",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
