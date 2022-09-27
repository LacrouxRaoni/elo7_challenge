package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.repositories.PlanetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PlanetService {

    final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public void createPlanetObject(PlanetDto planetDto){
        checkPlanetArgs(planetDto);
        PlanetEntity planetEntity = PlanetEntity.fromPlanetDto(planetDto);
        if (checkIfPlanetExists(planetEntity.getPlanetName()))
            throw new PlanetException("Planet already exists in Data Base");
        savePlanet(planetEntity);
    }
    private static void checkPlanetArgs(PlanetDto planet) {

         if (planet.getPlanetName().matches("\\W*")) {
            throw new PlanetException("Planet name should contains AlphaNumeric characters only");
         }
        if (planet.getWidth() <= 1 && planet.getHeight() <= 1){
            throw new PlanetException("Planet size must be greater than 0");
        }
    }

    private boolean checkIfPlanetExists(String planetName) {
        return planetRepository.existsByPlanetName(planetName);
    }

    @Transactional
    public void savePlanet(PlanetEntity planetEntity) {
        planetRepository.save(planetEntity);
    }
}
