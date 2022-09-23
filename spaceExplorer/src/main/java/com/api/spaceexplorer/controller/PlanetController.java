package com.api.spaceexplorer.controller;

import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.services.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/space-explorer/planet")
public class PlanetController {

    private final PlanetService planetService;
    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity postPlanet(@RequestBody PlanetDto planetDto){
        if (!planetService.createPlanetObject(planetDto))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conflict: Invalid argument");
        else
            return ResponseEntity.status(HttpStatus.CREATED).body("Ok");
    }
}
