package com.campusconnect.services;

import com.campusconnect.models.Cours;
import com.campusconnect.models.Groupe;
import com.campusconnect.models.Salle;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GestionFormation {
    private Set<Cours> coursList;
    private Set<Salle> salles;

    public GestionFormation() {
        this.coursList = new LinkedHashSet<>();
        this.salles = new LinkedHashSet<>();
    }

    public void ajouterCours(Cours cours) {
        coursList.add(cours);
    }

    public void ajouterSalle(Salle salle) {
        salles.add(salle);
    }

    public List<Cours> getCours() { return new ArrayList<>(coursList); }
    public List<Salle> getSalles() { return new ArrayList<>(salles); }

    public Cours getCoursByCode(String code) {
        for (Cours c : coursList) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }

    public Salle getSalleById(String idSalle) {
        for (Salle s : salles) {
            if (s.getIdSalle().equals(idSalle)) {
                return s;
            }
        }
        return null;
    }
}
