package com.campusconnect.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {
    private Groupe groupe;
    private Enseignant enseignant;
    private Salle salle;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Seance(Groupe groupe, Enseignant enseignant, Salle salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.groupe = groupe;
        this.enseignant = enseignant;
        this.salle = salle;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public Groupe getGroupe() { return groupe; }
    public Enseignant getEnseignant() { return enseignant; }
    public Salle getSalle() { return salle; }
    public LocalDate getDate() { return date; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }

    public void setGroupe(Groupe groupe) { this.groupe = groupe; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }
    public void setSalle(Salle salle) { this.salle = salle; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
}
