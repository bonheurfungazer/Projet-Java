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
    private static GestionActeurs gestionActeurs;
    private static GestionFormation gestionFormation;
    private static GestionSuiviAcademique gestionSuivi;
    private static GestionPlanning gestionPlanning;
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

    public static void lancer(GestionActeurs act, GestionFormation form, GestionSuiviAcademique suivi, GestionPlanning plan) {
        gestionActeurs = act;
        gestionFormation = form;
        gestionSuivi = suivi;
        gestionPlanning = plan;


        boolean continuer = true;
        clearScreen();

        while (continuer) {
            afficherEnTete();
            System.out.println(CYAN + "╭──────────────────────────────────────────────────╮" + RESET);
            System.out.println(CYAN + "│                 Menu Principal                   │" + RESET);
            System.out.println(CYAN + "├──────────────────────────────────────────────────┤" + RESET);
            System.out.println(CYAN + "│ " + RESET + "1. Gestion des Acteurs                           " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "2. Inscrire un étudiant à un cours               " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "3. Saisir une note pour un étudiant              " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "4. Générer le relevé de notes d'un étudiant      " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "5. Planifier une séance de cours                 " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "6. Afficher le planning                          " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "7. Gestion des Cours                             " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "8. Gestion des Salles                            " + CYAN + "│" + RESET);
            System.out.println(CYAN + "│ " + RESET + "0. Quitter l'application                         " + CYAN + "│" + RESET);
            System.out.println(CYAN + "╰──────────────────────────────────────────────────╯" + RESET);

            String choix = promptInput("Votre choix");
            clearScreen();

            try {
                switch (choix) {
                    case "1":
                        menuGestionActeurs();
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
                    case "7":
                        menuGestionCours();
                        break;
                    case "8":
                        menuGestionSalles();
                        break;
                    case "0":
                        continuer = false;
                        System.out.println(GREEN + "✓ Au revoir !" + RESET);
                        break;
                    default:
                        System.out.println(RED + "⚠ Choix invalide." + RESET);
                }
            } catch (Exception e) {
                System.out.println(RED + "⚠ Erreur: " + e.getMessage() + RESET);
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
    private static void menuGestionCours() {
        boolean retour = false;
        while (!retour) {
            System.out.println(BOLD + "--- Gestion des Cours ---" + RESET);
            System.out.println("1. Afficher tous les cours");
            System.out.println("2. Ajouter un cours");
            System.out.println("3. Modifier un cours");
            System.out.println("4. Supprimer un cours");
            System.out.println("0. Retour au menu principal");
            String choix = promptInput("Votre choix");
            clearScreen();
            switch (choix) {
                case "1": for (Cours c : gestionFormation.getCours()) System.out.println(c.getCode() + " - " + c.getIntitule()); break;
                case "2": menuAjouterCours(); break;
                case "3": menuModifierCours(); break;
                case "4": menuSupprimerCours(); break;
                case "0": retour = true; break;
                default: System.out.println(RED + "⚠ Choix invalide." + RESET);
            }
        }
    }

    private static void menuAjouterCours() {
        System.out.println(BOLD + "--- Ajouter un Cours ---" + RESET);
        String code = promptInput("Code");
        String intitule = promptInput("Intitulé");
        String description = promptInput("Description");
        int volHoraire = Integer.parseInt(promptInput("Volume horaire"));
        String idEnseignant = promptInput("ID Enseignant Responsable");
        Enseignant prof = gestionActeurs.getEnseignantById(idEnseignant);
        if (prof != null) {
            gestionFormation.ajouterCours(new Cours(code, intitule, description, volHoraire, prof));
            System.out.println(GREEN + "✓ Cours ajouté." + RESET);
        } else {
            System.out.println(RED + "⚠ Enseignant introuvable." + RESET);
        }
    }

    private static void menuModifierCours() {
        System.out.println(BOLD + "--- Modifier un Cours ---" + RESET);
        String code = promptInput("Code du cours");
        Cours c = gestionFormation.getCoursByCode(code);
        if (c == null) {
            System.out.println(RED + "⚠ Cours introuvable." + RESET);
            return;
        }
        c.setCode(promptInput("Nouveau code (" + c.getCode() + ")"));
        c.setIntitule(promptInput("Nouvel intitulé (" + c.getIntitule() + ")"));
        c.setDescription(promptInput("Nouvelle description (" + c.getDescription() + ")"));
        c.setVolumeHoraire(Integer.parseInt(promptInput("Nouveau vol. horaire (" + c.getVolumeHoraire() + ")")));
        System.out.println(GREEN + "✓ Cours modifié." + RESET);
    }

    private static void menuSupprimerCours() {
        System.out.println(BOLD + "--- Supprimer un Cours ---" + RESET);
        String code = promptInput("Code du cours");
        Cours c = gestionFormation.getCoursByCode(code);
        if (c == null) {
            System.out.println(RED + "⚠ Cours introuvable." + RESET);
            return;
        }
        gestionFormation.supprimerCours(c);
        System.out.println(GREEN + "✓ Cours supprimé." + RESET);
    }

    private static void menuGestionSalles() {
        boolean retour = false;
        while (!retour) {
            System.out.println(BOLD + "--- Gestion des Salles ---" + RESET);
            System.out.println("1. Afficher toutes les salles");
            System.out.println("2. Ajouter une salle");
            System.out.println("3. Modifier une salle");
            System.out.println("4. Supprimer une salle");
            System.out.println("0. Retour au menu principal");
            String choix = promptInput("Votre choix");
            clearScreen();
            switch (choix) {
                case "1": for (Salle s : gestionFormation.getSalles()) System.out.println(s.getIdSalle() + " - " + s.getType() + " (" + s.getCapaciteAccueil() + " places)"); break;
                case "2": menuAjouterSalle(); break;
                case "3": menuModifierSalle(); break;
                case "4": menuSupprimerSalle(); break;
                case "0": retour = true; break;
                default: System.out.println(RED + "⚠ Choix invalide." + RESET);
            }
        }
    }

    private static void menuAjouterSalle() {
        System.out.println(BOLD + "--- Ajouter une Salle ---" + RESET);
        String id = promptInput("ID de la salle");
        int cap = Integer.parseInt(promptInput("Capacité"));
        Salle.TypeSalle type = Salle.TypeSalle.valueOf(promptInput("Type (AMPHI, TP, COURS_CLASSIQUE)").toUpperCase());
        gestionFormation.ajouterSalle(new Salle(id, cap, type));
        System.out.println(GREEN + "✓ Salle ajoutée." + RESET);
    }

    private static void menuModifierSalle() {
        System.out.println(BOLD + "--- Modifier une Salle ---" + RESET);
        String id = promptInput("ID de la salle");
        Salle s = gestionFormation.getSalleById(id);
        if (s == null) {
            System.out.println(RED + "⚠ Salle introuvable." + RESET);
            return;
        }
        s.setCapaciteAccueil(Integer.parseInt(promptInput("Nouvelle capacité (" + s.getCapaciteAccueil() + ")")));
        System.out.println(GREEN + "✓ Salle modifiée." + RESET);
    }

    private static void menuSupprimerSalle() {
        System.out.println(BOLD + "--- Supprimer une Salle ---" + RESET);
        String id = promptInput("ID de la salle");
        Salle s = gestionFormation.getSalleById(id);
        if (s == null) {
            System.out.println(RED + "⚠ Salle introuvable." + RESET);
            return;
        }
        gestionFormation.supprimerSalle(s);
        System.out.println(GREEN + "✓ Salle supprimée." + RESET);
    }

    private static void menuGestionActeurs() {
        boolean retour = false;
        while (!retour) {
            System.out.println(BOLD + "--- Gestion des Acteurs ---" + RESET);
            System.out.println("1. Afficher tous les acteurs");
            System.out.println("2. Ajouter un étudiant");
            System.out.println("3. Modifier un étudiant");
            System.out.println("4. Supprimer un étudiant");
            System.out.println("5. Ajouter un enseignant");
            System.out.println("6. Modifier un enseignant");
            System.out.println("7. Supprimer un enseignant");
            System.out.println("0. Retour au menu principal");
            String choix = promptInput("Votre choix");
            clearScreen();
            switch (choix) {
                case "1": afficherActeursFiltres(); break;
                case "2": menuAjouterEtudiant(); break;
                case "3": menuModifierEtudiant(); break;
                case "4": menuSupprimerEtudiant(); break;
                case "5": menuAjouterEnseignant(); break;
                case "6": menuModifierEnseignant(); break;
                case "7": menuSupprimerEnseignant(); break;
                case "0": retour = true; break;
                default: System.out.println(RED + "⚠ Choix invalide." + RESET);
            }
        }
    }

    private static void menuAjouterEnseignant() {
        System.out.println(BOLD + "--- Ajouter un Enseignant ---" + RESET);
        String id = promptInput("ID unique");
        String nom = promptInput("Nom");
        String prenom = promptInput("Prénom");
        String email = promptInput("Email");
        String departement = promptInput("Département");
        Enseignant.Statut statut = Enseignant.Statut.valueOf(promptInput("Statut (PERMANENT ou VACATAIRE)").toUpperCase());
        Enseignant e = new Enseignant(id, nom, prenom, email, LocalDate.now(), statut, departement);
        gestionActeurs.ajouterPersonne(e);
        System.out.println(GREEN + "✓ Enseignant ajouté avec succès." + RESET);
    }

    private static void menuModifierEnseignant() {
        System.out.println(BOLD + "--- Modifier un Enseignant ---" + RESET);
        String id = promptInput("ID de l'enseignant");
        Enseignant e = gestionActeurs.getEnseignantById(id);
        if (e == null) {
            System.out.println(RED + "⚠ Enseignant non trouvé." + RESET);
            return;
        }
        e.setNom(promptInput("Nouveau nom (" + e.getNom() + ")"));
        e.setPrenom(promptInput("Nouveau prénom (" + e.getPrenom() + ")"));
        e.setEmail(promptInput("Nouvel email (" + e.getEmail() + ")"));
        e.setDepartement(promptInput("Nouveau département (" + e.getDepartement() + ")"));
        System.out.println(GREEN + "✓ Enseignant modifié avec succès." + RESET);
    }

    private static void menuSupprimerEnseignant() {
        System.out.println(BOLD + "--- Supprimer un Enseignant ---" + RESET);
        String id = promptInput("ID de l'enseignant");
        Enseignant e = gestionActeurs.getEnseignantById(id);
        if (e == null) {
            System.out.println(RED + "⚠ Enseignant non trouvé." + RESET);
            return;
        }
        gestionActeurs.supprimerPersonne(e);
        System.out.println(GREEN + "✓ Enseignant supprimé avec succès." + RESET);
    }

    private static void menuAjouterEtudiant() {
        System.out.println(BOLD + "--- Ajouter un Étudiant ---" + RESET);
        String id = promptInput("ID unique");
        String nom = promptInput("Nom");
        String prenom = promptInput("Prénom");
        String email = promptInput("Email");
        String matricule = promptInput("Matricule");
        String annee = promptInput("Année (ex: L3)");
        String filiere = promptInput("Filière");
        Etudiant e = new Etudiant(id, nom, prenom, email, LocalDate.now(), matricule, annee, filiere);
        gestionActeurs.ajouterPersonne(e);
        System.out.println(GREEN + "✓ Étudiant ajouté avec succès." + RESET);
    }

    private static void menuModifierEtudiant() {
        System.out.println(BOLD + "--- Modifier un Étudiant ---" + RESET);
        String matricule = promptInput("Matricule de l'étudiant");
        Etudiant e = gestionActeurs.getEtudiantByMatricule(matricule);
        if (e == null) {
            System.out.println(RED + "⚠ Étudiant non trouvé." + RESET);
            return;
        }
        e.setNom(promptInput("Nouveau nom (" + e.getNom() + ")"));
        e.setPrenom(promptInput("Nouveau prénom (" + e.getPrenom() + ")"));
        e.setEmail(promptInput("Nouvel email (" + e.getEmail() + ")"));
        e.setAnneeEtude(promptInput("Nouvelle année (" + e.getAnneeEtude() + ")"));
        e.setFiliere(promptInput("Nouvelle filière (" + e.getFiliere() + ")"));
        System.out.println(GREEN + "✓ Étudiant modifié avec succès." + RESET);
    }

    private static void menuSupprimerEtudiant() {
        System.out.println(BOLD + "--- Supprimer un Étudiant ---" + RESET);
        String matricule = promptInput("Matricule de l'étudiant");
        Etudiant e = gestionActeurs.getEtudiantByMatricule(matricule);
        if (e == null) {
            System.out.println(RED + "⚠ Étudiant non trouvé." + RESET);
            return;
        }
        gestionActeurs.supprimerPersonne(e);
        System.out.println(GREEN + "✓ Étudiant supprimé avec succès." + RESET);
    }


    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
            System.out.println(RED + "⚠ Erreur de capacité : " + e.getMessage() + RESET);
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
            System.out.println(RED + "⚠ Erreur : " + e.getMessage() + RESET);
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
            System.out.println(RED + "⚠ Erreur de planification : " + e.getMessage() + RESET);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println(RED + "⚠ Saisie invalide." + RESET);
        }
    }
}
