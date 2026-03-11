package com.campusconnect.services;

import com.campusconnect.exceptions.ConflitHoraireException;
import com.campusconnect.models.Enseignant;
import com.campusconnect.models.Groupe;
import com.campusconnect.models.Salle;
import com.campusconnect.models.Seance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GestionPlanning {
    private List<Seance> seances;

    public GestionPlanning() {
        this.seances = new ArrayList<>();
    }

    public void planifierSeance(Groupe groupe, Enseignant enseignant, Salle salle,
                                LocalDate date, LocalTime debut, LocalTime fin) throws ConflitHoraireException {
        // Validation des horaires
        if (fin.isBefore(debut) || fin.equals(debut)) {
            throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début.");
        }

        // Vérification de la capacité
        if (groupe.getEtudiantsInscrits().size() > salle.getCapaciteAccueil()) {
            throw new ConflitHoraireException("La salle " + salle.getIdSalle() + " est trop petite pour le groupe " + groupe.getNom() + ".");
        }

        // Vérification des conflits horaires
        for (Seance s : seances) {
            if (s.getDate().equals(date)) {
                boolean chevauchementHoraire = (debut.isBefore(s.getHeureFin()) && fin.isAfter(s.getHeureDebut()));

                if (chevauchementHoraire) {
                    if (s.getSalle().equals(salle)) {
                        throw new ConflitHoraireException("Conflit de salle : La salle " + salle.getIdSalle() + " est déjà occupée.");
                    }
                    if (s.getEnseignant().equals(enseignant)) {
                        throw new ConflitHoraireException("Conflit enseignant : M./Mme " + enseignant.getNom() + " a déjà cours.");
                    }
                    if (s.getGroupe().equals(groupe)) {
                        throw new ConflitHoraireException("Conflit de groupe : Le groupe " + groupe.getNom() + " a déjà cours.");
                    }
                }
            }
        }

        seances.add(new Seance(groupe, enseignant, salle, date, debut, fin));
        System.out.println("Séance planifiée avec succès.");
    }

    public void afficherPlanning() {
        if (seances.isEmpty()) {
            System.out.println("Aucune séance planifiée.");
            return;
        }
        System.out.println("--- Planning des Séances ---");
        for (Seance s : seances) {
            System.out.println(s.getDate() + " de " + s.getHeureDebut() + " à " + s.getHeureFin() +
                    " | " + s.getGroupe().getCours().getCode() + " " + s.getGroupe().getNom() +
                    " | Salle : " + s.getSalle().getIdSalle() +
                    " | Enseignant : " + s.getEnseignant().getNom());
        }
    }
}
