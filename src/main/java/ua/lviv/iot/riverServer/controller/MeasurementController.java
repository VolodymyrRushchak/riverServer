package ua.lviv.iot.riverServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.riverServer.logic.MeasurementService;
import ua.lviv.iot.riverServer.model.Measurement;

import java.io.IOException;
import java.util.List;

@RestController
public class MeasurementController {
    @Autowired
    private MeasurementService measurementService;


    @PostMapping("/measurements")
    public ResponseEntity<?> create(@RequestBody final List<Measurement> measurements) {
        try {
            for (Measurement measurement : measurements) {
                measurementService.create(measurement);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/measurements")
    public ResponseEntity<List<Measurement>> readAll() {
        List<Measurement> measurements = measurementService.readAll();
        return measurements != null && !measurements.isEmpty()
                ? new ResponseEntity<>(measurements, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/measurements/{id}")
    public ResponseEntity<Measurement> read(@PathVariable("id") final Long id) {
        Measurement measurement = measurementService.read(id);
        return measurement != null
                ? new ResponseEntity<>(measurement, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/measurementStations/{stationId}/measurements")
    public ResponseEntity<List<Measurement>> readAllStationsMeasurements(@PathVariable("stationId") final Long id) {
        final List<Measurement> measurements = measurementService.readAllStationsMeasurements(id);
        return measurements != null && !measurements.isEmpty()
                ? new ResponseEntity<>(measurements, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/measurements/{id}")
    public ResponseEntity<?> update(@RequestBody final Measurement measurement, @PathVariable("id") final Long id) {
        try {
            Boolean updated = measurementService.update(measurement, id);
            return updated
                    ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/measurements/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {
        try {
            Boolean deleted = measurementService.delete(id);
            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (IOException exc) {
            exc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
