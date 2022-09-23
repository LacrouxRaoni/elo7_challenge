package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import org.springframework.stereotype.Service;

import java.text.StringCharacterIterator;
import java.util.regex.Pattern;

@Service
public class PlanetService {

    public boolean createPlanetObject(PlanetDto planetDto){
        if (!checkPlanetArgs(planetDto)){
            return false;
        }
        PlanetEntity planetEntity = PlanetEntity.fromPlanetDto(planetDto);
        return true;
    }


    private static boolean checkPlanetArgs(PlanetDto planet) {

         if (planet.getPlanetName().matches("\\W*")) {
            return false;
         }
        return planet.getWidth() >= 1 && planet.getHeight() >= 1;
    }
}
