package ua.lviv.iot.riverServer.dataaccess.file;

import org.springframework.stereotype.Component;
import ua.lviv.iot.riverServer.model.MeasurementStation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class MeasurementStationStorage {
    private Long measurementStationIdHolder = 0L;
    private String workingDirectory;

    public HashMap<Long, MeasurementStation> getCurrentMonthMeasurementStations() {
        HashMap<Long, MeasurementStation> result = new HashMap<>();
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); ++i) {
            String path = workingDirectory + File.separator + "measurementStation-"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-"))
                    + String.format("%02d", i) + ".csv";
            File file = new File(path);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    List<String> lines = reader.lines().toList();
                    for (int j = 1; j < lines.size(); j++) {
                        String[] records = lines.get(j).split(",");
                        MeasurementStation measurementStation = new MeasurementStation();
                        measurementStation.setId(Long.valueOf(records[0]));
                        if (measurementStation.getId() > measurementStationIdHolder) {
                            measurementStationIdHolder = measurementStation.getId();
                        }
                        measurementStation.setName(records[1]);
                        measurementStation.setGpsCoordinates(records[2]);
                        measurementStation.setRiverId(Long.valueOf(records[3]));
                        result.put(measurementStation.getId(), measurementStation);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }
        return result;
    }

    public void create(final MeasurementStation measurementStation) throws IOException {
        measurementStation.setId(++measurementStationIdHolder);
        String path = workingDirectory + File.separator + "measurementStation-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        File file = new File(path);
        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
                fileWriter.write(measurementStation.getHeaders() + "\r\n");
                fileWriter.write(measurementStation.toCSV() + "\r\n");
            }
        } else {
            try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                fileWriter.write(measurementStation.toCSV() + "\r\n");
            }
        }
    }

    public Boolean update(final MeasurementStation measurementStation, final Long id) throws IOException {
        measurementStation.setId(id);
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            String path = workingDirectory + File.separator + "measurementStation-"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-"))
                    + String.format("%02d", i) + ".csv";
            File file = new File(path);
            if (file.exists()) {
                AtomicBoolean updated = new AtomicBoolean(false);
                try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    StringBuffer result = new StringBuffer();
                    reader.lines().forEach(line -> {
                        if (!line.split(",")[0].equals(id.toString()))
                            result.append(line).append("\r\n");
                        else {
                            updated.set(true);
                            result.append(measurementStation.toCSV()).append("\r\n");
                        }
                    });
                    if (updated.get()) {
                        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
                            fileWriter.write(result.toString());
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean delete(final Long id) throws IOException {
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            String path = workingDirectory + File.separator + "measurementStation-"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-"))
                    + String.format("%02d", i) + ".csv";
            File file = new File(path);
            if (file.exists()) {
                AtomicBoolean deleted = new AtomicBoolean(false);
                try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    StringBuffer result = new StringBuffer();
                    reader.lines().forEach(line -> {
                        if (!line.split(",")[0].equals(id.toString()))
                            result.append(line).append("\r\n");
                        else
                            deleted.set(true);
                    });
                    if (deleted.get()) {
                        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
                            fileWriter.write(result.toString());
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Long getMeasurementStationIdHolder() {
        return measurementStationIdHolder;
    }

    public void setWorkingDirectory(final String dir) {
        measurementStationIdHolder = 0L;
        workingDirectory = dir;
    }

}

