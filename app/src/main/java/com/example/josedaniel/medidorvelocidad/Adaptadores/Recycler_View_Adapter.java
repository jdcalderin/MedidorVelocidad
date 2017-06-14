package com.example.josedaniel.medidorvelocidad.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.example.josedaniel.medidorvelocidad.R;
import com.example.josedaniel.medidorvelocidad.dto.medidorVelocidadDTO;

import java.util.Collections;
import java.util.List;

/**
 * Created by calderjo on 13/06/2017.
 */

public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder>{

    List<medidorVelocidadDTO> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter(List<medidorVelocidadDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).dispositivo);
        holder.description.setText(list.get(position).macdispositivo);
        holder.imageView.setImageResource(list.get(position).imgdispositivo);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, medidorVelocidadDTO data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(medidorVelocidadDTO data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}
