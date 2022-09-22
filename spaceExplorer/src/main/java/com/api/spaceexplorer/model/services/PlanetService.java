package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    public void createPlanetObject(PlanetDto planetDto){
        checkPlanetArgs(planetDto);
        PlanetEntity planet = new PlanetEntity(planetDto.getPlanetName(),
                                                planetDto.getWidth(),
                                                planetDto.getHeight(),
                                   planetDto.getWidth() * planetDto.getHeight());
        System.out.println(planet.getPlanetName());
        System.out.println(planet.getExplorerAmount());
        System.out.println(planet.getHeight());
        System.out.println(planet.getWidth());

    }

    private static void checkPlanetArgs(PlanetDto planet) {
        if (planet.getWidth() < 1 || planet.getHeight() < 1){
            //execesao tamanho de planeta invalido
        }
    }

    public PlanetService() {
    }
}
