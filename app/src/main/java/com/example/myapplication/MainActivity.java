package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity  {

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);




        CardView btnInscription = findViewById(R.id.inscriptionBtn);



        btnInscription.setOnClickListener(v -> {
            Intent intent = new Intent(this, registration.class);
            startActivity(intent);
        });



        EditText emailEditText, passwordEditText;
        CardView btnConnexion;

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance()  ;// Obtenir l'instance d'authentification



        btnConnexion = findViewById(R.id.connectUser);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);


        btnConnexion.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Connexion en cours...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                signInUser(email, password, isSuccess -> {
                    progressDialog.dismiss();

                    if (isSuccess) {
                        Toast.makeText(this, "Réussite de la connexion.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, home.class));
                    } else {
                        Toast.makeText(this, "Échec de la connexion. Vérifiez vos identifiants ou votre connexion.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void signInUser(String email, String password, final Callback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null /* && user.isEmailVerified() */) {
                            Log.d("TAG", "Connexion réussie : " + user.getEmail());
                            callback.onResult(true);
                        } else {
                            Log.e("FirebaseAuth", "Connexion refusée");
                            callback.onResult(false);
                        }
                    } else {
                        Log.w("TAG", "Échec de la connexion", task.getException());
                        callback.onResult(false);
                    }
                });
    }



    interface Callback {
        void onResult(boolean isSuccess);
    }
}