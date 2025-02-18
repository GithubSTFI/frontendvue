package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.UUID;

public class NewCommandeActivity extends AppCompatActivity {

    // Références vers les vues du layout
    private TextInputEditText pizzaName;
    private TextInputEditText prizzaPrice;
    private Button btnCreatePizza;

    // Instance Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_commande); // Assurez-vous que le nom du layout est correct

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Lien avec les vues du layout
        pizzaName = findViewById(R.id.pizzaName);
        prizzaPrice = findViewById(R.id.pizzaPrice);
        btnCreatePizza = findViewById(R.id.buttonCreatePizza);

        btnCreatePizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Afficher un ProgressDialog pendant le traitement
                final ProgressDialog progressDialog = new ProgressDialog(NewCommandeActivity.this);
                progressDialog.setMessage("Création de la commande...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Récupérer le nom de la pizza
                String pizzaPriceStr = prizzaPrice.getText().toString(); // Récupère la valeur sous forme de chaîne
                Double.parseDouble(pizzaPriceStr);
                double prixTotal =Double.parseDouble(pizzaPriceStr);
                // Prix de base, à ajuster selon la logique que vous souhaite
                String nomPizza = pizzaName.getText().toString().trim();
                if (nomPizza.isEmpty()) {
                    Toast.makeText(NewCommandeActivity.this, "Veuillez entrer le nom de la pizza", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                // Calcul du prix (fixe pour cet exemple, vous pouvez l'ajuster en fonction de la taille ou d'autres critères)

                String idCommande = UUID.randomUUID().toString();
                // Création de l'instance de Commande avec seulement le nom et le prix
                Commande commande = new Commande(idCommande,"client",nomPizza, 5,prixTotal);

                // Ajout de la commande dans la collection "commandes" de Firestore
                db.collection("menuCommandes")
                        .add(commande)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                progressDialog.dismiss();
                                Toast.makeText(NewCommandeActivity.this, "Commande ajoutée avec succès !", Toast.LENGTH_SHORT).show();
                                // Optionnel : vider les champs ou fermer l'activité
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(NewCommandeActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.page_1) {
                startActivity(new Intent(NewCommandeActivity.this, home.class));
                return true;
            } else if (id == R.id.page_2) {
                startActivity(new Intent(NewCommandeActivity.this, liste.class));
                return true;
            } else if (id == R.id.page_3) {
                startActivity(new Intent(NewCommandeActivity.this, NewCommandeActivity.class));
                return true;
            }
            return false;
        });
    }
}
