package com.campusconnect.services;

import com.campusconnect.models.Personne;
import com.campusconnect.models.Etudiant;
import com.campusconnect.models.Enseignant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestionActeurs {
    private List<Personne> personnes;

    public GestionActeurs() {
        this.personnes = new ArrayList<>();
    }

    public void supprimerPersonne(Personne p) {
        personnes.remove(p);
    }

    public void ajouterPersonne(Personne p) {
        if (!personnes.contains(p)) {
            personnes.add(p);
        }
    }

    public List<Etudiant> getEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        for (Personne p : personnes) {
            if (p instanceof Etudiant) {
                etudiants.add((Etudiant) p);
            }
        }
        return etudiants;
    }

    public List<Enseignant> getEnseignants() {
        List<Enseignant> enseignants = new ArrayList<>();
        for (Personne p : personnes) {
            if (p instanceof Enseignant) {
                enseignants.add((Enseignant) p);
            }
        }
        return enseignants;
    }

    public Etudiant getEtudiantByMatricule(String matricule) {
        for (Personne p : personnes) {
            if (p instanceof Etudiant && ((Etudiant) p).getMatricule().equals(matricule)) {
                return (Etudiant) p;
            }
        }
        return null;
    }

    public Enseignant getEnseignantById(String id) {
        for (Personne p : personnes) {
            if (p instanceof Enseignant && p.getId().equals(id)) {
                return (Enseignant) p;
            }
        }
        return null;
    }

    public void afficherTous() {
        for (Personne p : personnes) {
            p.afficherDetails();
        }
    }
}
