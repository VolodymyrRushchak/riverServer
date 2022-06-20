package ua.lviv.iot.riverServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.riverServer.logic.MeasurementStationService;
import ua.lviv.iot.riverServer.model.MeasurementStation;

import java.io.IOException;
import java.util.List;


@RestController
public class MeasurementStationController {
    @Autowired
    private MeasurementStationService measurementStationService;


    @PostMapping("/measurementStations")
    public ResponseEntity<?> create(@RequestBody final List<MeasurementStation> measurementStations) {
        try {
            for (MeasurementStation measurementStation : measurementStations) {
                measurementStationService.create(measurementStation);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/measurementStations")
    public ResponseEntity<List<MeasurementStation>> readAll() {
        List<MeasurementStation> measurementStations = measurementStationService.readAll();
        return measurementStations != null && !measurementStations.isEmpty()
                ? new ResponseEntity<>(measurementStations, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/measurementStations/{id}")
    public ResponseEntity<MeasurementStation> read(@PathVariable("id") final Long id) {
        MeasurementStation measurementStation = measurementStationService.read(id);
        return measurementStation != null
                ? new ResponseEntity<>(measurementStation, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/rivers/{riverId}/measurementStations")
    public ResponseEntity<List<MeasurementStation>> readAllRiversMeasurementStations(@PathVariable("riverId") final Long id) {
        final List<MeasurementStation> measurementStations = measurementStationService.readAllRiversMeasurementStations(id);
        return measurementStations != null && !measurementStations.isEmpty()
                ? new ResponseEntity<>(measurementStations, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/measurementStations/{id}")
    public ResponseEntity<?> update(@RequestBody final MeasurementStation measurementStation, @PathVariable("id") final Long id) {
        try {
            Boolean updated = measurementStationService.update(measurementStation, id);
            return updated
                    ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/measurementStations/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {
        try {
            Boolean deleted = measurementStationService.delete(id);
            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
