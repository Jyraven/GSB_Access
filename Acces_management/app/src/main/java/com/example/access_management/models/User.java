package com.example.access_management.models;

public class User {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String groupe;
    private String role;
    private int niveau;

    public User(int id, String nom, String prenom, String email, String groupe, String role, int niveau) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.groupe = groupe;
        this.role = role;
        this.niveau = niveau;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getGroupe() {
        return groupe;
    }

    public String getRole() {
        return role;
    }

    public int getNiveau() {
        return niveau;
    }
}