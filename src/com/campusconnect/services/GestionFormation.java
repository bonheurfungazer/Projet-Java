package com.campusconnect.services;

import com.campusconnect.models.Cours;
import com.campusconnect.models.Salle;

import java.util.ArrayList;
import java.util.List;

public class GestionFormation {
    private List<Cours> coursList;
    private List<Salle> salles;

    public GestionFormation() {
        this.coursList = new ArrayList<>();
        this.salles = new ArrayList<>();
    }

    public void ajouterCours(Cours cours) {
        if (!coursList.contains(cours)) {
            coursList.add(cours);
        }
    }

    public void ajouterSalle(Salle salle) {
        if (!salles.contains(salle)) {
            salles.add(salle);
        }
    }

    public List<Cours> getCours() { return coursList; }
    public List<Salle> getSalles() { return salles; }

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
