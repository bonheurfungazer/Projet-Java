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

    // Couleurs ANSI
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String CYAN = "\033[36m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String RED = "\033[31m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";

    public static void main(String[] args) {
        initialiserDonnees();

        boolean continuer = true;
        clearScreen();

        while (continuer) {
            afficherEnTete();
            System.out.println(CYAN + "╭──────────────────────────────────────────────────╮" + RESET);
            System.out.println(CYAN + "│                 Menu Principal                   │" + RESET);
            System.out.println(CYAN + "├──────────────────────────────────────────────────┤" + RESET);
            System.out.println(CYAN + "│ " + RESET + "1. Afficher les acteurs (Étudiants/Enseignants)  " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "2. Inscrire un étudiant à un cours               " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "3. Saisir une note pour un étudiant              " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "4. Générer le relevé de notes d'un étudiant      " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "5. Planifier une séance de cours                 " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "6. Afficher le planning                          " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "0. Quitter l'application                         " + CYAN + "│" + RESET);
            System.out.println(CYAN + "╰──────────────────────────────────────────────────╯" + RESET);

            String choix = promptInput("Votre choix");
            clearScreen();

            try {
                switch (choix) {
                    case "1":
                        afficherActeursFiltres();
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
                        System.out.println(GREEN + "✓ Au revoir !" + RESET);
                        break;
                    default:
                        System.out.println(RED + "⚠ Choix invalide." + RESET);
                }
            } catch (Exception e) {
                System.out.println(RED + "⚠ Une erreur interne s'est produite." + RESET);
            }

            if (continuer) {
                System.out.println("\n" + YELLOW + "Appuyez sur Entrée pour continuer..." + RESET);
                scanner.nextLine();
                clearScreen();
            }
        }
        scanner.close();
    }

    private static void afficherEnTete() {
        System.out.println(BLUE + BOLD + "╔══════════════════════════════════════════════════╗");
        System.out.println("║        BIENVENUE SUR CAMPUSCONNECT (CLI)         ║");
        System.out.println("║ " + YELLOW + "Projet Réalisé par : Guepi Takouo Peguy Maeva    " + BLUE + "║");
        System.out.println("╚══════════════════════════════════════════════════╝" + RESET);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String promptInput(String label) {
        System.out.print("\n" + PURPLE + "╭─[ " + label + " ]\n╰─❯ " + RESET);
        return scanner.nextLine();
    }

    private static void afficherActeursFiltres() {
        System.out.println(GREEN + BOLD + "\n--- Liste des Étudiants ---" + RESET);
        System.out.printf(CYAN + "%-10s | %-15s | %-15s | %-10s | %-5s\n" + RESET, "Matricule", "Nom", "Prénom", "Filière", "Année");
        System.out.println("----------------------------------------------------------------------");
        for (Etudiant e : gestionActeurs.getEtudiants()) {
            System.out.printf("%-10s | %-15s | %-15s | %-10s | %-5s\n", e.getMatricule(), e.getNom(), e.getPrenom(), e.getFiliere(), e.getAnneeEtude());
        }

        System.out.println(GREEN + BOLD + "\n--- Liste des Enseignants ---" + RESET);
        System.out.printf(CYAN + "%-10s | %-15s | %-15s | %-15s | %-10s\n" + RESET, "ID", "Nom", "Prénom", "Département", "Statut");
        System.out.println("----------------------------------------------------------------------------");
        for (Enseignant e : gestionActeurs.getEnseignants()) {
            System.out.printf("%-10s | %-15s | %-15s | %-15s | %-10s\n", e.getId(), e.getNom(), e.getPrenom(), e.getDepartement(), e.getStatut());
        }
    }

    private static void menuInscription() {
        System.out.println(BOLD + "--- Inscription d'un Étudiant ---" + RESET);
        String mat = promptInput("Matricule de l'étudiant");
        Etudiant etudiant = gestionActeurs.getEtudiantByMatricule(mat);
        if (etudiant == null) {
            System.out.println(RED + "⚠ Étudiant non trouvé." + RESET);
            return;
        }

        String code = promptInput("Code du cours");
        Cours cours = gestionFormation.getCoursByCode(code);
        if (cours == null || cours.getGroupes().isEmpty()) {
            System.out.println(RED + "⚠ Cours non trouvé ou sans groupe." + RESET);
            return;
        }

        System.out.println(CYAN + "\nGroupes disponibles :" + RESET);
        for (int i = 0; i < cours.getGroupes().size(); i++) {
            System.out.println(" [" + (i + 1) + "] " + cours.getGroupes().get(i).getNom());
        }

        try {
            int indexGroupe = Integer.parseInt(promptInput("Numéro du groupe")) - 1;
            Groupe groupe = cours.getGroupes().get(indexGroupe);

            gestionSuivi.inscrireEtudiant(etudiant, groupe);
            System.out.println(GREEN + "✓ Inscription réussie : " + etudiant.getPrenom() + " -> " + groupe.getNom() + RESET);
        } catch (CapaciteDepasseeException e) {
            System.out.println(RED + "⚠ La capacité maximale est atteinte." + RESET);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println(RED + "⚠ Choix invalide." + RESET);
        }
    }

    private static void menuSaisieNote() {
        System.out.println(BOLD + "--- Saisie de Note ---" + RESET);
        String mat = promptInput("Matricule de l'étudiant");
        Etudiant etudiant = gestionActeurs.getEtudiantByMatricule(mat);

        String code = promptInput("Code du cours");
        Cours cours = gestionFormation.getCoursByCode(code);

        if (etudiant == null || cours == null) {
            System.out.println(RED + "⚠ Étudiant ou Cours non trouvé." + RESET);
            return;
        }

        try {
            double note = Double.parseDouble(promptInput("Note sur 20"));
            double coef = Double.parseDouble(promptInput("Coefficient"));
            gestionSuivi.ajouterNote(etudiant, cours, note, coef);
            System.out.println(GREEN + "✓ Note ajoutée avec succès." + RESET);
        } catch (NumberFormatException e) {
             System.out.println(RED + "⚠ Veuillez entrer des nombres valides." + RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "⚠ Entrée invalide." + RESET);
        }
    }

    private static void menuReleveNotes() {
        System.out.println(BOLD + "--- Relevé de Notes ---" + RESET);
        String mat = promptInput("Matricule de l'étudiant");
        Etudiant etudiant = gestionActeurs.getEtudiantByMatricule(mat);
        if (etudiant == null) {
            System.out.println(RED + "⚠ Étudiant non trouvé." + RESET);
            return;
        }

        // Custom releve with colors could be integrated here, but using the service one for now
        System.out.println(YELLOW + "=================================================" + RESET);
        gestionSuivi.genererReleveNotes(etudiant);
    }

    private static void menuPlanifierSeance() {
        System.out.println(BOLD + "--- Planification d'une Séance ---" + RESET);
        try {
            String code = promptInput("Code du cours");
            Cours cours = gestionFormation.getCoursByCode(code);

            if (cours == null || cours.getGroupes().isEmpty()) {
                System.out.println(RED + "⚠ Cours non trouvé ou sans groupe." + RESET);
                return;
            }

            System.out.println(CYAN + "\nGroupes disponibles :" + RESET);
            for (int i = 0; i < cours.getGroupes().size(); i++) {
                System.out.println(" [" + (i + 1) + "] " + cours.getGroupes().get(i).getNom());
            }
            int indexGroupe = Integer.parseInt(promptInput("Numéro du groupe")) - 1;
            Groupe groupe = cours.getGroupes().get(indexGroupe);

            String idEns = promptInput("ID de l'enseignant");
            Enseignant enseignant = gestionActeurs.getEnseignantById(idEns);

            String idSalle = promptInput("ID de la salle");
            Salle salle = gestionFormation.getSalleById(idSalle);

            if (enseignant == null || salle == null) {
                System.out.println(RED + "⚠ Enseignant ou Salle non trouvé(e)." + RESET);
                return;
            }

            LocalDate date = LocalDate.parse(promptInput("Date (YYYY-MM-DD)"));
            LocalTime debut = LocalTime.parse(promptInput("Heure de début (HH:MM)"));
            LocalTime fin = LocalTime.parse(promptInput("Heure de fin (HH:MM)"));

            gestionPlanning.planifierSeance(groupe, enseignant, salle, date, debut, fin);
            System.out.println(GREEN + "✓ Séance planifiée avec succès." + RESET);

        } catch (DateTimeParseException e) {
            System.out.println(RED + "⚠ Format de date ou d'heure invalide." + RESET);
        } catch (ConflitHoraireException e) {
            System.out.println(RED + "⚠ Conflit de planification détecté." + RESET);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println(RED + "⚠ Saisie invalide." + RESET);
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
        Cours coursJava = new Cours("INFO-301", "Programmation Orientée Objet", "Concepts avancés", 40, prof1);
        Cours coursBD = new Cours("INFO-302", "Bases de Données", "Modélisation", 30, prof2);
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
