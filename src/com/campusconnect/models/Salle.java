package com.campusconnect.models;

public class Salle {
    public enum TypeSalle { AMPHI, TP, COURS_CLASSIQUE }

    private String idSalle;
    private int capaciteAccueil;
    private TypeSalle type;

    public Salle(String idSalle, int capaciteAccueil, TypeSalle type) {
        this.idSalle = idSalle;
        this.capaciteAccueil = capaciteAccueil;
        this.type = type;
    }

    public String getIdSalle() { return idSalle; }
    public int getCapaciteAccueil() { return capaciteAccueil; }
    public TypeSalle getType() { return type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salle salle = (Salle) o;
        return java.util.Objects.equals(idSalle, salle.idSalle);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(idSalle);
    }
}
