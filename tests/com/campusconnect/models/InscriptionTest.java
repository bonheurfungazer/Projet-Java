package com.campusconnect.models;

import com.campusconnect.models.Cours;
import com.campusconnect.models.Etudiant;
import com.campusconnect.models.Enseignant;
import com.campusconnect.models.Inscription;
import java.time.LocalDate;

public class InscriptionTest {
    public static void main(String[] args) {
        int failures = 0;
        failures += testCalculerMoyenneSansNotes();
        failures += testCalculerMoyenneUneNote();
        failures += testCalculerMoyennePlusieursNotes();

        if (failures == 0) {
            System.out.println("Tous les tests d'Inscription ont réussi !");
            System.exit(0);
        } else {
            System.err.println(failures + " tests ont échoué !");
            System.exit(1);
        }
    }

    private static int testCalculerMoyenneSansNotes() {
        System.out.print("Test calculerMoyenne sans notes... ");
        Inscription inscription = createMockInscription();
        double moyenne = inscription.calculerMoyenne();
        if (Math.abs(moyenne - 0.0) > 0.001) {
            System.err.println("FAIL: La moyenne devrait être 0.0 pour une inscription sans notes, mais était " + moyenne);
            return 1;
        }
        System.out.println("PASS");
        return 0;
    }

    private static int testCalculerMoyenneUneNote() {
        System.out.print("Test calculerMoyenne avec une note... ");
        Inscription inscription = createMockInscription();
        inscription.ajouterNote(15.0, 2.0);
        double moyenne = inscription.calculerMoyenne();
        if (Math.abs(moyenne - 15.0) > 0.001) {
            System.err.println("FAIL: La moyenne devrait être 15.0, mais était " + moyenne);
            return 1;
        }
        System.out.println("PASS");
        return 0;
    }

    private static int testCalculerMoyennePlusieursNotes() {
        System.out.print("Test calculerMoyenne avec plusieurs notes... ");
        Inscription inscription = createMockInscription();
        inscription.ajouterNote(10.0, 1.0);
        inscription.ajouterNote(20.0, 3.0);
        // (10*1 + 20*3) / (1+3) = (10 + 60) / 4 = 70 / 4 = 17.5
        double moyenne = inscription.calculerMoyenne();
        if (Math.abs(moyenne - 17.5) > 0.001) {
            System.err.println("FAIL: La moyenne devrait être 17.5, mais était " + moyenne);
            return 1;
        }
        System.out.println("PASS");
        return 0;
    }

    private static Inscription createMockInscription() {
        Enseignant prof = new Enseignant("P1", "Dupont", "Jean", "jean.dupont@univ.fr",
                                        LocalDate.of(1980, 1, 1), Enseignant.Statut.PERMANENT, "Informatique");
        Cours cours = new Cours("INF101", "Java", "Cours de Java", 30, prof);
        Etudiant etudiant = new Etudiant("E1", "Durand", "Marie", "marie.durand@etud.univ.fr",
                                        LocalDate.of(2000, 5, 15), "20200001", "L3", "Informatique");
        return new Inscription(etudiant, cours);
    }
}
