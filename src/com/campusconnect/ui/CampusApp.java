package com.campusconnect.ui;

import com.campusconnect.exceptions.CapaciteDepasseeException;
import com.campusconnect.exceptions.ConflitHoraireException;
import com.campusconnect.models.*;
import com.campusconnect.services.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CampusApp {
    private static GestionActeurs gestionActeurs = new GestionActeurs();
    private static GestionFormation gestionFormation = new GestionFormation();
    private static GestionSuiviAcademique gestionSuivi = new GestionSuiviAcademique();
    private static GestionPlanning gestionPlanning = new GestionPlanning();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initialiserDonnees();

        System.out.println("==========================================");
        System.out.println("  Bienvenue sur CampusConnect Dashboard   ");
        System.out.println("==========================================\n");
        System.out.println("==========================================");

        boolean continuer = true;

        while (continuer) {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Afficher les acteurs (Étudiants / Enseignants)");
            System.out.println("2. Inscrire un étudiant à un cours");
            System.out.println("3. Saisir une note pour un étudiant");
            System.out.println("4. Générer le relevé de notes d'un étudiant");
            System.out.println("5. Planifier une séance de cours");
            System.out.println("6. Afficher le planning");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            String choix = scanner.nextLine();

            try {
                switch (choix) {
                    case "1":
                        gestionActeurs.afficherTous();
                        break;
                    case "2":
                        menuInscription();
                        break;
                    case "3":
                        menuSaisieNote();
                        break;
                    case "4":
                        menuReleveNotes();
                        break;
                    case "5":
                        menuPlanifierSeance();
                        break;
                    case "6":
                        gestionPlanning.afficherPlanning();
                        break;
                    case "0":
                        continuer = false;
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void menuInscription() {
        System.out.print("Matricule de l'étudiant : ");
        String mat = scanner.nextLine();
        Etudiant etudiant = gestionActeurs.getEtudiantByMatricule(mat);
        if (etudiant == null) {
            System.out.println("Étudiant non trouvé.");
            return;
        }

        System.out.print("Code du cours : ");
        String code = scanner.nextLine();
        Cours cours = gestionFormation.getCoursByCode(code);
        if (cours == null || cours.getGroupes().isEmpty()) {
            System.out.println("Cours non trouvé ou sans groupe.");
            return;
        }

        System.out.println("Groupes disponibles pour ce cours:");
        for (int i = 0; i < cours.getGroupes().size(); i++) {
            System.out.println((i + 1) + ". " + cours.getGroupes().get(i).getNom());
        }
        System.out.print("Choisissez le groupe (numéro) : ");
        int indexGroupe = Integer.parseInt(scanner.nextLine()) - 1;
        Groupe groupe = cours.getGroupes().get(indexGroupe);

        try {
            gestionSuivi.inscrireEtudiant(etudiant, groupe);
            System.out.println("Étudiant " + etudiant.getPrenom() + " inscrit avec succès au groupe " + groupe.getNom() + ".");
        } catch (CapaciteDepasseeException e) {
            System.out.println("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    private static void menuSaisieNote() {
        System.out.print("Matricule de l'étudiant : ");
        String mat = scanner.nextLine();
        Etudiant etudiant = gestionActeurs.getEtudiantByMatricule(mat);

        System.out.print("Code du cours : ");
        String code = scanner.nextLine();
        Cours cours = gestionFormation.getCoursByCode(code);

        if (etudiant == null || cours == null) {
            System.out.println("Étudiant ou Cours non trouvé.");
            return;
        }

        System.out.print("Note sur 20 : ");
        double note = Double.parseDouble(scanner.nextLine());
        System.out.print("Coefficient : ");
        double coef = Double.parseDouble(scanner.nextLine());

        gestionSuivi.ajouterNote(etudiant, cours, note, coef);
        System.out.println("Note ajoutée avec succès.");
    }

    private static void menuReleveNotes() {
        System.out.print("Matricule de l'étudiant : ");
        String mat = scanner.nextLine();
        Etudiant etudiant = gestionActeurs.getEtudiantByMatricule(mat);
        if (etudiant == null) {
            System.out.println("Étudiant non trouvé.");
            return;
        }
        gestionSuivi.genererReleveNotes(etudiant);
    }

    private static void menuPlanifierSeance() {
        try {
            System.out.print("Code du cours : ");
            String code = scanner.nextLine();
            Cours cours = gestionFormation.getCoursByCode(code);

            if (cours == null || cours.getGroupes().isEmpty()) {
                System.out.println("Cours non trouvé ou sans groupe.");
                return;
            }

            System.out.println("Groupes disponibles:");
            for (int i = 0; i < cours.getGroupes().size(); i++) {
                System.out.println((i + 1) + ". " + cours.getGroupes().get(i).getNom());
            }
            System.out.print("Choix du groupe : ");
            int indexGroupe = Integer.parseInt(scanner.nextLine()) - 1;
            Groupe groupe = cours.getGroupes().get(indexGroupe);

            System.out.print("ID de l'enseignant : ");
            String idEns = scanner.nextLine();
            Enseignant enseignant = gestionActeurs.getEnseignantById(idEns);

            System.out.print("ID de la salle : ");
            String idSalle = scanner.nextLine();
            Salle salle = gestionFormation.getSalleById(idSalle);

            if (enseignant == null || salle == null) {
                System.out.println("Enseignant ou Salle non trouvé(e).");
                return;
            }

            System.out.print("Date (YYYY-MM-DD) : ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Heure de début (HH:MM) : ");
            LocalTime debut = LocalTime.parse(scanner.nextLine());

            System.out.print("Heure de fin (HH:MM) : ");
            LocalTime fin = LocalTime.parse(scanner.nextLine());

            gestionPlanning.planifierSeance(groupe, enseignant, salle, date, debut, fin);

        } catch (DateTimeParseException e) {
            System.out.println("Format de date ou d'heure invalide.");
        } catch (ConflitHoraireException e) {
            System.out.println("Erreur de planification : " + e.getMessage());
        }
    }

    // --- Jeu d'essai initial ---
    private static void initialiserDonnees() {
        // Enseignants
        Enseignant prof1 = new Enseignant("E001", "Dupont", "Jean", "jean.dupont@campus.com", LocalDate.of(1980, 5, 12), Enseignant.Statut.PERMANENT, "Informatique");
        Enseignant prof2 = new Enseignant("E002", "Martin", "Sophie", "sophie.martin@campus.com", LocalDate.of(1985, 8, 22), Enseignant.Statut.VACATAIRE, "Mathématiques");
        gestionActeurs.ajouterPersonne(prof1);
        gestionActeurs.ajouterPersonne(prof2);

        // Étudiants
        Etudiant etu1 = new Etudiant("S001", "Durand", "Alice", "alice.durand@campus.com", LocalDate.of(2001, 3, 15), "MAT123", "L3", "Informatique");
        Etudiant etu2 = new Etudiant("S002", "Lefebvre", "Marc", "marc.lefebvre@campus.com", LocalDate.of(2000, 11, 2), "MAT456", "L3", "Informatique");
        gestionActeurs.ajouterPersonne(etu1);
        gestionActeurs.ajouterPersonne(etu2);

        // Cours & Groupes
        Cours coursJava = new Cours("INFO-301", "Programmation Orientée Objet", "Concepts avancés de POO en Java", 40, prof1);
        Cours coursBD = new Cours("INFO-302", "Bases de Données", "Modélisation et SQL", 30, prof2);
        gestionFormation.ajouterCours(coursJava);
        gestionFormation.ajouterCours(coursBD);

        Groupe cmJava = new Groupe("CM", coursJava, 100, prof1);
        Groupe tdJava = new Groupe("TD1", coursJava, 30, prof1);

        // Salles
        Salle amphiA = new Salle("Amphi A", 150, Salle.TypeSalle.AMPHI);
        Salle salleTP1 = new Salle("Salle 402", 35, Salle.TypeSalle.TP);
        gestionFormation.ajouterSalle(amphiA);
        gestionFormation.ajouterSalle(salleTP1);

        // Inscriptions initiales
        try {
            gestionSuivi.inscrireEtudiant(etu1, cmJava);
            gestionSuivi.inscrireEtudiant(etu2, cmJava);
            gestionSuivi.inscrireEtudiant(etu1, tdJava);

            gestionSuivi.ajouterNote(etu1, coursJava, 16.5, 2.0);
            gestionSuivi.ajouterNote(etu1, coursJava, 14.0, 1.0);
        } catch(Exception e) {}
    }
}
