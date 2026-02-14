package de.verwahrstelle.mandatierung.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mandate")
public class Mandat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fondsName;

    private String kapitalverwaltungsgesellschaft;

    private LocalDateTime erstelltAm;

    @Enumerated(EnumType.STRING)
    private Meilenstein aktuellerMeilenstein;

    @OneToMany(mappedBy = "mandat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("reihenfolge ASC")
    private List<Aufgabe> aufgaben = new ArrayList<>();

    public Mandat() {}

    public Mandat(String fondsName, String kapitalverwaltungsgesellschaft) {
        this.fondsName = fondsName;
        this.kapitalverwaltungsgesellschaft = kapitalverwaltungsgesellschaft;
        this.erstelltAm = LocalDateTime.now();
        this.aktuellerMeilenstein = Meilenstein.EROEFFNUNG_MANDATIERUNG;
    }

    public void addAufgabe(Aufgabe aufgabe) {
        aufgaben.add(aufgabe);
        aufgabe.setMandat(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFondsName() { return fondsName; }
    public void setFondsName(String fondsName) { this.fondsName = fondsName; }

    public String getKapitalverwaltungsgesellschaft() { return kapitalverwaltungsgesellschaft; }
    public void setKapitalverwaltungsgesellschaft(String kvg) { this.kapitalverwaltungsgesellschaft = kvg; }

    public LocalDateTime getErstelltAm() { return erstelltAm; }
    public void setErstelltAm(LocalDateTime erstelltAm) { this.erstelltAm = erstelltAm; }

    public Meilenstein getAktuellerMeilenstein() { return aktuellerMeilenstein; }
    public void setAktuellerMeilenstein(Meilenstein aktuellerMeilenstein) { this.aktuellerMeilenstein = aktuellerMeilenstein; }

    public List<Aufgabe> getAufgaben() { return aufgaben; }
    public void setAufgaben(List<Aufgabe> aufgaben) { this.aufgaben = aufgaben; }

    public int getFortschrittProzent() {
        if (aufgaben.isEmpty()) return 0;
        long erledigt = aufgaben.stream()
                .filter(a -> a.getStatus() == AufgabenStatus.ERLEDIGT)
                .count();
        return (int) (erledigt * 100 / aufgaben.size());
    }
}
