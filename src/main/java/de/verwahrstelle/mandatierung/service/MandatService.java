package de.verwahrstelle.mandatierung.service;

import de.verwahrstelle.mandatierung.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MandatService {

    private final MandatRepository mandatRepository;
    private final AufgabeRepository aufgabeRepository;

    public MandatService(MandatRepository mandatRepository, AufgabeRepository aufgabeRepository) {
        this.mandatRepository = mandatRepository;
        this.aufgabeRepository = aufgabeRepository;
    }

    public Mandat neuesMandatErstellen(String fondsName, String kvg) {
        Mandat mandat = new Mandat(fondsName, kvg);

        // Meilenstein 1: Eröffnung Mandatierung
        mandat.addAufgabe(new Aufgabe(
                "Kundenanfrage erfassen und prüfen",
                Meilenstein.EROEFFNUNG_MANDATIERUNG,
                Rolle.CRM_MITARBEITER, 1));
        mandat.addAufgabe(new Aufgabe(
                "Fondsdaten und KVG-Informationen aufnehmen",
                Meilenstein.EROEFFNUNG_MANDATIERUNG,
                Rolle.CRM_MITARBEITER, 2));
        mandat.addAufgabe(new Aufgabe(
                "Mandatierungsantrag intern weiterleiten",
                Meilenstein.EROEFFNUNG_MANDATIERUNG,
                Rolle.CRM_MITARBEITER, 3));
        mandat.addAufgabe(new Aufgabe(
                "Genehmigung durch Leitung erteilen",
                Meilenstein.EROEFFNUNG_MANDATIERUNG,
                Rolle.LEITER_VERWAHRSTELLE, 4));

        // Meilenstein 2: Unterzeichnung Verwahrstellenvertrag
        mandat.addAufgabe(new Aufgabe(
                "Verwahrstellenvertrag erstellen",
                Meilenstein.UNTERZEICHNUNG_VERWAHRSTELLENVERTRAG,
                Rolle.CRM_MITARBEITER, 5));
        mandat.addAufgabe(new Aufgabe(
                "Vertragsprüfung und Freigabe",
                Meilenstein.UNTERZEICHNUNG_VERWAHRSTELLENVERTRAG,
                Rolle.LEITER_VERWAHRSTELLE, 6));
        mandat.addAufgabe(new Aufgabe(
                "Vertragsunterzeichnung durch beide Parteien",
                Meilenstein.UNTERZEICHNUNG_VERWAHRSTELLENVERTRAG,
                Rolle.LEITER_VERWAHRSTELLE, 7));

        // Meilenstein 3: Erfassung im Fondsbuchhaltungssystem
        mandat.addAufgabe(new Aufgabe(
                "Fondsstammdaten im System anlegen",
                Meilenstein.ERFASSUNG_FONDSBUCHHALTUNG,
                Rolle.SACHBEARBEITER_FONDSBUCHHALTUNG, 8));
        mandat.addAufgabe(new Aufgabe(
                "Depotverbindungen und Konten einrichten",
                Meilenstein.ERFASSUNG_FONDSBUCHHALTUNG,
                Rolle.SACHBEARBEITER_FONDSBUCHHALTUNG, 9));
        mandat.addAufgabe(new Aufgabe(
                "Abschlussprüfung und Freigabe im System",
                Meilenstein.ERFASSUNG_FONDSBUCHHALTUNG,
                Rolle.SACHBEARBEITER_FONDSBUCHHALTUNG, 10));

        return mandatRepository.save(mandat);
    }

    @Transactional(readOnly = true)
    public List<Mandat> alleMandateLaden() {
        return mandatRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Mandat mandatLaden(Long id) {
        return mandatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mandat nicht gefunden: " + id));
    }

    public Aufgabe aufgabeErledigen(Long aufgabeId) {
        Aufgabe aufgabe = aufgabeRepository.findById(aufgabeId)
                .orElseThrow(() -> new IllegalArgumentException("Aufgabe nicht gefunden: " + aufgabeId));

        aufgabe.setStatus(AufgabenStatus.ERLEDIGT);
        aufgabe.setErledigtAm(LocalDateTime.now());

        aktualisiereMeilenstein(aufgabe.getMandat());

        return aufgabeRepository.save(aufgabe);
    }

    public Aufgabe aufgabeStarten(Long aufgabeId) {
        Aufgabe aufgabe = aufgabeRepository.findById(aufgabeId)
                .orElseThrow(() -> new IllegalArgumentException("Aufgabe nicht gefunden: " + aufgabeId));

        aufgabe.setStatus(AufgabenStatus.IN_BEARBEITUNG);
        return aufgabeRepository.save(aufgabe);
    }

    private void aktualisiereMeilenstein(Mandat mandat) {
        List<Aufgabe> aufgaben = mandat.getAufgaben();

        boolean m1Erledigt = aufgaben.stream()
                .filter(a -> a.getMeilenstein() == Meilenstein.EROEFFNUNG_MANDATIERUNG)
                .allMatch(a -> a.getStatus() == AufgabenStatus.ERLEDIGT);

        boolean m2Erledigt = aufgaben.stream()
                .filter(a -> a.getMeilenstein() == Meilenstein.UNTERZEICHNUNG_VERWAHRSTELLENVERTRAG)
                .allMatch(a -> a.getStatus() == AufgabenStatus.ERLEDIGT);

        if (m2Erledigt) {
            mandat.setAktuellerMeilenstein(Meilenstein.ERFASSUNG_FONDSBUCHHALTUNG);
        } else if (m1Erledigt) {
            mandat.setAktuellerMeilenstein(Meilenstein.UNTERZEICHNUNG_VERWAHRSTELLENVERTRAG);
        } else {
            mandat.setAktuellerMeilenstein(Meilenstein.EROEFFNUNG_MANDATIERUNG);
        }

        mandatRepository.save(mandat);
    }
}
