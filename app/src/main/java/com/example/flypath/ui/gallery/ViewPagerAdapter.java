package com.example.flypath.ui.gallery;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsRutes = new ArrayList<>();
    private List<String> fragmentsTitlesRutes = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsRutes.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsRutes.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return fragmentsTitlesRutes.get(position);
    }

    //afegeix esl fragments i els seus tituls
    public void addFragment(Fragment fragment, String title){
        fragmentsRutes.add(fragment);
        fragmentsTitlesRutes.add(title);
    }
}
