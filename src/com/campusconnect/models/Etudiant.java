package com.campusconnect.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Etudiant extends Personne {
    private String matricule;
    private String anneeEtude; // L1, L2, L3, etc.
    private String filiere;
    private List<Groupe> groupesInscrits;

    public Etudiant(String id, String nom, String prenom, String email, LocalDate dateNaissance,
                    String matricule, String anneeEtude, String filiere) {
        super(id, nom, prenom, email, dateNaissance);
        this.matricule = matricule;
        this.anneeEtude = anneeEtude;
        this.filiere = filiere;
        this.groupesInscrits = new ArrayList<>();
    }

    public String getMatricule() { return matricule; }
    public String getAnneeEtude() { return anneeEtude; }
    public String getFiliere() { return filiere; }
    public List<Groupe> getGroupesInscrits() { return groupesInscrits; }

    public void ajouterGroupe(Groupe groupe) {
        if (!this.groupesInscrits.contains(groupe)) {
            this.groupesInscrits.add(groupe);
        }
    }

    @Override
    public void afficherDetails() {
        System.out.println("Etudiant [" + matricule + "] " + prenom + " " + nom + " - " + filiere + " (" + anneeEtude + ")");
    }
}
