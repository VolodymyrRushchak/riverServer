package ua.lviv.iot.riverServer.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.riverServer.dataaccess.file.RiverStorage;
import ua.lviv.iot.riverServer.logic.RiverService;
import ua.lviv.iot.riverServer.model.River;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RiverServiceImpl implements RiverService {

    @Autowired
    private RiverStorage riverStorage;
    private HashMap<Long, River> rivers;

    @PostConstruct
    private void init() {
        String[] directoryElements = {"csvfiles", "rivers"};
        String directory = String.join(File.separator, directoryElements);
        riverStorage.setWorkingDirectory(directory);
        rivers = riverStorage.getCurrentMonthRivers();
    }

    public void create(final River river) throws IOException {
        riverStorage.create(river);
        rivers.put(riverStorage.getRiverIdHolder(), river);
    }

    public List<River> readAll() {
        return new ArrayList<>(rivers.values());
    }

    public River read(final Long id) {
        return rivers.get(id);
    }

    public Boolean update(final River river, final Long id) throws IOException {
        river.setId(id);
        if (riverStorage.update(river, id)) {
            rivers.put(id, river);
            return true;
        }
        return false;
    }

    public Boolean delete(final Long id) throws IOException {
        return riverStorage.delete(id);
    }

}
