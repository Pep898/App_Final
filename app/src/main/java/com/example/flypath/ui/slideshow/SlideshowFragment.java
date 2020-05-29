package com.example.flypath.ui.slideshow;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.flypath.BD.Social.PostAfegirSeguidor;
import com.example.flypath.BD.Usuari.Usuari;
import com.example.flypath.MainActivity;
import com.example.flypath.R;
import com.example.flypath.ui.gallery.ViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class SlideshowFragment extends Fragment {

    EditText editUsernameFollow;
    Button btnAgregarFollow,btnRefrescarFollow;
    Usuari usuariFollow;
    View vistaFollow;

    private TabLayout tabLayoutFollow;
    private AppBarLayout appBarLayoutFollow;
    private ViewPager viewPagerFollow;
    private ViewPagerAdapter viewPagerAdapterFollow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vistaFollow = inflater.inflate(R.layout.fragment_slideshow, container, false);

        editUsernameFollow = (EditText) vistaFollow.findViewById(R.id.editUsernameFollow);
        btnAgregarFollow = (Button) vistaFollow.findViewById(R.id.btnAgregarFollow);
        btnRefrescarFollow = (Button) vistaFollow.findViewById(R.id.btnRefrescarFollow);
        //obte l'usuari de la MainActivty
        usuariFollow=((MainActivity) getActivity()).getUsuari();

        setUpView();
        setUpViewPagerAdapter();

        btnAgregarFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameASeguir =editUsernameFollow.getText().toString();
                new PostAfegirSeguidor(getActivity()).execute(usernameASeguir,usuariFollow.Username);
                setUpView();
                setUpViewPagerAdapter();
            }
        });
        btnRefrescarFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpView();
                setUpViewPagerAdapter();
            }
        });


        return vistaFollow;
    }


    public void refresch(){
        setUpView();
        setUpViewPagerAdapter();
    }
    //inicilitza els components de al vista de la paginacio
    private void setUpView(){
        tabLayoutFollow = vistaFollow.findViewById(R.id.tabLayoutFollow);
        appBarLayoutFollow = vistaFollow.findViewById(R.id.appBarLayoutFollow);
        viewPagerFollow = vistaFollow.findViewById(R.id.viewPagerFollow);
        viewPagerAdapterFollow = new ViewPagerAdapter(getFragmentManager());
    }
    //adaptador del paginador dels seguidors
    private void setUpViewPagerAdapter(){
        viewPagerAdapterFollow.addFragment(new FragLlistaSeguidors(),"Seguidors");
        viewPagerAdapterFollow.addFragment(new FragLlistaSeguits(getActivity()),"Seguits");
        viewPagerFollow.setAdapter(viewPagerAdapterFollow);


        tabLayoutFollow.setupWithViewPager(viewPagerFollow);
        tabLayoutFollow.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.i("TAG1", "Posicion: " + tabLayoutFollow.getSelectedTabPosition() + " Titulo: " + viewPagerAdapterFollow.getPageTitle(tabLayoutFollow.getSelectedTabPosition()));
                        break;
                    case 1:
                        Log.i("TAG1", "Posicion: " + tabLayoutFollow.getSelectedTabPosition() + " Titulo: " + viewPagerAdapterFollow.getPageTitle(tabLayoutFollow.getSelectedTabPosition()));
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
