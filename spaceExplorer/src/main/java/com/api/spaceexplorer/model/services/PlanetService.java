package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.repositories.PlanetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    public void getPlanetObject(PlanetDto planetDto) {

        var planetEntity =  planetRepository.findPlanetEntityByPlanetName(planetDto.getPlanetName());
        if (!planetEntity.isPresent())
            throw new PlanetException("Planet doesn't exists in Data Base");
        System.out.println(planetEntity.get().toString());
        for (ExplorerEntity c : planetEntity.get().getExplorer()){
            System.out.println(c.getExplorerName());
        }
    }
    public void validAndDeletePlanet(PlanetDto planetDto) {
        var planetEntity =  planetRepository.findPlanetEntityByPlanetName(planetDto.getPlanetName());
        if (!planetEntity.isPresent())
            throw new PlanetException("Planet doesn't exists in Data Base");
        deletePlanet(planetEntity.get());
    }

    private boolean checkIfPlanetExists(String planetName) {

        return planetRepository.existsByPlanetName(planetName);
    }

    @Transactional
    public void savePlanet(PlanetEntity planetEntity) {
        planetRepository.save(planetEntity);
    }

    @Transactional
    public void deletePlanet(PlanetEntity planetEntity) { planetRepository.delete(planetEntity); }


}
