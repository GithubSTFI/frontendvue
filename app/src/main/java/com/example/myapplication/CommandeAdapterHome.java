package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommandeAdapterHome extends RecyclerView.Adapter<CommandeAdapterHome.CommandeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Commande commande);
    }

    private List<Commande> commandes;
    private OnItemClickListener listener;

    public CommandeAdapterHome(List<Commande> commandes, OnItemClickListener listener) {
        this.commandes = commandes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande_home, parent, false);
        return new CommandeViewHolder(v);
    }

//    @Override
//    public void onBindViewHolder(@NonNull CommandeViewHolder holder, int position) {
//        Commande commande = commandes.get(position);
//        holder.bind(commande, listener);
//    }

    @Override
    public void onBindViewHolder(@NonNull CommandeViewHolder holder, int position) {
        Commande commande = commandes.get(position);

        // Remplir les deux éléments avec les mêmes valeurs
        String produit = commande.getProduit();  // Si vous avez un produit unique
        double prixTotal = commande.getPrixTotal();  // Prix commun pour les deux produits

        // Remplir les TextViews avec les mêmes données pour les deux éléments
        holder.tvProduit1.setText(produit);
        holder.tvProduit2.setText(produit);
        holder.tvPrixTotal1.setText(String.valueOf(prixTotal));
        holder.tvPrixTotal2.setText(String.valueOf(prixTotal));

        holder.bind(commande, listener);
    }


    @Override
    public int getItemCount() {
        return commandes.size();
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
        notifyDataSetChanged();
    }

    static class CommandeViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduit1, tvProduit2, tvPrixTotal1, tvPrixTotal2;

        public CommandeViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialisation des TextViews pour les produits et prix
            tvProduit1 = itemView.findViewById(R.id.tvProduit1);
            tvProduit2 = itemView.findViewById(R.id.tvProduit2);
            tvPrixTotal1 = itemView.findViewById(R.id.tvPrixTotal1);
            tvPrixTotal2 = itemView.findViewById(R.id.tvPrixTotal2);
        }

        public void bind(final Commande commande, final OnItemClickListener listener) {
            // Lier les données aux vues
            itemView.setOnClickListener(v -> listener.onItemClick(commande));
        }
    }

}
