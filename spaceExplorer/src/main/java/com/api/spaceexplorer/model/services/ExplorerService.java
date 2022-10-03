package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.model.enums.ExplorerEnum;
import com.api.spaceexplorer.repositories.ExplorerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExplorerService {

    private final ExplorerRepository explorerRepository;
    private final PlanetService planetService;

    public ExplorerService(ExplorerRepository explorerRepository, PlanetService planetService) {
        this.explorerRepository = explorerRepository;
        this.planetService = planetService;
    }

    private Optional<ExplorerEntity> checkIfExplorerExist(String explorerName){
        return explorerRepository.findExplorerEntityByExplorerName(explorerName);

    }

    private static void checkExplorerArgs(String args) {
        if (args.matches("\\W*")){
            throw new ExplorerException("Arguments should contains AlphaNumeric characters only");
        }
    }

    private static void validAxisOutOfBounds(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        if (explorerDto.getX() < 0 || explorerDto.getY() < 0)
            throw new ExplorerException("axis x and y can't be less than 0");
        if (explorerDto.getX() > planetEntity.getHeight() - 1)
            throw new ExplorerException("axis x out of planet area");
        if (explorerDto.getY() > planetEntity.getWidth() - 1)
            throw new ExplorerException("axis y out of planet area");
    }

    private static void validAxisInSamePosition(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        List<ExplorerEntity> explorers = planetEntity.getExplorer();
        for (ExplorerEntity c : explorers){
            if (c.getX() == explorerDto.getX() && c.getY() == explorerDto.getY())
                throw new ExplorerException("This explorer can't be registered in this position");
        }
    }

    private static void validCardinal(String cardinal) {
        switch (cardinal.toUpperCase()){
            case "NORTH":
            case "SOUTH":
            case "WEST":
            case "EAST":
                break ;
            default:
                throw new ExplorerException("invalid Cardinal");
        }
    }

    private static void validDirection(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        validAxisOutOfBounds(explorerDto, planetEntity);
        validAxisInSamePosition(explorerDto, planetEntity);
        validCardinal(explorerDto.getDirection());
    }

    private static void validObjArgs(ExplorerDto explorer, PlanetEntity planet){
        checkExplorerArgs(explorer.getExplorerName());
        checkExplorerArgs(explorer.getPlanetName());
        validDirection(explorer, planet);
    }

    public void prepareToCreateExplorerObj(ExplorerDto explorerDto) {
        var planetEntity = planetService.locatePlanet(explorerDto.getPlanetName());
        if(checkIfExplorerExist(explorerDto.getExplorerName()).isPresent())
            throw new ExplorerException("Explorer already exists in Data Base");
        planetService.planetCapacity(planetEntity.get());
        var explorerEntity = ExplorerEntity.fromExplorerDto(explorerDto, planetEntity.get());
        validObjArgs(explorerDto, planetEntity.get());
        planetEntity.get().sumExplorerAmount();
        saveExplorer(explorerEntity);
    }

    public String findAllExplorers() {
        StringBuilder sb = new StringBuilder();
        List<ExplorerEntity> explorers = explorerRepository.findAll();
        sb.append("Explorers{\n");
        for (ExplorerEntity e : explorers){
            sb.append("ExplorerName: ");
            sb.append(e.getExplorerName()).append("\n");
        }
        sb.append('}');
       return sb.toString();
    }

    public ExplorerEntity getExplorerObject(ExplorerDto explorerDto) {
        var explorerEntity = checkIfExplorerExist(explorerDto.getExplorerName());
        if (explorerEntity.isEmpty()){
            throw new ExplorerException("Explorer not found in data base\"");
        }
        return explorerEntity.get();
    }

    private void validatePutNameArgs(ExplorerDto explorerDto) {

        if (checkIfExplorerExist(explorerDto.getExplorerName()).isEmpty())
            throw new ExplorerException("Explorer not found in data base\"");
        if (checkIfExplorerExist(explorerDto.getNewExplorerName()).isPresent())
            throw new ExplorerException("Explorer already exist in Data Base");
        if (explorerDto.getNewExplorerName().matches("\\W*"))
            throw new ExplorerException("Explorer name should contain AlphaNumeric characters only");
    }

    public ExplorerEntity validAndModifyName(ExplorerDto explorerDto) {

        validatePutNameArgs(explorerDto);
        var explorer = checkIfExplorerExist(explorerDto.getExplorerName());
        if (explorer.isEmpty())
            throw new ExplorerException("Explorer not found in data base\"");
        explorer.get().changeExplorerName(explorerDto.getNewExplorerName());
        saveExplorer(explorer.get());
        return explorer.get();
    }

    private void validateMoveArgs(ExplorerDto explorerDto) {
        checkExplorerArgs(explorerDto.getExplorerName());
        if (!explorerDto.getMovement().matches("[LMR]*"))
            throw new ExplorerException("move sequence must be L, M, R in Uppercase");
    }

    private void moveExplorer(String[][] planet, String movement, ExplorerEntity explorer) {
        for (int i = 0; i < movement.length(); i++){
            if(movement.charAt(i) == 'L'){
                ExplorerEnum.turnLeft(explorer);
            }
            else if (movement.charAt(i) == 'R'){
                ExplorerEnum.turnRight(explorer);
            }
            else if (movement.charAt(i) == 'M'){
                moveForward(planet, explorer);
            }
        }
    }

    private void moveForward(String[][] planet, ExplorerEntity explorer) {

        boolean routeCalculator = true;
        switch (explorer.getDirection()){
            case NORTH:
                routeCalculator = moveNorth(planet, explorer);
                break ;
            case WEST:
                routeCalculator = moveWest(planet, explorer);
                break ;
            case SOUTH:
                routeCalculator = moveSouth(planet, explorer);
                break ;
            case EAST:
                routeCalculator = moveEast(planet, explorer);
                break ;
        }
        if (!routeCalculator)
            throw new ExplorerException("There is an explorer ahead, aborting movement.");
    }

    private boolean moveNorth(String[][] planet, ExplorerEntity explorer) {

        if (explorer.getX() == 0){
            if (planet[planet.length - 1][explorer.getY()].matches("[x0]")){
                explorer.axisUpdate((planet.length - 1), explorer.getY());
            } else {
                return false;
            }
        } else if (planet[explorer.getX() - 1][explorer.getY()].matches("[x0]")){
                explorer.axisUpdate(explorer.getX() - 1, explorer.getY());
        } else {
            return false;
        }
        return true;
    }

    private boolean moveWest(String[][] planet, ExplorerEntity explorer) {
        if (explorer.getY() == planet[explorer.getX()].length - 1){
            if (planet[explorer.getX()][0].matches("[x0]")){
                explorer.axisUpdate((explorer.getX()), 0);
            } else {
                return false;
            }
        } else if (planet[explorer.getX()][explorer.getY() + 1].matches("[x0]")){
            explorer.axisUpdate(explorer.getX(), explorer.getY() + 1);
        } else {
            return false;
        }
        return true;
    }

    private boolean moveSouth(String[][] planet, ExplorerEntity explorer) {
        if (explorer.getX() == planet.length - 1){
            if (planet[0][explorer.getY()].matches("[x0]")){
                explorer.axisUpdate(0, explorer.getY());
            } else {
                return false;
            }
        } else if (planet[explorer.getX() + 1][explorer.getY()].matches("[x0]")){
            explorer.axisUpdate(explorer.getX() + 1, explorer.getY());
        } else {
            return false;
        }
        return true;
    }

    private boolean moveEast(String[][] planet, ExplorerEntity explorer) {
        if (explorer.getY() == 0){
            if (planet[explorer.getX()][planet[explorer.getX()].length - 1].matches("[x0]")){
                explorer.axisUpdate((explorer.getX()), planet[explorer.getX()].length - 1);
            } else {
                return false;
            }
        } else if (planet[explorer.getX()][explorer.getY() - 1].matches("[x0]")){
            explorer.axisUpdate(explorer.getX(), explorer.getY() - 1);
        } else {
            return false;
        }
        return true;
    }

    public ExplorerEntity validAndMoveExplorer(ExplorerDto explorerDto) {
        validateMoveArgs(explorerDto);
        var explorer = checkIfExplorerExist(explorerDto.getExplorerName());
        if (explorer.isEmpty())
            throw new ExplorerException("Explorer not found in data base\"");
        String[][] planet = PlanetService.drawPlanet(explorer.get());
        moveExplorer(planet, explorerDto.getMovement(), explorer.get());
        saveExplorer(explorer.get());
        return explorer.get();
    }

    public void validAndDeleteExplorer(ExplorerDto explorerDto) {
        checkExplorerArgs(explorerDto.getExplorerName());
        var planetEntity = planetService.locatePlanet(explorerDto.getPlanetName());
        var explorerEntity = checkIfExplorerExist(explorerDto.getExplorerName());

        if (explorerEntity.isEmpty())
            throw new ExplorerException("Explorer not found in data base\"");
        if (planetEntity.isEmpty())
            throw new ExplorerException("Planet not found in data base");
        explorerEntity.get().getPlanet().decExplorerAmount();
        deleteExplorer(explorerEntity.get());
    }

    @Transactional
    public void saveExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.save(explorerEntity);
    }

    @Transactional
    public void deleteExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.delete(explorerEntity); }
}
