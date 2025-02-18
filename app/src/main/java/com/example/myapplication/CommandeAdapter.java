package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommandeAdapter extends ArrayAdapter<Commande> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;

    public CommandeAdapter(Context context, List<Commande> commandes) {
        super(context, 0, commandes);
        this.context = context;
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
        ImageView imgDelete = convertView.findViewById(R.id.imgDelete);

        // Récupération de la commande actuelle
        Commande commande = getItem(position);
        if (commande != null) {
            tvProduit.setText("Produit: " + commande.getProduit());
            tvQuantite.setText("Quantité: " + commande.getPrixTotal());

            // Gestion du clic sur l'icône de suppression
            imgDelete.setOnClickListener(v -> {
                supprimerCommande(commande, position);
            });
        }

        return convertView;
    }

    // Méthode pour supprimer une commande de Firebase et de la liste
    // Méthode pour supprimer une commande de Firebase et de la liste
    private void supprimerCommande(Commande commande, int position) {
        // Afficher le ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Suppression en cours...");
        progressDialog.setCancelable(false); // Ne pas permettre de fermer le dialog pendant la suppression
        progressDialog.show();

        // Suppression de la commande de Firebase
        db.collection("commandes")
                .document(commande.getId())  // Suppression basée sur l'ID de la commande
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Supprimer la commande de la liste et rafraîchir l'affichage
                    remove(commande);
                    notifyDataSetChanged();
                    progressDialog.dismiss(); // Fermer le ProgressDialog
                    Toast.makeText(context, "Commande supprimée", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Fermer le ProgressDialog en cas d'échec
                    Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                });
    }


}
