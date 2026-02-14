package de.verwahrstelle.mandatierung.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aufgaben")
public class Aufgabe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bezeichnung;

    @Enumerated(EnumType.STRING)
    private Meilenstein meilenstein;

    @Enumerated(EnumType.STRING)
    private Rolle zustaendigeRolle;

    @Enumerated(EnumType.STRING)
    private AufgabenStatus status;

    private int reihenfolge;

    private LocalDateTime erledigtAm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mandat_id")
    private Mandat mandat;

    public Aufgabe() {}

    public Aufgabe(String bezeichnung, Meilenstein meilenstein, Rolle zustaendigeRolle, int reihenfolge) {
        this.bezeichnung = bezeichnung;
        this.meilenstein = meilenstein;
        this.zustaendigeRolle = zustaendigeRolle;
        this.reihenfolge = reihenfolge;
        this.status = AufgabenStatus.OFFEN;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBezeichnung() { return bezeichnung; }
    public void setBezeichnung(String bezeichnung) { this.bezeichnung = bezeichnung; }

    public Meilenstein getMeilenstein() { return meilenstein; }
    public void setMeilenstein(Meilenstein meilenstein) { this.meilenstein = meilenstein; }

    public Rolle getZustaendigeRolle() { return zustaendigeRolle; }
    public void setZustaendigeRolle(Rolle zustaendigeRolle) { this.zustaendigeRolle = zustaendigeRolle; }

    public AufgabenStatus getStatus() { return status; }
    public void setStatus(AufgabenStatus status) { this.status = status; }

    public int getReihenfolge() { return reihenfolge; }
    public void setReihenfolge(int reihenfolge) { this.reihenfolge = reihenfolge; }

    public LocalDateTime getErledigtAm() { return erledigtAm; }
    public void setErledigtAm(LocalDateTime erledigtAm) { this.erledigtAm = erledigtAm; }

    public Mandat getMandat() { return mandat; }
    public void setMandat(Mandat mandat) { this.mandat = mandat; }
}
