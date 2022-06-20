package ua.lviv.iot.riverServer.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.riverServer.dataaccess.file.MeasurementStorage;
import ua.lviv.iot.riverServer.logic.MeasurementService;
import ua.lviv.iot.riverServer.model.Measurement;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    @Autowired
    private MeasurementStorage measurementStorage;
    private HashMap<Long, Measurement> measurements;

    @PostConstruct
    private void init() {
        String[] directoryElements = {"csvfiles", "measurements"};
        String directory = String.join(File.separator, directoryElements);
        measurementStorage.setWorkingDirectory(directory);
        measurements = measurementStorage.getCurrentMonthMeasurements();
    }

    public void create(final Measurement measurement) throws IOException {
        measurementStorage.create(measurement);
        measurements.put(measurementStorage.getMeasurementIdHolder(), measurement);
    }

    public List<Measurement> readAll() {
        return new ArrayList<>(measurements.values());
    }

    public Measurement read(final Long id) {
        return measurements.get(id);
    }

    public List<Measurement> readAllStationsMeasurements(final Long id) {
        return measurements.values().stream()
                .filter(measurement ->
                        Objects.equals(measurement.getStationId(), id)).collect(Collectors.toList());
    }

    public Boolean update(final Measurement measurement, final Long id) throws IOException {
        measurement.setId(id);
        if (measurementStorage.update(measurement, id)) {
            measurements.put(id, measurement);
            return true;
        }
        return false;
    }

    public Boolean delete(final Long id) throws IOException {
        return measurementStorage.delete(id);
    }

}