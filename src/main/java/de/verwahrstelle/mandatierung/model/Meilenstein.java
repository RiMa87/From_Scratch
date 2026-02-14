package de.verwahrstelle.mandatierung.model;

public enum Meilenstein {

    EROEFFNUNG_MANDATIERUNG("Eröffnung Mandatierung", 1),
    UNTERZEICHNUNG_VERWAHRSTELLENVERTRAG("Unterzeichnung Verwahrstellenvertrag", 2),
    ERFASSUNG_FONDSBUCHHALTUNG("Erfassung des Fonds im Fondsbuchhaltungssystem", 3);

    private final String bezeichnung;
    private final int reihenfolge;

    Meilenstein(String bezeichnung, int reihenfolge) {
        this.bezeichnung = bezeichnung;
        this.reihenfolge = reihenfolge;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public int getReihenfolge() {
        return reihenfolge;
    }
}
