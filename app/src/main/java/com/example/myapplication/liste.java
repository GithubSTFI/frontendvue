package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class liste extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listViewCommandes;
    private CommandeAdapter adapter;
    private List<Commande> commandesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialisation du ListView et de l'ArrayAdapter
        listViewCommandes = findViewById(R.id.listViewCommandes);
        commandesList = new ArrayList<>();

        adapter = new CommandeAdapter(this, commandesList);
        listViewCommandes.setAdapter(adapter);


        listViewCommandes.setAdapter(adapter);


        // Récupérer et afficher les commandes
        recupererCommandes();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.page_1) {
                    startActivity(new Intent(liste.this, home.class));
                    return true;
                } else if (id == R.id.page_2) {
                    startActivity(new Intent(liste.this, liste.class));
                    return true;
                }else if (id == R.id.page_3) {
                    startActivity(new Intent(liste.this, NewCommandeActivity.class));
                    return true;
                }


                return false;
            }




        });


    }

    private void recupererCommandes() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("recuperation des commande en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection("commandes").get()
                .addOnSuccessListener(result -> {
                    commandesList.clear(); // Vider la liste avant d'ajouter les nouvelles données
                    for (QueryDocumentSnapshot document : result) {
                        Commande commande = document.toObject(Commande.class);
                        commandesList.add(commande);
                    }

                    progressDialog.dismiss();

                    // Mettre à jour la liste affichée
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Commandes récupérées !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Erreur lors de la récupération", Toast.LENGTH_SHORT).show();
                        }
                );
    }
}