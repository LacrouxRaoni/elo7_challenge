package com.api.spaceexplorer.controller;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.services.ExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/space-explorer/explorer")
public class ExplorerController {

    private final ExplorerService explorerService;

    @Autowired
    public ExplorerController(ExplorerService explorerService) {
        this.explorerService = explorerService;
    }

    @PostMapping
    public ResponseEntity postExplorer(@RequestBody ExplorerDto explorerDto){
        try {
            explorerService.createExplorerObj(explorerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Explorer added with success");
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
