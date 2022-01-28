package com.hackslash.getslot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackslash.getslot.Model.Hospital;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalHolder> {
    ArrayList<Hospital> hospitals=new ArrayList<>();

    public HospitalAdapter(ArrayList<Hospital> hospitals) {
        this.hospitals=hospitals;
    }

    @NonNull
    @Override
    public HospitalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_element,parent,false);
        HospitalHolder hospitalHolder=new HospitalHolder(myView);
        return hospitalHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.HospitalHolder holder, int position) {
        holder.address.setText(hospitals.get(position).getAddress());
        holder.fee.setText(hospitals.get(position).getFeetype());
        holder.name.setText(hospitals.get(position).getName());
        holder.vaccine.setText(hospitals.get(position).getVaccine());
        holder.session.setText(hospitals.get(position).getSessions());
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class HospitalHolder extends RecyclerView.ViewHolder {
        TextView name,fee,address,view,session,vaccine;
        public HospitalHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.hospital_name);
            fee=(TextView) itemView.findViewById(R.id.hospital_price);
            address=(TextView) itemView.findViewById(R.id.hospital_address);
            session=(TextView) itemView.findViewById(R.id.hospial_sessions);
            vaccine=(TextView) itemView.findViewById(R.id.vaccine_name);
        }
    }
}
