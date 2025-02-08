package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        CardView btnOuvrir1 = findViewById(R.id.btnOuvrir1);
        CardView btnOuvrir2 = findViewById(R.id.btnOuvrir2);
        CardView btnOuvrir3 = findViewById(R.id.btnOuvrir3);
        CardView btnOuvrir4 = findViewById(R.id.btnOuvrir4);

        btnOuvrir1.setOnClickListener(v -> ouvrirDetail("test", "Pizza 1", 2, 2000));
        btnOuvrir2.setOnClickListener(v -> ouvrirDetail("Alice", "pizza 2", 1, 1000));
        btnOuvrir3.setOnClickListener(v -> ouvrirDetail("John", "pizza 3", 3, 1500));
        btnOuvrir4.setOnClickListener(v -> ouvrirDetail("Emma", "pizza 4", 1, 3000));



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.page_1) {
                    startActivity(new Intent(home.this, home.class));
                    return true;
                } else if (id == R.id.page_2) {
                    startActivity(new Intent(home.this, liste.class));
                    return true;
                }


                return false;
            }


        });


    }





    private void ouvrirDetail(String client, String produit, int quantite, double prixTotal) {
        Intent intent = new Intent(home.this, details.class);
        intent.putExtra("client", client);
        intent.putExtra("produit", produit);
        intent.putExtra("quantite", quantite);
        intent.putExtra("prixTotal", prixTotal);
        startActivity(intent);
    }

}