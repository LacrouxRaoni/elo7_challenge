package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
public class PlanetServicesTest {

    @Autowired
    private PlanetService planetService;


    @Test
    @DisplayName("planet name with no alphanumeric")
    public void args_to_creat_a_planet_must_be_alphanumeric(){
        PlanetDto dto = new PlanetDto("$", 12, 12);
        try {
            planetService.validatePlanetAndSaveInDb(dto);
        } catch (PlanetException e){
            assertEquals("Planet name should contain AlphaNumeric characters only", e.getMessage());
            return ;
        }
        fail("expected: Planet name should contain AlphaNumeric characters only");
    }

    @Test
    @DisplayName("planet name with size less 1")
    public void args_to_creat_a_planet_must_greater_than_0(){
        PlanetDto dto = new PlanetDto("test", 0, 12);
        try {
            planetService.validatePlanetAndSaveInDb(dto);
        } catch (PlanetException e){
            assertEquals("Planet size must be greater than 0", e.getMessage());
            return ;
        }
        fail("expected: Planet size must be greater than 0");
    }

    @Test
    @DisplayName("Check Planet Existence in DB")
    public void planet_must_exist_in_db(){
            PlanetDto dto = new PlanetDto("test", 12, 12);
            planetService.validatePlanetAndSaveInDb(dto);
            PlanetDto dto1 = new PlanetDto("test", 12, 12);
        try {
            planetService.validatePlanetAndSaveInDb(dto1);
        } catch (PlanetException e){
            assertEquals("Planet already exists in Data Base", e.getMessage());
            return ;
        }
        fail("expected: Planet already exists in Data Base");
    }

    @Test
    @DisplayName("Try Delete Planet from  DB")
    public void exception_must_be_thrown_if_a_planet_do_not_exist_when_it_try_to_delete_from_db(){
        PlanetDto dto = new PlanetDto("test", 12, 12);
        if (planetService.locatePlanet("test").isPresent()) {
            planetService.validAndDeletePlanet(dto);
        }
        try {
            planetService.validAndDeletePlanet(dto);
        } catch (PlanetException e){
            assertEquals("Planet doesn't exist in Data Base", e.getMessage());
            return ;
        }
        fail("expected: Planet doesn't exist in Data Base");
    }

    @Test
    @DisplayName("Get all planets")
    public void get_method_all_must_return_string(){
        PlanetDto dto = new PlanetDto("test", 12, 12);
        if (planetService.locatePlanet("test").isEmpty())
            planetService.validatePlanetAndSaveInDb(dto);
        System.out.println(planetService.getAll().toString());
    }

    @Test
    @DisplayName("Return exception in get one Planet")
    public void get_method_one_planet_must_return_exception_case_planet_do_not_exist(){
        PlanetDto dto = new PlanetDto("test", 12, 12);
        if (planetService.locatePlanet("test").isPresent()){
            planetService.validAndDeletePlanet(dto);
        }
        try {
            System.out.println(planetService.getPlanetObject(dto).toString());
        } catch (PlanetException e){
            assertEquals("Planet doesn't exist in Data Base", e.getMessage());
            return ;
        }
        fail("expected: Planet doesn't exist in Data Base");
    }

    @Test
    @DisplayName("Return planet data in Json format")
    public void get_method_one_planet_must_return_planet_entity(){
        PlanetDto dto = new PlanetDto("test", 12, 12);
        planetService.validatePlanetAndSaveInDb(dto);
        PlanetEntity entity = planetService.getPlanetObject(dto);
        if (entity.getPlanetName().isEmpty()){
            fail("expected: Entity not empty");
        }
    }

    @Test
    @DisplayName("put method args validation planet does not exist")
    public void put_method_change_planet_name_validation_must_return_invalid_planet(){
        PlanetDto dto = new PlanetDto("test", "test1");
        try {
            planetService.checkArgsToModifyPlanetName(dto);
        } catch (PlanetException e){
            assertEquals("Planet doesn't exist in Data Base", e.getMessage());
            return ;
        }
        fail("expected: Planet doesn't exist in Data Base");
    }

    @Test
    @DisplayName("put method args validation planet does exist")
    public void put_method_change_planet_name_validation_must_return_planet_exist_in_db(){
        PlanetDto dto1 = new PlanetDto("test", 5, 5);
        if (planetService.locatePlanet("test").isEmpty()){
            planetService.validatePlanetAndSaveInDb(dto1);
        }
        PlanetDto dto = new PlanetDto("test", "test");
        try {
            planetService.checkArgsToModifyPlanetName(dto);
        } catch (PlanetException e){
            assertEquals("Planet already exists in Data Base", e.getMessage());
            return ;
        }
        fail("expected: Planet already exists in Data Base");
    }

    @Test
    @DisplayName("put method args validation invalid new planet name")
    public void put_method_change_planet_name_validation_must_return_invalid_name(){
        PlanetDto dto1 = new PlanetDto("test", 5, 5);
        if (planetService.locatePlanet("test").isEmpty()){
            planetService.validatePlanetAndSaveInDb(dto1);
        }
        PlanetDto dto = new PlanetDto("test", "$");
        try {
            planetService.checkArgsToModifyPlanetName(dto);
        } catch (PlanetException e){
            assertEquals("Planet name should contain AlphaNumeric characters only", e.getMessage());
            return ;
        }
        fail("expected: Planet name should contain AlphaNumeric characters only");
    }

    @Test
    @DisplayName("put method args validation invalid new planet name")
    public void put_method_change_planet_name_must_change_planet_name(){
        PlanetDto dto1 = new PlanetDto("test", 5, 5);
        if (planetService.locatePlanet("test").isEmpty()){
            planetService.validatePlanetAndSaveInDb(dto1);
        }
        PlanetDto dto = new PlanetDto("test", "test1");
        PlanetEntity planet = planetService.checkArgsToModifyPlanetName(dto);
        assertEquals("test1", planet.getPlanetName());
    }

}
