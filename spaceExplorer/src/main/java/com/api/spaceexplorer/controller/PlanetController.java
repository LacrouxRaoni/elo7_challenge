package com.api.spaceexplorer.controller;

import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.services.PlanetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/space-explorer")
public class PlanetController {


    @PostMapping
    public ResponseEntity postPlanet(@RequestBody PlanetDto planetDto){
        PlanetService planetService = new PlanetService();
        planetService.createPlanetObject(planetDto);
        return null;
    }



}
