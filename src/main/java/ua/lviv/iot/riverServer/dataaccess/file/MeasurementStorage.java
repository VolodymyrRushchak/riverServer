package ua.lviv.iot.riverServer.dataaccess.file;

import org.springframework.stereotype.Component;
import ua.lviv.iot.riverServer.model.Measurement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class MeasurementStorage {
    private Long measurementIdHolder = 0L;
    private String workingDirectory;

    public HashMap<Long, Measurement> getCurrentMonthMeasurements() {
        HashMap<Long, Measurement> result = new HashMap<>();
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); ++i) {
            String path = workingDirectory + File.separator + "measurement-"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-"))
                    + String.format("%02d", i) + ".csv";
            File file = new File(path);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    List<String> lines = reader.lines().toList();
                    for (int j = 1; j < lines.size(); j++) {
                        String[] records = lines.get(j).split(",");
                        Measurement measurement = new Measurement();
                        measurement.setId(Long.valueOf(records[0]));
                        if (measurement.getId() > measurementIdHolder) {
                            measurementIdHolder = measurement.getId();
                        }
                        measurement.setDate(records[1]);
                        measurement.setWaterLevel(Double.valueOf(records[2]));
                        measurement.setStationName(records[3]);
                        measurement.setStationId(Long.valueOf(records[4]));
                        result.put(measurement.getId(), measurement);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }
        return result;
    }

    public void create(final Measurement measurement) throws IOException {
        measurement.setId(++measurementIdHolder);
        String path = workingDirectory + File.separator + "measurement-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        File file = new File(path);
        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
                fileWriter.write(measurement.getHeaders() + "\r\n");
                fileWriter.write(measurement.toCSV() + "\r\n");
            }
        } else {
            try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                fileWriter.write(measurement.toCSV() + "\r\n");
            }
        }
    }

    public Boolean update(final Measurement measurement, final Long id) throws IOException {
        measurement.setId(id);
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            String path = workingDirectory + File.separator + "measurement-"
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
                            result.append(measurement.toCSV()).append("\r\n");
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
            String path = workingDirectory + File.separator + "measurement-"
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

    public Long getMeasurementIdHolder() {
        return measurementIdHolder;
    }

    public void setWorkingDirectory(final String dir) {
        measurementIdHolder = 0L;
        workingDirectory = dir;
    }

}

