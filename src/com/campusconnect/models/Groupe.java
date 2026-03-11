package com.campusconnect.models;

import com.campusconnect.exceptions.CapaciteDepasseeException;
import java.util.ArrayList;
import java.util.List;

public class Groupe {
    private String nom; // CM, TD1, TP1, etc.
    private Cours cours;
    private int capaciteMax;
    private Enseignant enseignant;
    private List<Etudiant> etudiantsInscrits;

    public Groupe(String nom, Cours cours, int capaciteMax, Enseignant enseignant) {
        this.nom = nom;
        this.cours = cours;
        this.capaciteMax = capaciteMax;
        this.enseignant = enseignant;
        this.etudiantsInscrits = new ArrayList<>();

        if (enseignant != null) {
            enseignant.ajouterGroupe(this);
        }
        if (cours != null) {
            cours.ajouterGroupe(this);
        }
    }

    public String getNom() { return nom; }
    public Cours getCours() { return cours; }
    public int getCapaciteMax() { return capaciteMax; }
    public Enseignant getEnseignant() { return enseignant; }
    public List<Etudiant> getEtudiantsInscrits() { return etudiantsInscrits; }

    public void setNom(String nom) { this.nom = nom; }
    public void setCours(Cours cours) { this.cours = cours; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    public void inscrireEtudiant(Etudiant etudiant) throws CapaciteDepasseeException {
        if (etudiantsInscrits.size() >= capaciteMax) {
            throw new CapaciteDepasseeException("La capacité maximale du groupe " + nom + " est atteinte (" + capaciteMax + ").");
        }
        if (!etudiantsInscrits.contains(etudiant)) {
            etudiantsInscrits.add(etudiant);
            etudiant.ajouterGroupe(this);
        }
    }

    @Override
    public String toString() {
        return cours.getCode() + " - " + nom;
    }
}
