package com.example.flypath.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.Rutes.RutesSimples;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class GalleryFragment extends Fragment {

    private Button btnCrearSimpleRutes,btnRefrescar;
    private View vistaRutes;
    String usuariSTRRutes;
    private TabLayout tabLayoutRutes;
    private AppBarLayout appBarLayoutRutaRutes;
    private ViewPager viewPagerRutes;
    private ViewPagerAdapter viewPagerAdapterRutes;
    private Object GalleryFragment;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        vistaRutes = inflater.inflate(R.layout.fragment_gallery, container, false);

        btnCrearSimpleRutes = (Button) vistaRutes.findViewById(R.id.btnCrearSimpleRuta);
        btnRefrescar = (Button) vistaRutes.findViewById(R.id.btnRefrescar);

        setUpView();
        setUpViewPagerAdapter();
        //obte l'usuari del MainActivty
        usuariSTRRutes=((MainActivity) getActivity()).getUsuariJSONcomSTR();

        //obre l'activitat de RutesSimples, per crear una ruta
        btnCrearSimpleRutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RutesSimples.class);
                intent.putExtra("Usuari",usuariSTRRutes);//pasa el usuari en format STR from MainActivty
                startActivity(intent);
            }
        });
        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpView();
                setUpViewPagerAdapter();
            }
        });
        return vistaRutes;
    }
    //inicialitza els elements de al vista de les pagines
    private void setUpView(){
        tabLayoutRutes = vistaRutes.findViewById(R.id.tabLayoutRuta);
        appBarLayoutRutaRutes = vistaRutes.findViewById(R.id.appBarLayoutRuta);
        viewPagerRutes = vistaRutes.findViewById(R.id.viewPagerRuta);
        viewPagerAdapterRutes = new ViewPagerAdapter(getFragmentManager());
    }

    //crea l'adaptador de les pagines de les rutes
    private void setUpViewPagerAdapter(){
        viewPagerAdapterRutes.addFragment(new FragLlistaPrivades(),"rutes privades");
        viewPagerAdapterRutes.addFragment(new FragLlistaPubliques(),"rutes publiques");
        viewPagerAdapterRutes.addFragment(new FragLlistaAmistats(),"rutes compartides");
        viewPagerRutes.setAdapter(viewPagerAdapterRutes);

        tabLayoutRutes.setupWithViewPager(viewPagerRutes);
        tabLayoutRutes.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.i("TAG1", "Posicion: " + tabLayoutRutes.getSelectedTabPosition() + " Titulo: " + viewPagerAdapterRutes.getPageTitle(tabLayoutRutes.getSelectedTabPosition()));
                        break;
                    case 1:
                        Log.i("TAG1", "Posicion: " + tabLayoutRutes.getSelectedTabPosition() + " Titulo: " + viewPagerAdapterRutes.getPageTitle(tabLayoutRutes.getSelectedTabPosition()));
                        break;
                    case 2:
                        Log.i("TAG1", "Posicion: " + tabLayoutRutes.getSelectedTabPosition() + " Titulo: " + viewPagerAdapterRutes.getPageTitle(tabLayoutRutes.getSelectedTabPosition()));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}