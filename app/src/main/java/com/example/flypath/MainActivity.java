package com.example.flypath;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flypath.BD.Usuari.Usuari;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public String usuariSTR;
    public JSONObject usuariJSON;
    public Usuari usuari;
    private TextView txtUsername,txtEmail;
    private View vista;
    private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //reb usuari del login
        usuariSTR= getIntent().getStringExtra("Usuari");
        try {
            //transfora usuari STR a Usuari objecta
            usuariJSON = new JSONObject(usuariSTR);
            usuari=obtenirUsuariToJSON(usuariJSON);
        }catch (JSONException err){
            Log.d("Error: ", err.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //afegeix elements a la barra d’acció si està present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public Usuari obtenirUsuariToJSON(JSONObject jsonObject){
        try {
            usuari= new Usuari(jsonObject.getInt("ID"),jsonObject.getString("Email"),
                    jsonObject.getString("Username"),jsonObject.getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(usuari==null){
            Log.i("ProvaUser","es null");
        }
        return usuari;
    }

    public Usuari getUsuari(){
        return  usuari;
    }
    public String getUsuariJSONcomSTR(){
        return usuariSTR;
    }

    //comporva si l'accio cridada es de desconnectar
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String a= (String) item.getTitle();

        if(a==null){
            return super.onOptionsItemSelected(item);

        }else if(a.equals("Desconnectar")){
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;
        }else{
            return super.onOptionsItemSelected(item);

        }

        }


}
