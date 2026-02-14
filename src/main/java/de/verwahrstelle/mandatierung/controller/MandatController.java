package de.verwahrstelle.mandatierung.controller;

import de.verwahrstelle.mandatierung.model.Meilenstein;
import de.verwahrstelle.mandatierung.model.Mandat;
import de.verwahrstelle.mandatierung.service.MandatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MandatController {

    private final MandatService mandatService;

    public MandatController(MandatService mandatService) {
        this.mandatService = mandatService;
    }

    @GetMapping("/")
    public String uebersicht(Model model) {
        model.addAttribute("mandate", mandatService.alleMandateLaden());
        return "uebersicht";
    }

    @GetMapping("/mandat/neu")
    public String neuesMandatFormular() {
        return "neues-mandat";
    }

    @PostMapping("/mandat/neu")
    public String mandatErstellen(@RequestParam String fondsName,
                                  @RequestParam String kvg,
                                  RedirectAttributes redirectAttributes) {
        Mandat mandat = mandatService.neuesMandatErstellen(fondsName, kvg);
        redirectAttributes.addFlashAttribute("erfolg",
                "Mandat für '" + mandat.getFondsName() + "' wurde erfolgreich angelegt.");
        return "redirect:/mandat/" + mandat.getId();
    }

    @GetMapping("/mandat/{id}")
    public String mandatDetail(@PathVariable Long id, Model model) {
        Mandat mandat = mandatService.mandatLaden(id);
        model.addAttribute("mandat", mandat);
        model.addAttribute("meilensteine", Meilenstein.values());
        return "mandat-detail";
    }

    @PostMapping("/aufgabe/{id}/erledigen")
    public String aufgabeErledigen(@PathVariable Long id) {
        var aufgabe = mandatService.aufgabeErledigen(id);
        return "redirect:/mandat/" + aufgabe.getMandat().getId();
    }

    @PostMapping("/aufgabe/{id}/starten")
    public String aufgabeStarten(@PathVariable Long id) {
        var aufgabe = mandatService.aufgabeStarten(id);
        return "redirect:/mandat/" + aufgabe.getMandat().getId();
    }
}
