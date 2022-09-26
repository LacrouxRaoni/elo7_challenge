package com.api.spaceexplorer.model.services;

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

    public boolean createPlanetObject(PlanetDto planetDto){
        if (!checkPlanetArgs(planetDto)){
            return false;
        }
        PlanetEntity planetEntity = PlanetEntity.fromPlanetDto(planetDto);
        savePlanet(planetEntity);
        return true;
    }

    private static boolean checkPlanetArgs(PlanetDto planet) {

         if (planet.getPlanetName().matches("\\W*")) {
            return false;
         }
        return planet.getWidth() >= 1 && planet.getHeight() >= 1;
    }

    @Transactional
    public void savePlanet(PlanetEntity planetEntity) {
        planetRepository.save(planetEntity);
    }
}
