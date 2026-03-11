package com.campusconnect.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Enseignant extends Personne {
    public enum Statut { PERMANENT, VACATAIRE }

    private Statut statut;
    private String departement;
    private List<Groupe> groupesEnseignes;

    public Enseignant(String id, String nom, String prenom, String email, LocalDate dateNaissance,
                      Statut statut, String departement) {
        super(id, nom, prenom, email, dateNaissance);
        this.statut = statut;
        this.departement = departement;
        this.groupesEnseignes = new ArrayList<>();
    }

    public Statut getStatut() { return statut; }
    public String getDepartement() { return departement; }
    public List<Groupe> getGroupesEnseignes() { return groupesEnseignes; }

    public void setStatut(Statut statut) { this.statut = statut; }
    public void setDepartement(String departement) { this.departement = departement; }

    public void ajouterGroupe(Groupe groupe) {
        if (!this.groupesEnseignes.contains(groupe)) {
            this.groupesEnseignes.add(groupe);
        }
    }

    @Override
    public void afficherDetails() {
        System.out.println("Enseignant [" + departement + "] " + prenom + " " + nom + " (" + statut + ")");
    }
}
