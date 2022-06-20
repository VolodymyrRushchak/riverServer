package ua.lviv.iot.riverServer.logic;

import ua.lviv.iot.riverServer.model.Measurement;

import java.io.IOException;
import java.util.List;


public interface MeasurementService {

    void create(Measurement measurement) throws IOException;

    List<Measurement> readAll();

    Measurement read(Long id);

    List<Measurement> readAllStationsMeasurements(Long id);

    Boolean update(Measurement measurement, Long id) throws IOException;

    Boolean delete(Long id) throws IOException;

}
