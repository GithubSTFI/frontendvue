package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    private RecyclerView recyclerCommandes;
    private CommandeAdapterHome adapter;
    private List<Commande> commandes;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialiser Firestore
        db = FirebaseFirestore.getInstance();

        // Désactiver la persistance locale de Firestore pour éviter l'utilisation du cache
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false) // Désactive le cache local
                .build();
        db.setFirestoreSettings(settings);

        // Initialiser ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement des commandes...");
        progressDialog.setCancelable(false);

        // Configurer le RecyclerView
        recyclerCommandes = findViewById(R.id.recyclerCommandes);
        recyclerCommandes.setLayoutManager(new LinearLayoutManager(this));
        commandes = new ArrayList<>();
        adapter = new CommandeAdapterHome(commandes, commande -> ouvrirDetail(
                commande.getId(),
                commande.getClient(),
                commande.getProduit(),
                commande.getQuantite(),
                commande.getPrixTotal()
        ));
        recyclerCommandes.setAdapter(adapter);

        // BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.page_1) {
                startActivity(new Intent(home.this, home.class));
                return true;
            } else if (id == R.id.page_2) {
                startActivity(new Intent(home.this, liste.class));
                return true;
            } else if (id == R.id.page_3) {
                startActivity(new Intent(home.this, NewCommandeActivity.class));
                return true;
            }
            return false;
        });

        loadCommandes();
    }

    /**
     * Cette méthode est appelée chaque fois que l'activité devient visible,
     * ce qui permet de recharger les commandes depuis Firestore.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadCommandes(); // Recharge les données à chaque retour sur cette activité
    }

    /**
     * Récupère les commandes depuis Firestore et met à jour le RecyclerView.
     */
    public void loadCommandes() {
        progressDialog.show(); // Affiche le ProgressDialog

        db.collection("menuCommandes")
                .get(Source.SERVER) // Force la récupération des données depuis le serveur, sans utiliser le cache
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    commandes.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Commande commande = document.toObject(Commande.class);
                        if (commande != null) {
                            // Récupérer l'ID du document Firestore
                            String idFirestore = document.getId();
                            // Ajouter l'ID Firestore à l'objet commande
                            commande.setId(idFirestore); // Ajoute un setter ou un champ pour cela si nécessaire
                            commandes.add(commande);
                        }
                    }
                    Log.d("Chargement", "Commandes après chargement : " + commandes.size());
                    adapter.setCommandes(commandes);
                    adapter.notifyDataSetChanged(); // Notifie l'adaptateur de la mise à jour

                    progressDialog.dismiss(); // Masque le ProgressDialog après le chargement
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Masque le ProgressDialog en cas d'échec
                    Toast.makeText(home.this, "Erreur lors du chargement des commandes : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Ouvre l'activité de détails pour la commande sélectionnée.
     */
    private void ouvrirDetail(String id,String client, String produit, int quantite, double prixTotal) {
        Intent intent = new Intent(home.this, details.class);
        intent.putExtra("id", id);
        intent.putExtra("client", client);
        intent.putExtra("produit", produit);
        intent.putExtra("quantite", quantite);
        intent.putExtra("prixTotal", prixTotal);
        startActivity(intent);
    }
}
