package com.api.spaceexplorer.controller;

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
        if (!explorerService.createExplorerObj(explorerDto)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conflict: Invalid argument");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }
}
