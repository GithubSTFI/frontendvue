package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class registration extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);




        EditText emailEditText, passwordEditText;
        CardView btnConnexion, btnInscription;



        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance()  ;// Obtenir l'instance d'authentification



        btnConnexion = findViewById(R.id.connxion);
        btnInscription = findViewById(R.id.btnCreate);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        btnConnexion.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btnInscription.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Connexion en cours...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            registerUser(email, password, isSuccess -> {
                progressDialog.dismiss();

                if (isSuccess) {
                    Toast.makeText(this, "Création réussie de la connexion.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(this, "Échec de la creation de compte. Vérifiez vos identifiants. ou votre connexion", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void registerUser(String email, String password, final Callback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d("TAG", "Enregistrement réussi : " + (user != null ? user.getEmail() : "Utilisateur inconnu"));
                        callback.onResult(true);
                    } else {
                        Log.w("TAG", "Échec de l'enregistrement", task.getException());
                        callback.onResult(false);
                    }
                });
    }

    interface Callback {
        void onResult(boolean isSuccess);
    }


}