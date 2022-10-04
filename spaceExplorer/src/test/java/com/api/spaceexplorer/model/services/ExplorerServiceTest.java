package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import org.junit.jupiter.api.BeforeEach;
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
public class ExplorerServiceTest {

    @Autowired
    private ExplorerService explorerService;
    @Autowired
    private PlanetService planetService;

    private void create_planet(){
        PlanetDto pDto = new PlanetDto("planet_test", 5 , 5);
        if (planetService.locatePlanet("planet_test").isEmpty()) {
            planetService.validatePlanetAndSaveInDb(pDto);
        }
    }

    @Test
    @DisplayName("Explorer existence exception")
    public void prepare_to_create_obj_will_return_explorer_existence_exception(){
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer_test", "NORTH",1 , 1);
        explorerService.prepareToCreateExplorerObj(dto);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e){
            assertEquals("Explorer already exists in Data Base", e.getMessage());
            return ;
        }
        fail("expected: Explorer already exists in Data Base");
    }

    @Test
    @DisplayName("Explorer name exception")
    public void checkExplorerArgs_will_return_alphanumeric_exception(){
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "$", "NORTH",1 , 1);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e){
            assertEquals("Arguments should contains AlphaNumeric characters only", e.getMessage());
            return ;
        }
        fail("expected: Arguments should contains AlphaNumeric characters only");
    }

    @Test
    @DisplayName("Explorer axis exception < 0")
    public void validAxisOutOfBounds_will_return_x_or_y_must_return_exception_less_than_0() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer1", "NORTH",-1 , 0);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e){
            assertEquals("axis x and y can't be less than 0", e.getMessage());
            return ;
        }
        fail("expected: axis x and y can't be less than 0");
    }

    @Test
    @DisplayName("Explorer axis exception x > planet height")
    public void validAxisOutOfBounds_will_return_x_must_return_exception_greater_than_planet_size() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer2", "NORTH",5 , 0);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e){
            assertEquals("axis x out of planet area", e.getMessage());
            return ;
        }
        fail("expected: axis x out of planet area");
    }

    @Test
    @DisplayName("Explorer axis exception y > planet width")
    public void validAxisOutOfBounds_will_return_y_must_return_exception_greater_than_planet_size() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer3", "NORTH",2 , 5);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e){
            assertEquals("axis y out of planet area", e.getMessage());
            return ;
        }
        fail("expected: axis y out of planet area");
    }

    @Test
    @DisplayName("Explorer axis exception same position as other explorer")
    public void validAxisOutOfBounds_will_return_axis_can_not_be_registered_in_same_position_from_other_explorer() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer4", "NORTH",2 , 0);
        explorerService.prepareToCreateExplorerObj(dto);
        ExplorerDto dto1 = new ExplorerDto("planet_test", "explorer5", "NORTH",2 , 0);
        try {
            explorerService.prepareToCreateExplorerObj(dto1);
        } catch (ExplorerException e){
            assertEquals("This explorer can't be registered in this position", e.getMessage());
            return ;
        }
        fail("expected: This explorer can't be registered in this position");
    }

    @Test
    @DisplayName("Explorer cardinal must be valid")
    public void validCardinal_will_return_a_invalid_cardinal_msg() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer6", "potato",2 , 4);
         try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e){
            assertEquals("Invalid Cardinal", e.getMessage());
            return ;
        }
        fail("expected: Invalid Cardinal");
    }

    @Test
    @DisplayName("Explorer get must return to string")
    public void findAllExplorers_will_return_all_explorers_in_string_format() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer7", "NORTH",2 , 4);
        ExplorerDto dto1 = new ExplorerDto("planet_test", "explorer8", "south",1 , 2);
        explorerService.prepareToCreateExplorerObj(dto);
        explorerService.prepareToCreateExplorerObj(dto1);
        System.out.println(explorerService.findAllExplorers());
    }

}
