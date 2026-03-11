package com.campusconnect.models;

import java.util.ArrayList;
import java.util.List;

public class Cours {
    private String code;
    private String intitule;
    private String description;
    private int volumeHoraire;
    private Enseignant responsable;
    private List<Groupe> groupes;

    public Cours(String code, String intitule, String description, int volumeHoraire, Enseignant responsable) {
        this.code = code;
        this.intitule = intitule;
        this.description = description;
        this.volumeHoraire = volumeHoraire;
        this.responsable = responsable;
        this.groupes = new ArrayList<>();
    }

    public String getCode() { return code; }
    public String getIntitule() { return intitule; }
    public String getDescription() { return description; }
    public int getVolumeHoraire() { return volumeHoraire; }
    public Enseignant getResponsable() { return responsable; }
    public List<Groupe> getGroupes() { return groupes; }

    public void ajouterGroupe(Groupe groupe) {
        if (!this.groupes.contains(groupe)) {
            this.groupes.add(groupe);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cours cours = (Cours) o;
        return java.util.Objects.equals(code, cours.code);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(code);
    }
}
