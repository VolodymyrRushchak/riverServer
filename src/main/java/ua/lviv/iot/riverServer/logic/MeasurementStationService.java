package ua.lviv.iot.riverServer.logic;

import ua.lviv.iot.riverServer.model.MeasurementStation;

import java.io.IOException;
import java.util.List;


public interface MeasurementStationService {
    void create(MeasurementStation measurementStation) throws IOException;

    List<MeasurementStation> readAll();

    MeasurementStation read(Long id);

    List<MeasurementStation> readAllRiversMeasurementStations(Long id);

    Boolean update(MeasurementStation measurementStation, Long id) throws IOException;

    Boolean delete(Long id) throws IOException;

}
