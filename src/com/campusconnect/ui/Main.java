package com.campusconnect.ui;

import com.campusconnect.models.*;
import com.campusconnect.services.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        GestionActeurs gestionActeurs = new GestionActeurs();
        GestionFormation gestionFormation = new GestionFormation();
        GestionSuiviAcademique gestionSuivi = new GestionSuiviAcademique();
        GestionPlanning gestionPlanning = new GestionPlanning();

        initialiserDonnees(gestionActeurs, gestionFormation, gestionSuivi);

        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            // Lancer la version console
            CampusApp.lancer(gestionActeurs, gestionFormation, gestionSuivi, gestionPlanning);
        } else {
            // Lancer la version GUI
            javax.swing.SwingUtilities.invokeLater(() -> {
                DashboardGUI gui = new DashboardGUI(gestionActeurs, gestionFormation, gestionSuivi, gestionPlanning);
                gui.setVisible(true);
            });
        }
    }

    private static void initialiserDonnees(GestionActeurs gestionActeurs, GestionFormation gestionFormation, GestionSuiviAcademique gestionSuivi) {
        Enseignant prof1 = new Enseignant("E001", "Dupont", "Jean", "jean.dupont@campus.com", LocalDate.of(1980, 5, 12), Enseignant.Statut.PERMANENT, "Informatique");
        Enseignant prof2 = new Enseignant("E002", "Martin", "Sophie", "sophie.martin@campus.com", LocalDate.of(1985, 8, 22), Enseignant.Statut.VACATAIRE, "Mathématiques");
        gestionActeurs.ajouterPersonne(prof1);
        gestionActeurs.ajouterPersonne(prof2);

        Etudiant etu1 = new Etudiant("S001", "Durand", "Alice", "alice.durand@campus.com", LocalDate.of(2001, 3, 15), "MAT123", "L3", "Informatique");
        Etudiant etu2 = new Etudiant("S002", "Lefebvre", "Marc", "marc.lefebvre@campus.com", LocalDate.of(2000, 11, 2), "MAT456", "L3", "Informatique");
        gestionActeurs.ajouterPersonne(etu1);
        gestionActeurs.ajouterPersonne(etu2);

        Cours coursJava = new Cours("INFO-301", "Programmation Orientée Objet", "Concepts avancés", 40, prof1);
        Cours coursBD = new Cours("INFO-302", "Bases de Données", "Modélisation", 30, prof2);
        gestionFormation.ajouterCours(coursJava);
        gestionFormation.ajouterCours(coursBD);

        Groupe cmJava = new Groupe("CM", coursJava, 100, prof1);
        Salle amphiA = new Salle("Amphi A", 150, Salle.TypeSalle.AMPHI);
        gestionFormation.ajouterSalle(amphiA);

        try {
            gestionSuivi.inscrireEtudiant(etu1, cmJava);
            gestionSuivi.inscrireEtudiant(etu2, cmJava);
        } catch(Exception e) {}
    }
}
