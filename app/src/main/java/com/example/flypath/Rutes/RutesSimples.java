package com.example.flypath.Rutes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class RutesSimples extends AppCompatActivity implements LocationListener,
        OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    Context context;
    private LocationManager locationManager;
    private Location location;

    private Button btnComençarRuta,btnGuardarRuta;
    private Point originPosition;
    private Point destionationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";
    private DirectionsRoute currentRoute ;
    final Double[] puntsRuta= new Double[10];

    InfoRutes infoRutes= new InfoRutes();
    String rutaSTR;
    String usuariSTR;

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

        setContentView(R.layout.rutes_simples);
        context=getApplicationContext();

        //Obte l'usuari que esta actuan, from GalleryFragment
        usuariSTR= getIntent().getStringExtra("Usuari");
        rutaSTR= getIntent().getStringExtra("Usuari");

        mapView = findViewById(R.id.mapView);
        btnComençarRuta = findViewById(R.id.btnComençarRuta);
        btnGuardarRuta = findViewById(R.id.btnGuardarRuta);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //comprova els permissos de ubicacio del dispositiu movil
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //obte la localitzacio de l'usuari
        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);

        //guarda la ruta i obra el Dades per inserix les dades de al ruta
        btnGuardarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoRutes.pInici[0]=location.getLatitude();
                infoRutes.pInici[1]=location.getLongitude();
                infoRutes.pFi[0]=puntsRuta[2];
                infoRutes.pFi[1]=puntsRuta[3];
                infoRutes.distancia=currentRoute.distance();
                infoRutes.duracio=currentRoute.duration();
            Gson gson = new Gson();
            String json = gson.toJson(infoRutes);
           rutaSTR=json;
            try{
                Intent myIntent = new Intent(context, DadesRuta.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("RutaSTR",rutaSTR);
                myIntent.putExtra("UsuariSTR",usuariSTR);
                //obre l'activitat on demana dades de la ruta per guardarles
                context.startActivity(myIntent);
            }catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("errorPost", ex.getMessage());
            }



            }
        });
        //Comença a fe la ruta que s'ha generat
        btnComençarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)//simula el moviment del usuari
                        .build();
                NavigationLauncher.startNavigation(RutesSimples.this,options);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        RutesSimples.this.mapboxMap = mapboxMap;
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
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    //insereix un marker i obte la LatLng de l'ubicació per generar una ruta
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        if (destinationMarker!=null){
            destinationMarker.remove();
        }else{
            puntsRuta[2]=point.getLatitude();
            puntsRuta[3]=point.getLongitude();
        }
        destinationMarker= mapboxMap.addMarker(new MarkerOptions().position(point));
        destionationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());

        originPosition=Point.fromLngLat( location.getLongitude(), location.getLatitude());
        getRoute(originPosition,destionationPosition);
        btnComençarRuta.setEnabled(true);
        btnGuardarRuta.setEnabled(true);
        btnComençarRuta.setBackgroundResource(R.color.mapbox_blue);
        btnGuardarRuta.setBackgroundResource(R.color.mapbox_blue);

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
                            navigationMapRoute= new NavigationMapRoute(null,mapView,mapboxMap);
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

