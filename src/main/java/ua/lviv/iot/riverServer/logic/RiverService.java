package ua.lviv.iot.riverServer.logic;

import ua.lviv.iot.riverServer.model.River;

import java.io.IOException;
import java.util.List;


public interface RiverService {

    void create(River river) throws IOException;

    List<River> readAll();

    River read(Long id);

    Boolean update(River river, Long id) throws IOException;

    Boolean delete(Long id) throws IOException;

}
