package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CommandeAdapter extends ArrayAdapter<Commande> {

    public CommandeAdapter(Context context, List<Commande> commandes) {
        super(context, 0, commandes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_commande, parent, false);
        }

        // Récupération des éléments du layout 
        TextView tvProduit = convertView.findViewById(R.id.tvProduit);
        TextView tvQuantite = convertView.findViewById(R.id.tvPrix);

        // Récupération de la commande actuelle
        Commande commande = getItem(position);
        if (commande != null) { 
            tvProduit.setText("Produit: " + commande.getProduit());
            tvQuantite.setText("Quantité: " + commande.getPrixTotal());
        }

        return convertView;
    }
}
