package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.controller.exceptions.PlanetException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.model.enums.ExplorerEnum;
import com.api.spaceexplorer.repositories.ExplorerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
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
    @Autowired
    private ExplorerRepository explorerRepository;

    private void create_planet() {
        PlanetDto pDto = new PlanetDto("planet_test", 5, 5);
        if (planetService.locatePlanet("planet_test").isEmpty()) {
            planetService.validatePlanetAndSaveInDb(pDto);
        }
    }

    @DisplayName("Explorer existence exception")
    public void prepare_to_create_obj_will_return_explorer_existence_exception() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer_test", "NORTH", 2, 2);
        explorerService.prepareToCreateExplorerObj(dto);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e) {
            assertEquals("Explorer already exists in Data Base", e.getMessage());
            return;
        }
        fail("expected: Explorer already exists in Data Base");
    }

    @Test
    @DisplayName("Explorer name exception")
    public void checkExplorerArgs_will_return_alphanumeric_exception() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "$", "NORTH", 1, 1);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e) {
            assertEquals("Arguments should contains AlphaNumeric characters", e.getMessage());
            return;
        }
        fail("expected: Arguments should contains AlphaNumeric characters");
    }

    @Test
    @DisplayName("Explorer axis exception < 0")
    public void validAxisOutOfBounds_will_return_x_or_y_must_return_exception_less_than_0() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer1", "NORTH", -1, 0);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e) {
            assertEquals("axis x and y can't be less than 1", e.getMessage());
            return;
        }
        fail("expected: axis x and y can't be less than 1");
    }

    @Test
    @DisplayName("Explorer axis exception x > planet height")
    public void validAxisOutOfBounds_will_return_x_must_return_exception_greater_than_planet_size() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer2", "NORTH", 6, 1);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e) {
            assertEquals("axis x out of planet area", e.getMessage());
            return;
        }
        fail("expected: axis x out of planet area");
    }

    @Test
    @DisplayName("Explorer axis exception y > planet width")
    public void validAxisOutOfBounds_will_return_y_must_return_exception_greater_than_planet_size() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer3", "NORTH", 2, 6);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e) {
            assertEquals("axis y out of planet area", e.getMessage());
            return;
        }
        fail("expected: axis y out of planet area");
    }

    @Test
    @DisplayName("Explorer axis exception same position as other explorer")
    public void validAxisOutOfBounds_will_return_axis_can_not_be_registered_in_same_position_from_other_explorer() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer4", "NORTH", 5, 5);
        explorerService.prepareToCreateExplorerObj(dto);
        ExplorerDto dto1 = new ExplorerDto("planet_test", "explorer5", "NORTH", 5, 5);
        try {
            explorerService.prepareToCreateExplorerObj(dto1);
        } catch (ExplorerException e) {
            assertEquals("This explorer can't be registered in this position", e.getMessage());
            return;
        }
        fail("expected: This explorer can't be registered in this position");
    }

    @Test
    @DisplayName("Explorer cardinal must be valid")
    public void validCardinal_will_return_a_invalid_cardinal_msg() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer6", "potato", 2, 4);
        try {
            explorerService.prepareToCreateExplorerObj(dto);
        } catch (ExplorerException e) {
            assertEquals("Invalid Cardinal", e.getMessage());
            return;
        }
        fail("expected: Invalid Cardinal");
    }

    @Test
    @DisplayName("Explorer get must return to string")
    public void findAllExplorers_will_return_all_explorers_in_string_format() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer7", "NORTH", 2, 1);
        ExplorerDto dto1 = new ExplorerDto("planet_test", "explorer8", "south", 1, 1);
        explorerService.prepareToCreateExplorerObj(dto);
        explorerService.prepareToCreateExplorerObj(dto1);
        System.out.println(explorerService.findAllExplorers());
    }

    @Test
    @DisplayName("Explorer get must return explorer not found")
    public void getExplorerObject_will_return_exception_explorer_not_found() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer9", "NORTH", 2, 4);
        try {
            explorerService.getExplorerObject(dto);
        } catch (ExplorerException e) {
            assertEquals("Explorer not found in data base", e.getMessage());
            return;
        }
        fail("expected: Explorer not found in data base");
    }

    @Test
    @DisplayName("Explorer get must return one explorer data")
    public void getExplorerObject_will_return_explorer10_data() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer10", "NORTH", 2, 4);
        explorerService.prepareToCreateExplorerObj(dto);
        ExplorerEntity explorer = explorerService.getExplorerObject(dto);
        System.out.println(explorer.toString());
    }

    @Test
    @DisplayName("Explorer put args exception")
    public void validatePutNameArgs_will_return_explorer_not_found_in_data() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("explorer12", "explorer12");
        try {
            explorerService.validAndModifyName(dto);
        } catch (ExplorerException e) {
            assertEquals("Explorer not found in data base", e.getMessage());
            return;
        }
        fail("expected: Explorer not found in data base");
    }

    @Test
    @DisplayName("Explorer put args exception")
    public void validatePutNameArgs_will_return_explorer_already_registered_in_dba() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer12", "NORTH", 1, 2);
        explorerService.prepareToCreateExplorerObj(dto);
        ExplorerDto dto1 = new ExplorerDto("explorer12", "explorer12");
        try {
            explorerService.validAndModifyName(dto1);
        } catch (ExplorerException e) {
            assertEquals("Explorer already exist in Data Base", e.getMessage());
            return;
        }
        fail("expected: Explorer already exist in Data Base");
    }

    @DisplayName("Explorer put args exception")
    public void validatePutNameArgs_will_return_explorer_must_be_composed_with_alphanumerics() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer11", "NORTH", 1, 3);
        explorerService.prepareToCreateExplorerObj(dto);
        ExplorerDto dto1 = new ExplorerDto("explorer11", "$");
        try {
            explorerService.validAndModifyName(dto1);
        } catch (ExplorerException e) {
            assertEquals("Explorer name should contain AlphaNumeric characters only", e.getMessage());
            return;
        }
        fail("expected: Explorer name should contain AlphaNumeric characters only");
    }

    @Test
    @DisplayName("Explorer put ok status return updated Entity")
    public void validAndModifyName_will_return_new_explorer_name() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer12", "NORTH", 1, 4);
        explorerService.prepareToCreateExplorerObj(dto);
        ExplorerDto dto1 = new ExplorerDto("explorer12", "explorer13");
        ExplorerEntity explorer = explorerService.validAndModifyName(dto1);
        System.out.println(explorer.toString());
    }
    @Test
    @DisplayName("Explorer put move validate must return exception with invalid letter")
    public void validateMoveArgs_will_return_invalid_movement_letter() {
        ExplorerDto dto = ExplorerDto.movements("explorer12", "lmr");
        try{
            explorerService.validAndMoveExplorer(dto);
        } catch (ExplorerException e) {
            assertEquals("move sequence must be L, M, R in Uppercase", e.getMessage());
            return;
        }
        fail("expected: move sequence must be L, M, R in Uppercase");
    }

    @Test
    @DisplayName("Explorer put move must throw exception")
    public void validAndMoveExplorer_will_return_message_explorer_not_found_in_db() {
        ExplorerDto dto = ExplorerDto.movements("explorer", "LMR");
        try{
            explorerService.validAndMoveExplorer(dto);
        } catch (ExplorerException e) {
            assertEquals("Explorer not found in data base", e.getMessage());
            return;
        }
        fail("expected: Explorer not found in data base");
    }

    @Test
    @DisplayName("explorer must stop pointing west direction")
    public void moveExplorer_must_turn_explorer_left_and_stop_west() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.NORTH, 0, 0, planet);
        ExplorerEnum.turnLeft(explorer);
        if (!ExplorerEnum.WEST.equals(explorer.getDirection())){
            fail("expected: West");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing south direction")
    public void moveExplorer_must_turn_explorer_left_and_stop_south() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.WEST, 0, 0, planet);
        ExplorerEnum.turnLeft(explorer);
        if (!ExplorerEnum.SOUTH.equals(explorer.getDirection())){
            fail("expected: South");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing east direction")
    public void moveExplorer_must_turn_explorer_left_and_stop_east() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.SOUTH, 0, 0, planet);
        ExplorerEnum.turnLeft(explorer);
        if (!ExplorerEnum.EAST.equals(explorer.getDirection())){
            fail("expected: East");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing north direction")
    public void moveExplorer_must_turn_explorer_left_and_stop_north() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.EAST, 0, 0, planet);
        ExplorerEnum.turnLeft(explorer);
        if (!ExplorerEnum.NORTH.equals(explorer.getDirection())){
            fail("expected: North");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing east direction")
    public void moveExplorer_must_turn_explorer_right_and_stop_east() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.NORTH, 0, 0, planet);
        ExplorerEnum.turnRight(explorer);
        if (!ExplorerEnum.EAST.equals(explorer.getDirection())){
            fail("expected: East");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing west south")
    public void moveExplorer_must_turn_explorer_right_and_stop_south() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.EAST, 0, 0, planet);
        ExplorerEnum.turnRight(explorer);
        if (!ExplorerEnum.SOUTH.equals(explorer.getDirection())){
            fail("expected: south");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing west direction")
    public void moveExplorer_must_turn_explorer_right_and_stop_west() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.SOUTH, 0, 0, planet);
        ExplorerEnum.turnRight(explorer);
        if (!ExplorerEnum.WEST.equals(explorer.getDirection())){
            fail("expected: West");
        }
    }

    @Test
    @DisplayName("explorer must stop pointing north direction")
    public void moveExplorer_must_turn_explorer_right_and_stop_north() {
        PlanetEntity planet = new PlanetEntity("test", 5 , 5, 25);
        ExplorerEntity explorer = new ExplorerEntity("explorer", ExplorerEnum.WEST, 0, 0, planet);
        ExplorerEnum.turnRight(explorer);
        if (!ExplorerEnum.NORTH.equals(explorer.getDirection())){
            fail("expected: north");
        }
    }

    @Test
    @DisplayName("Explorer delete will throw exception invalid explorer")
    public void validAndDeleteExplorer_will_return_exception_there_is_no_explorer_in_db() {
        create_planet();
        ExplorerDto dto = new ExplorerDto("planet_test", "explorer14", "NORTH", 2, 1);
        try {
            explorerService.validAndDeleteExplorer(dto);
        } catch (ExplorerException e) {
            assertEquals("Explorer not found in data base", e.getMessage());
            return;
        }
        fail("expected: Explorer not found in data base");
    }
}
