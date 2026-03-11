package com.campusconnect.services;

import com.campusconnect.exceptions.CapaciteDepasseeException;
import com.campusconnect.models.Cours;
import com.campusconnect.models.Etudiant;
import com.campusconnect.models.Groupe;
import com.campusconnect.models.Inscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestionSuiviAcademique {
    private List<Inscription> inscriptions;

    public GestionSuiviAcademique() {
        this.inscriptions = new ArrayList<>();
    }

    public void inscrireEtudiant(Etudiant etudiant, Groupe groupe) throws CapaciteDepasseeException {
        groupe.inscrireEtudiant(etudiant);
        Cours cours = groupe.getCours();

        // Vérifier si une inscription pour ce cours existe déjà
        Optional<Inscription> existing = inscriptions.stream()
            .filter(i -> i.getEtudiant().equals(etudiant) && i.getCours().equals(cours))
            .findFirst();

        if (!existing.isPresent()) {
            inscriptions.add(new Inscription(etudiant, cours));
        }
    }

    public void ajouterNote(Etudiant etudiant, Cours cours, double valeur, double coefficient) {
        for (Inscription i : inscriptions) {
            if (i.getEtudiant().equals(etudiant) && i.getCours().equals(cours)) {
                i.ajouterNote(valeur, coefficient);
                return;
            }
        }
        throw new IllegalArgumentException("L'étudiant n'est pas inscrit à ce cours.");
    }

    public void genererReleveNotes(Etudiant etudiant) {
        System.out.println("--- Relevé de notes de " + etudiant.getPrenom() + " " + etudiant.getNom() + " ---");
        double sommeMoyennes = 0;
        int nbCours = 0;

        for (Inscription i : inscriptions) {
            if (i.getEtudiant().equals(etudiant)) {
                double moyenne = i.calculerMoyenne();
                System.out.println(i.getCours().getIntitule() + " : " + (Double.isNaN(moyenne) ? "Aucune note" : String.format("%.2f", moyenne)));
                if (!Double.isNaN(moyenne)) {
                    sommeMoyennes += moyenne;
                    nbCours++;
                }
            }
        }

        if (nbCours > 0) {
            System.out.println("Moyenne Générale : " + String.format("%.2f", sommeMoyennes / nbCours));
        }
        System.out.println("-------------------------------------------------");
    }
}
