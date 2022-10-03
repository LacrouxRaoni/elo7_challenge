package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
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

    public Optional<PlanetEntity> locatePlanet(String planetName) {
        return planetRepository.findPlanetEntityByPlanetName(planetName);
    }

    private static void checkPostArgs(PlanetDto planet) {
        if (planet.getPlanetName().matches("\\W*")) {
            throw new PlanetException("Planet name should contain AlphaNumeric characters only");
        }
        if (planet.getWidth() <= 1 || planet.getHeight() <= 1){
            throw new PlanetException("Planet size must be greater than 0");
        }
    }

    public void validatePlanetAndSaveInDb(PlanetDto planetDto){
        checkPostArgs(planetDto);
        var planetEntity = PlanetEntity.fromPlanetDto(planetDto);
        if (locatePlanet(planetEntity.getPlanetName()).isPresent())
            throw new PlanetException("Planet already exists in Data Base");
        savePlanet(planetEntity);
    }

    public String getAll() {
        StringBuilder sb = new StringBuilder();
        var list = planetRepository.findAll();
        sb.append("PlanetInfo{\n");
        for (PlanetEntity c : list){
            sb.append("planetName= ");
            sb.append(c.getPlanetName()).append("\n");
        }
        sb.append('}');
        return sb.toString();
    }

    public PlanetEntity getPlanetObject(PlanetDto planetDto) {
        var planetEntity =  locatePlanet(planetDto.getPlanetName());
        if (planetEntity.isEmpty())
            throw new PlanetException("Planet doesn't exist in Data Base");
        return planetEntity.get();
    }


    private void validateArgs(PlanetDto planetDto) {
        if (locatePlanet(planetDto.getPlanetName()).isEmpty())
            throw new PlanetException("Planet doesn't exist in Data Base");
        if (locatePlanet(planetDto.getNewPlanetName()).isPresent())
            throw new PlanetException("Planet already exists in Data Base");
        if (planetDto.getNewPlanetName().matches("\\W*"))
            throw new PlanetException("Planet name should contain AlphaNumeric characters only");
    }

    public PlanetEntity checkArgsToModifyPlanetName(PlanetDto planetDto) {
        validateArgs(planetDto);
        var planet = locatePlanet(planetDto.getPlanetName());
        if (planet.isEmpty())
            throw new PlanetException("Planet doesn't exist in Data Base");
        planet.get().changePlanetName(planetDto.getNewPlanetName());
        savePlanet(planet.get());
        return planet.get();
    }


    private static void checkInExplorerList(int i, int j, List<ExplorerEntity> explorers, String[][] planet) {
        for (ExplorerEntity c : explorers){
            if (i == c.getX() && j == c.getY()){
                planet[i][j] = String.valueOf('s');
                break ;
            }
        }
    }

    public static String[][] drawPlanet(ExplorerEntity explorer) {
        var planetData = explorer.getPlanet();
        var explorers = planetData.getExplorer();
        String [][]planet = new String[planetData.getHeight()][planetData.getWidth()];
        for (int i = 0; i < planetData.getHeight(); i++){
            for (int j = 0; j < planetData.getWidth(); j++){
                planet[i][j] = String.valueOf('0');
                if (i == explorer.getX() && j == explorer.getY())
                    planet[i][j] = String.valueOf('x');
                else {
                    checkInExplorerList(i, j, explorers, planet);
                }
            }
        }
        return planet;
    }

    public void planetCapacity(PlanetEntity planetEntity) {
        if (planetEntity.getExplorerAmount() == planetEntity.getExplorerAmountLimit()) {
            throw new ExplorerException("Planet is full!");
        }
    }

    public void validAndDeletePlanet(PlanetDto planetDto) {
        var planetEntity =  locatePlanet(planetDto.getPlanetName());
        if (planetEntity.isEmpty())
            throw new PlanetException("Planet doesn't exist in Data Base");
        deletePlanet(planetEntity.get());
    }

    @Transactional
    public void savePlanet(PlanetEntity planetEntity) {
        planetRepository.save(planetEntity);
    }

    @Transactional
    public void deletePlanet(PlanetEntity planetEntity) {
        planetRepository.delete(planetEntity);
    }

}
