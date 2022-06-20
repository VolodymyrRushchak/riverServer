package ua.lviv.iot.riverServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.riverServer.logic.RiverService;
import ua.lviv.iot.riverServer.model.River;

import java.io.IOException;
import java.util.List;

@RestController
public class RiverController {
    @Autowired
    private RiverService riverService;


    @PostMapping("/rivers")
    public ResponseEntity<?> create(@RequestBody final List<River> rivers) {
        try {
            for (River river : rivers) {
                riverService.create(river);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/rivers")
    public ResponseEntity<List<River>> readAll() {
        List<River> rivers = riverService.readAll();
        return rivers != null && !rivers.isEmpty()
                ? new ResponseEntity<>(rivers, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/rivers/{id}")
    public ResponseEntity<River> read(@PathVariable("id") final Long id) {
        River river = riverService.read(id);
        return river != null
                ? new ResponseEntity<>(river, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/rivers/{id}")
    public ResponseEntity<?> update(@RequestBody final River river, @PathVariable("id") final Long id) {
        try {
            Boolean updated = riverService.update(river, id);
            return updated
                    ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/rivers/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {
        try {
            Boolean deleted = riverService.delete(id);
            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
