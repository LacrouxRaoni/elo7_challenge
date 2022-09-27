package com.api.spaceexplorer.controller;

import com.api.spaceexplorer.controller.exceptions.PlanetException;
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

    @GetMapping
    public ResponseEntity getPlanet(@RequestBody PlanetDto planetDto){
        try {
            planetService.getPlanetObject(planetDto);
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (PlanetException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity postPlanet(@RequestBody PlanetDto planetDto){
        try {
            planetService.createPlanetObject(planetDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Planet created with success");
        } catch (PlanetException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity deletePlanet(@RequestBody PlanetDto planetDto){
        try {
            planetService.validAndDeletePlanet(planetDto);
            return ResponseEntity.status(HttpStatus.OK).body("Planet deleted with success");
        } catch (PlanetException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}
