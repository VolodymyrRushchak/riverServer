package ua.lviv.iot.riverServer.dataaccess.file;

import org.springframework.stereotype.Component;
import ua.lviv.iot.riverServer.model.River;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class RiverStorage {
    private Long riverIdHolder = 0L;
    private String workingDirectory;

    public HashMap<Long, River> getCurrentMonthRivers() {
        HashMap<Long, River> result = new HashMap<>();
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); ++i) {
            String path = workingDirectory + File.separator + "river-"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-"))
                    + String.format("%02d", i) + ".csv";
            File file = new File(path);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    List<String> lines = reader.lines().toList();
                    for (int j = 1; j < lines.size(); j++) {
                        String[] records = lines.get(j).split(",");
                        River river = new River();
                        river.setId(Long.valueOf(records[0]));
                        if (river.getId() > riverIdHolder) {
                            riverIdHolder = river.getId();
                        }
                        river.setName(records[1]);
                        river.setLength(Double.valueOf(records[2]));
                        river.setFlowRate(Double.valueOf(records[3]));
                        river.setBasinArea(Double.valueOf(records[4]));
                        result.put(river.getId(), river);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }
        return result;
    }

    public void create(final River river) throws IOException {
        river.setId(++riverIdHolder);
        String path = workingDirectory + File.separator + "river-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        File file = new File(path);
        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
                fileWriter.write(river.getHeaders() + "\r\n");
                fileWriter.write(river.toCSV() + "\r\n");
            }
        } else {
            try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                fileWriter.write(river.toCSV() + "\r\n");
            }
        }
    }

    public Boolean update(final River river, final Long id) throws IOException {
        river.setId(id);
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            String path = workingDirectory + File.separator + "river-"
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
                            result.append(river.toCSV()).append("\r\n");
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
            String path = workingDirectory + File.separator + "river-"
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

    public Long getRiverIdHolder() {
        return riverIdHolder;
    }

    public void setWorkingDirectory(final String dir) {
        riverIdHolder = 0L;
        workingDirectory = dir;
    }

}

