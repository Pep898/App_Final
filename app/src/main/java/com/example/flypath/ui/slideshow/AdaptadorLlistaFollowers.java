package com.example.flypath.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flypath.R;

import java.util.ArrayList;

public class AdaptadorLlistaFollowers extends RecyclerView.Adapter<AdaptadorLlistaFollowers.ViewHolderDatos> implements View.OnClickListener{


    ArrayList<ItemFollowers> listDatosFollow ;
    View.OnClickListener listenerFollow;

    public AdaptadorLlistaFollowers(ArrayList<ItemFollowers> listDatos) {
        this.listDatosFollow = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_followers,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.username.setText(listDatosFollow.get(position).getUsername());
        holder.foto.setImageResource(listDatosFollow.get(position).getImatge());
    }

    @Override
    public int getItemCount() {
        return listDatosFollow.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listenerFollow=listener;
    }

    @Override
    public void onClick(View v) {
        if(listenerFollow!=null){
            listenerFollow.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView username;
        ImageView foto;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            username= itemView.findViewById(R.id.idUsernameFollow);
            foto= itemView.findViewById(R.id.idImatgeFollow);
        }

    }
}
