package com.campusconnect.models;

import java.util.ArrayList;
import java.util.List;

public class Inscription {
    public static class Note {
        private double valeur;
        private double coefficient;

        public Note(double valeur, double coefficient) {
            if (valeur < 0 || valeur > 20) throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
            if (coefficient <= 0) throw new IllegalArgumentException("Le coefficient doit être strictement positif.");
            this.valeur = valeur;
            this.coefficient = coefficient;
        }

        public double getValeur() { return valeur; }
        public double getCoefficient() { return coefficient; }
    }

    private Etudiant etudiant;
    private Cours cours;
    private List<Note> notes;

    public Inscription(Etudiant etudiant, Cours cours) {
        this.etudiant = etudiant;
        this.cours = cours;
        this.notes = new ArrayList<>();
    }

    public Etudiant getEtudiant() { return etudiant; }
    public Cours getCours() { return cours; }
    public List<Note> getNotes() { return notes; }

    public void ajouterNote(double valeur, double coefficient) {
        this.notes.add(new Note(valeur, coefficient));
    }

    public double calculerMoyenne() {
        if (notes.isEmpty()) return 0.0;
        double somme = 0;
        double sumCoeffs = 0;
        for (Note n : notes) {
            somme += n.getValeur() * n.getCoefficient();
            sumCoeffs += n.getCoefficient();
        }
        return somme / sumCoeffs;
    }
}
