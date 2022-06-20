package ua.lviv.iot.riverServer.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.riverServer.dataaccess.file.MeasurementStationStorage;
import ua.lviv.iot.riverServer.logic.MeasurementStationService;
import ua.lviv.iot.riverServer.model.MeasurementStation;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MeasurementStationServiceImpl implements MeasurementStationService {

    @Autowired
    private MeasurementStationStorage measurementStationStorage;
    private HashMap<Long, MeasurementStation> measurementStations;

    @PostConstruct
    private void init() {
        String[] directoryElements = {"csvfiles", "measurementStations"};
        String directory = String.join(File.separator, directoryElements);
        measurementStationStorage.setWorkingDirectory(directory);
        measurementStations = measurementStationStorage.getCurrentMonthMeasurementStations();
    }

    public void create(final MeasurementStation measurementStation) throws IOException {
        measurementStationStorage.create(measurementStation);
        measurementStations.put(measurementStationStorage.getMeasurementStationIdHolder(), measurementStation);
    }

    public List<MeasurementStation> readAll() {
        return new ArrayList<>(measurementStations.values());
    }

    public MeasurementStation read(final Long id) {
        return measurementStations.get(id);
    }

    public List<MeasurementStation> readAllRiversMeasurementStations(final Long id) {
        return measurementStations.values().stream()
                .filter(measurementStation ->
                        Objects.equals(measurementStation.getRiverId(), id)).collect(Collectors.toList());
    }

    public Boolean update(final MeasurementStation measurementStation, final Long id) throws IOException {
        measurementStation.setId(id);
        if (measurementStationStorage.update(measurementStation, id)) {
            measurementStations.put(id, measurementStation);
            return true;
        }
        return false;
    }

    public Boolean delete(final Long id) throws IOException {
        return measurementStationStorage.delete(id);
    }

}

