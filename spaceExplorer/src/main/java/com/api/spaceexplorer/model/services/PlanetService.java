package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.repositories.PlanetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }


    private boolean checkIfPlanetExists(String planetName) {

        return planetRepository.existsByPlanetName(planetName);
    }

    private static void checkPlanetArgs(PlanetDto planet) {

        if (planet.getPlanetName().matches("\\W*")) {
            throw new PlanetException("Planet name should contain AlphaNumeric characters only");
        }
        if (planet.getWidth() <= 1 && planet.getHeight() <= 1){
            throw new PlanetException("Planet size must be greater than 0");
        }
    }

    public void createPlanetObject(PlanetDto planetDto){
        checkPlanetArgs(planetDto);
        PlanetEntity planetEntity = PlanetEntity.fromPlanetDto(planetDto);
        if (checkIfPlanetExists(planetEntity.getPlanetName()))
            throw new PlanetException("Planet already exists in Data Base");
        savePlanet(planetEntity);
    }

    public PlanetEntity getPlanetObject(PlanetDto planetDto) {

        var planetEntity =  planetRepository.findPlanetEntityByPlanetName(planetDto.getPlanetName());
        if (!planetEntity.isPresent())
            throw new PlanetException("Planet doesn't exist in Data Base");
        return planetEntity.get();
    }

    public void validAndDeletePlanet(PlanetDto planetDto) {
        var planetEntity =  planetRepository.findPlanetEntityByPlanetName(planetDto.getPlanetName());
        if (!planetEntity.isPresent())
            throw new PlanetException("Planet doesn't exist in Data Base");
        deletePlanet(planetEntity.get());
    }

    public String findAll() {
        StringBuilder sb = new StringBuilder();
        List<PlanetEntity> list = planetRepository.findAll();
        sb.append("PlanetInfo{\n");
        for (PlanetEntity c : list){
            sb.append("planetName= ");
            sb.append(c.getPlanetName() + "\n");
        }
        sb.append('}');
        return sb.toString();
    }
    @Transactional
    public void savePlanet(PlanetEntity planetEntity) {
        planetRepository.save(planetEntity);
    }

    @Transactional
    public void deletePlanet(PlanetEntity planetEntity) { planetRepository.delete(planetEntity); }


    public PlanetEntity modifyPlanetName(PlanetDto planetDto) {
        if (!checkIfPlanetExists(planetDto.getPlanetName()))
            throw new PlanetException("Planet doesn't exist in Data Base");
        if (checkIfPlanetExists(planetDto.getNewPlanetName()))
            throw new PlanetException("Planet already exists in Data Base");
        if (planetDto.getNewPlanetName().matches("\\W*")) {
            throw new PlanetException("Planet name should contain AlphaNumeric characters only");
        }
        Optional<PlanetEntity> planet = planetRepository.findPlanetEntityByPlanetName(planetDto.getPlanetName());
        planet.get().changePlanetName(planetDto.getNewPlanetName());
        savePlanet(planet.get());
        return planet.get();
    }
}
