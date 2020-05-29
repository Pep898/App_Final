package com.example.flypath.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flypath.R;
import com.mapbox.mapboxsdk.geometry.VisibleRegion;

import java.util.ArrayList;

public class AdaptadorLlistaRutes extends RecyclerView.Adapter<AdaptadorLlistaRutes.ViewHolderDatos>
implements View.OnClickListener{
    ArrayList<ItemLlista> listItemsRutes;
    View.OnClickListener listenerRutes;

    public AdaptadorLlistaRutes(ArrayList<ItemLlista> listItems) {
        this.listItemsRutes = listItems;
    }
    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list,null,false);

        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nom.setText(listItemsRutes.get(position).getNom());
        holder.descripcio.setText(listItemsRutes.get(position).getDescripcio());
        holder.imatge.setImageResource(listItemsRutes.get(position).getImatge());
        holder.creador.setText(listItemsRutes.get(position).getCreador());
    }

    @Override
    public int getItemCount() {
        return listItemsRutes.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listenerRutes=listener;
    }
    @Override
    public void onClick(View v) {
        if(listenerRutes!=null){
            listenerRutes.onClick(v);
        }
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nom,descripcio,creador;
        ImageView imatge;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nom= itemView.findViewById(R.id.idNomRuta);
            descripcio= itemView.findViewById(R.id.idDescripcioRuta);
            imatge= itemView.findViewById(R.id.idImatgeRuta);
            creador= itemView.findViewById(R.id.idCreadorRuta);
        }

    }
}
