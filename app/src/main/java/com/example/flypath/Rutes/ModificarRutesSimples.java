package com.example.flypath.Rutes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.flypath.BD.Ruta.Ruta;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarRutesSimples extends AppCompatActivity implements LocationListener,
        OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;

    private MapView mapViewModificar;
    Context context;
    private LocationManager locationManager;
    private Location location;

    private Button btnComençarRutaModificar,btnGuardarRutaModificar;
    private Point originPosition;
    private Point destionationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";
    private DirectionsRoute currentRoute ;


    InfoRutes infoRutes= new InfoRutes();
    String infoRutaSTR;
    String rutaID;

    Ruta rutaFormat;
    public class InfoRutes{
        Double pInici[]=new Double[2];
        Double pFi[]=new Double[2];
        Double distancia;
        Double duracio;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicialitza el token de l'api de MapBox
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.modificar_rutes_simples);
        context=getApplicationContext();

        //obte la ID de la ruta seleccionada del intent
        rutaID= getIntent().getStringExtra("RutaID");


        mapViewModificar = findViewById(R.id.mapViewModificar);
        btnComençarRutaModificar = findViewById(R.id.btnComençarRutaModificar);
        btnGuardarRutaModificar = findViewById(R.id.btnGuardarRutaModificar);
        mapViewModificar.onCreate(savedInstanceState);
        mapViewModificar.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //executa el threead per obtenir la ruta seleccionada
        new ObtenirRuta().execute();

        //comprova els permissos de ubicacio del dispositiu movil
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //obte la localitzacio de l'usuari
        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);



        //guarda la ruta i obra el modificarDades per modificarne les dades ja existents
        btnGuardarRutaModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoRutes.distancia=currentRoute.distance();
                infoRutes.duracio=currentRoute.duration();
                Gson gson = new Gson();
                infoRutaSTR= gson.toJson(infoRutes);
                try{
                    //transforma totes les dades de la ruta per poder convertir-les en JSON i passar-les a la base de dades per fer la modificacio
                    Intent myIntentMod = new Intent(context, ModificarDadesRuta.class);

                    myIntentMod.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    String IDSTR=String.valueOf(rutaFormat.ID);
                    String NomSTR=rutaFormat.Nom;
                    String DescripcioSTR=rutaFormat.Descripcio;
                    String CreadorSTR=String.valueOf(rutaFormat.Creador);
                    String disSTR=String.valueOf(infoRutes.distancia);
                    String durSTR=String.valueOf(infoRutes.duracio);
                    String fiSTR1=String.valueOf(infoRutes.pFi[0]);
                    String fiSTr2=String.valueOf(infoRutes.pFi[1]);
                    String iniSTR1=String.valueOf(infoRutes.pInici[0]);
                    String iniciSTR2=String.valueOf(infoRutes.pInici[1]);
                    String novaInfoRuta="{\"distancia\":"+disSTR+",\"duracio\":"+durSTR+",\"pFi\":["+fiSTR1+","+fiSTr2+"],\"pInici\":["+iniSTR1+","+iniciSTR2+"]"+"}";
                    String EstatSTR=String.valueOf(rutaFormat.Estat);

                    myIntentMod.putExtra("RutaID",IDSTR);
                    myIntentMod.putExtra("RutaNom",NomSTR);
                    myIntentMod.putExtra("RutaDescripcio",DescripcioSTR);
                    myIntentMod.putExtra("RutaCreador",CreadorSTR);
                    myIntentMod.putExtra("RutaInfo",novaInfoRuta);
                    myIntentMod.putExtra("RutaEstat",EstatSTR);
                    //obre l'activitat on demana dades de la ruta per guardarles
                    context.startActivity(myIntentMod);
                }catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("errorPost", ex.getMessage());
                }

            }
        });
        //Comença a fe la ruta que s'ha generat
        btnComençarRutaModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)//simula el moviment del usuari
                        .build();
                NavigationLauncher.startNavigation(ModificarRutesSimples.this,options);
            }
        });


    }

    private class ObtenirRuta extends AsyncTask<Void,Void, JSONObject> {
        JSONObject jsonObjectRuta;
        JSONArray jsonArrayRuta;

        @Override
        protected JSONObject doInBackground(Void... idRuta) {
            String urlString = "http://192.168.100.11:45455/obtenirRuta/"+rutaID;

            try{
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
                    jsonObjectRuta= jsonArrayRuta.getJSONObject(0);
                    int rID=Integer.parseInt(jsonObjectRuta.getString("ID"));
                    String rNom=jsonObjectRuta.getString("Nom");
                    String rDescripcio=jsonObjectRuta.getString("Descripcio");
                    int rCreador=Integer.parseInt(jsonObjectRuta.getString("Creador"));
                    String rPunts_Ruta=jsonObjectRuta.getString("Info_Ruta");
                    int rEstat=Integer.parseInt(jsonObjectRuta.getString("Estat"));

                    rutaFormat= new Ruta(rID,rNom,rDescripcio,rCreador,rPunts_Ruta,rEstat);
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
            return jsonObjectRuta;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            String info="";
            try {
                //obte les dades del JSON
               info=jsonObject.getString("Info_Ruta");
               String[] dades=info.split(",");

                //obte les dades dinici i fi amb les seves latitud i longitud

                String dist1=dades[0];
                String[] dist=dist1.split(":");
                String distanciaSTR=dist[1];
                Double distancia= Double.parseDouble(distanciaSTR);

                String dur1=dades[1];
                String[] dur=dur1.split(":");
                String duracioSTR=dur[1];
                Double duracio= Double.parseDouble(duracioSTR);

                Double[] Fi= new Double[2];
                String pFi1=dades[2];
                String[] pFi2=pFi1.split("\\[");
                String pFi3=dades[3].substring(0,dades[3].length()-1);
                Fi[0]=Double.parseDouble(pFi2[1]);
                Fi[1]=Double.parseDouble(pFi3);

                Double[] Inici= new Double[2];
                String pInici1=dades[4];
                String[] pInici2=pInici1.split("\\[");
                String pInici3=dades[5].substring(0,dades[5].length()-2);

                Inici[0]=Double.parseDouble(pInici2[1]);
                Inici[1]=Double.parseDouble(pInici3);

                infoRutes.pInici=Inici;
                infoRutes.pFi=Fi;
                infoRutes.distancia=distancia;
                infoRutes.duracio=duracio;

                //crea la ruta amb les dadesd ela ruta que s'ha carregat
                Point iniciCarregat=Point.fromLngLat(infoRutes.pInici[1], infoRutes.pInici[0]);
                Point fiCarregat=Point.fromLngLat(infoRutes.pFi[1], infoRutes.pFi[0]);
                getRoute(iniciCarregat,fiCarregat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        ModificarRutesSimples.this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Comprova si els permisos estan habilitats i si no ho sol·liciteu
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Obtenir una instancia del component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activar les opciones
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Permet fer visible el component
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.cancelZoomWhileTrackingAnimation();

            // definex el mode de càmera del component
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Defineix el mode de renderització del component
            locationComponent.setRenderMode(RenderMode.NORMAL);
            setCameraPosition(location);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    private void setCameraPosition(Location location){
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),13 ));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "user_location_permission_not_granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapViewModificar.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapViewModificar.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewModificar.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapViewModificar.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapViewModificar.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewModificar.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewModificar.onLowMemory();
    }

    //insereix un marker i obte la LatLng de l'ubicació per generar una ruta
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        if (destinationMarker!=null){
            destinationMarker.remove();
        }
        destinationMarker= mapboxMap.addMarker(new MarkerOptions().position(point));
        destionationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition=Point.fromLngLat( location.getLongitude(), location.getLatitude());

        infoRutes.pFi[0]=point.getLatitude();
        infoRutes.pFi[1]=point.getLongitude();


        infoRutes.pInici[0]= location.getLatitude();
        infoRutes.pInici[1]=location.getLongitude();


        getRoute(originPosition,destionationPosition);


        return false;
    }

    //obte l'inici i fi de la ruta i la genera en el mapa per visualitzar-la
    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(TAG,"no routes found , check right user and access token");
                            return;
                        }else if(response.body().routes().size()==0){
                            Log.e(TAG,"No routes found");
                            return;
                        }
                        currentRoute = response.body().routes().get(0);

                        if(navigationMapRoute!=null){
                            navigationMapRoute.removeRoute();
                        }else {
                            navigationMapRoute= new NavigationMapRoute(null,mapViewModificar,mapboxMap);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"Error: "+t.getMessage());
                    }
                });
    }


    @Override
    public void onLocationChanged(Location location) {
        double longitud=location.getLongitude();
        double latitud=location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

