package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.Einladung;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.service.EinladungService;
import com.Kernighan.TerminKalender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/einladungen")  // Basis-URL for the Controller
public class EinladungWebController {

    private final EinladungService einladungService;
    private final UserService userService;

    @Autowired
    public EinladungWebController(EinladungService einladungService, UserService userService) {
        this.einladungService = einladungService;
        this.userService = userService;
    }

    @GetMapping("")
    public String meineEinladungen(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        List<Einladung> einladungen = einladungService.getEinladungenFürBenutzer(user.getId());
        model.addAttribute("einladungen", einladungen);

        return "einladungen";
    }

    @PostMapping("/akzeptieren/{id}")
    public String akzeptiereEinladung(@PathVariable Long id) {
        einladungService.akzeptiereEinladung(id);
        return "redirect:/einladungen";
    }

    @PostMapping("/ablehnen/{id}")
    public String ablehnenEinladung(@PathVariable Long id) {
        einladungService.ablehnenEinladung(id);
        return "redirect:/einladungen";
    }
}
