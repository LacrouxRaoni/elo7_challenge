package com.api.spaceexplorer.controller;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.services.ExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/space-explorer/explorer")
public class ExplorerController {

    private final ExplorerService explorerService;

    @Autowired
    public ExplorerController(ExplorerService explorerService) {
        this.explorerService = explorerService;
    }

    @GetMapping("/all")
    public ResponseEntity getAllExplorers(){
        return ResponseEntity.status(HttpStatus.OK).body(explorerService.findAllExplorers());
    }

    @GetMapping
    public ResponseEntity getExplorer(@RequestBody @Valid ExplorerDto explorerDto){
        try {
            ExplorerEntity explorer = explorerService.getExplorerObject(explorerDto);
            return ResponseEntity.status(HttpStatus.OK).body(explorer.toString());
        } catch (ExplorerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity postExplorer(@RequestBody ExplorerDto explorerDto){
        try {
            explorerService.prepareToCreateExplorerObj(explorerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Explorer added with success");
        } catch (ExplorerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/name")
    public ResponseEntity putExplorerName(@RequestBody ExplorerDto explorerDto) {
        try{
            ExplorerEntity explorer = explorerService.validAndModifyName(explorerDto);
            return ResponseEntity.status(HttpStatus.OK).body(explorer.toString());
        } catch (ExplorerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("move")
    public ResponseEntity putExplorerMove(@RequestBody ExplorerDto explorerDto) {
        try {
            explorerService.validAndMoveExplorer(explorerDto);
            return null;
        } catch (ExplorerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity deleteExplorer(@RequestBody ExplorerDto explorerDto) {
        try {
            explorerService.validAndDeleteExplorer(explorerDto);
            return ResponseEntity.status(HttpStatus.OK).body("Explorer deleted with success");
        } catch (ExplorerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
