package com.example.myapplication;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Commande {
    private String id;
    private String client;
    private String produit;
    private int quantite;
    private double prixTotal;

    // 🔹 Constructeur sans argument PUBLIC (important pour Firestore)
    public Commande() {
    }

    public Commande(String id, String client, String produit, int quantite, double prixTotal) {
        this.id = id;
        this.client = client;
        this.produit = produit;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
    }

    // 🔹 Getters et Setters (nécessaires pour Firestore)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }
}
