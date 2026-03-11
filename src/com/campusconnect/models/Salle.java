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

    public void setIdSalle(String idSalle) { this.idSalle = idSalle; }
    public void setCapaciteAccueil(int capaciteAccueil) { this.capaciteAccueil = capaciteAccueil; }
    public void setType(TypeSalle type) { this.type = type; }
}
