package de.verwahrstelle.mandatierung.model;

public enum Rolle {

    CRM_MITARBEITER("CRM-Mitarbeiter Verwahrstelle"),
    LEITER_VERWAHRSTELLE("Leiter Verwahrstelle"),
    SACHBEARBEITER_FONDSBUCHHALTUNG("Sachbearbeiter Fondsbuchhaltungssystem");

    private final String bezeichnung;

    Rolle(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
