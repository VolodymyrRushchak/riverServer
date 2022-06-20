package ua.lviv.iot.riverServer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.riverServer.dataaccess.file.MeasurementStationStorage;
import ua.lviv.iot.riverServer.dataaccess.file.MeasurementStorage;
import ua.lviv.iot.riverServer.dataaccess.file.RiverStorage;
import ua.lviv.iot.riverServer.model.Measurement;
import ua.lviv.iot.riverServer.model.MeasurementStation;
import ua.lviv.iot.riverServer.model.River;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@SpringBootTest
class RiverServerApplicationTests {

    @Autowired
    private RiverStorage testRiverStorage;
    @Autowired
    private MeasurementStationStorage testMeasurementStationStorage;
    @Autowired
    private MeasurementStorage testMeasurementStorage;


    @Test
    void testWritingRiverToCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources"};
        String directory = String.join(File.separator, directoryElements);
        testRiverStorage.setWorkingDirectory(directory);
        testRiverStorage.create(new River(null, "Dnipro", 2100.5, 134.7, 14688.2));
        String path = directory + File.separator + "river-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertNotEquals(null, line);
        String[] records = line.split(",");
        Assertions.assertEquals("1", records[0]);
        Assertions.assertEquals("Dnipro", records[1]);
        Assertions.assertEquals("2100.5", records[2]);
        Assertions.assertEquals("134.7", records[3]);
        Assertions.assertEquals("14688.2", records[4]);
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testReadingRiverFromCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources", "testRivers"};
        String directory = String.join(File.separator, directoryElements);
        String path = directory + File.separator + "river-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,name,length,flowRate,basinArea\r
                    1,Dnipro,2100.5,134.7,14688.2\r
                    """);
        }
        testRiverStorage.setWorkingDirectory(directory);
        HashMap<Long, River> rivers = testRiverStorage.getCurrentMonthRivers();
        Assertions.assertEquals(1, rivers.get(1L).getId());
        Assertions.assertEquals("Dnipro", rivers.get(1L).getName());
        Assertions.assertEquals(2100.5, rivers.get(1L).getLength());
        Assertions.assertEquals(134.7, rivers.get(1L).getFlowRate());
        Assertions.assertEquals(14688.2, rivers.get(1L).getBasinArea());
        Files.delete(Path.of(path));
    }

    @Test
    void testUpdatingRiverInCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources", "testRivers"};
        String directory = String.join(File.separator, directoryElements);
        String path = directory + File.separator + "river-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,name,length,flowRate,basinArea\r
                    1,Dnipro,2100.5,134.7,14688.2\r
                    """);
        }
        testRiverStorage.setWorkingDirectory(directory);
        testRiverStorage.update(
                new River(null, "Dnister", 2001.6, 121.11, 10176.2), 1L);
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertNotEquals(null, line);
        String[] records = line.split(",");
        Assertions.assertEquals("1", records[0]);
        Assertions.assertEquals("Dnister", records[1]);
        Assertions.assertEquals("2001.6", records[2]);
        Assertions.assertEquals("121.11", records[3]);
        Assertions.assertEquals("10176.2", records[4]);
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testDeletingRiverFromCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources", "testRivers"};
        String directory = String.join(File.separator, directoryElements);
        String path = directory + File.separator + "river-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,name,length,flowRate,basinArea\r
                    1,Dnipro,2100.5,134.7,14688.2\r
                    """);
        }
        testRiverStorage.setWorkingDirectory(directory);
        testRiverStorage.delete(1L);
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        Assertions.assertEquals("id,name,length,flowRate,basinArea", reader.readLine());
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testWritingMeasurementStationToCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources"};
        String directory = String.join(File.separator, directoryElements);
        testMeasurementStationStorage.setWorkingDirectory(directory);
        testMeasurementStationStorage.create(
                new MeasurementStation(null, "Kyiv", "-49.351267;51.345212", 1L));
        String path = directory + File.separator + "measurementStation-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertNotEquals(null, line);
        String[] records = line.split(",");
        Assertions.assertEquals("1", records[0]);
        Assertions.assertEquals("Kyiv", records[1]);
        Assertions.assertEquals("-49.351267;51.345212", records[2]);
        Assertions.assertEquals("1", records[3]);
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testReadingMeasurementStationFromCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources", "testMeasurementStations"};
        String directory = String.join(File.separator, directoryElements);
        String path = directory + File.separator + "measurementStation-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,name,gpsCoordinates,riverId\r
                    1,Kyiv,-49.267136;50.123567,1\r
                    """);
        }
        testMeasurementStationStorage.setWorkingDirectory(directory);
        HashMap<Long, MeasurementStation> measurementStations =
                testMeasurementStationStorage.getCurrentMonthMeasurementStations();
        Assertions.assertEquals(1, measurementStations.get(1L).getId());
        Assertions.assertEquals("Kyiv", measurementStations.get(1L).getName());
        Assertions.assertEquals("-49.267136;50.123567", measurementStations.get(1L).getGpsCoordinates());
        Assertions.assertEquals(1, measurementStations.get(1L).getRiverId());
        Files.delete(Path.of(path));
    }

    @Test
    void testUpdatingMeasurementStationInCSV() throws IOException {
        String[] directoryElements = {"src", "test", "java", "testresources", "testMeasurementStations"};
        String directory = String.join(File.separator, directoryElements);
        String path = directory + File.separator + "measurementStation-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,name,gpsCoordinates,riverId\r
                    1,Kyiv,-49.267136;50.123567,1\r
                    """);
        }
        testMeasurementStationStorage.setWorkingDirectory(directory);
        testMeasurementStationStorage.update(
                new MeasurementStation(null, "Dnipro", "-46.351267;55.345212", 2L), 1L);
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertNotEquals(null, line);
        String[] records = line.split(",");
        Assertions.assertEquals("1", records[0]);
        Assertions.assertEquals("Dnipro", records[1]);
        Assertions.assertEquals("-46.351267;55.345212", records[2]);
        Assertions.assertEquals("2", records[3]);
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testDeletingMeasurementStationFromCSV() throws IOException {
        String[] directory_elements = {"src", "test", "java", "testresources", "testMeasurementStations"};
        String directory = String.join(File.separator, directory_elements);
        String path = directory + File.separator + "measurementStation-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,name,gpsCoordinates,riverId\r
                    1,Kyiv,-49.267136;50.123567,1\r
                    """);
        }
        testMeasurementStationStorage.setWorkingDirectory(directory);
        testMeasurementStationStorage.delete(1L);
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        Assertions.assertEquals("id,name,gpsCoordinates,riverId", reader.readLine());
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testWritingMeasurementToCSV() throws IOException {
        String[] directory_elements = {"src", "test", "java", "testresources"};
        String directory = String.join(File.separator, directory_elements);
        testMeasurementStorage.setWorkingDirectory(directory);
        testMeasurementStorage.create(
                new Measurement(null, "2022-06-20", 32.3, "Kyiv", 1L));
        String path = directory + File.separator + "measurement-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertNotEquals(null, line);
        String[] records = line.split(",");
        Assertions.assertEquals("1", records[0]);
        Assertions.assertEquals("2022-06-20", records[1]);
        Assertions.assertEquals("32.3", records[2]);
        Assertions.assertEquals("Kyiv", records[3]);
        Assertions.assertEquals("1", records[4]);
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testReadingMeasurementFromCSV() throws IOException {
        String[] directory_elements = {"src", "test", "java", "testresources", "testMeasurements"};
        String directory = String.join(File.separator, directory_elements);
        String path = directory + File.separator + "measurement-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,date,waterLevel,stationName,stationId\r
                    1,2022-06-15,15.7,Kyiv,1\r
                    """);
        }
        testMeasurementStorage.setWorkingDirectory(directory);
        HashMap<Long, Measurement> measurements =
                testMeasurementStorage.getCurrentMonthMeasurements();
        Assertions.assertEquals(1, measurements.get(1L).getId());
        Assertions.assertEquals("2022-06-15", measurements.get(1L).getDate());
        Assertions.assertEquals(15.7, measurements.get(1L).getWaterLevel());
        Assertions.assertEquals("Kyiv", measurements.get(1L).getStationName());
        Assertions.assertEquals(1, measurements.get(1L).getStationId());
        Files.delete(Path.of(path));
    }

    @Test
    void testUpdatingMeasurementInCSV() throws IOException {
        String[] directory_elements = {"src", "test", "java", "testresources", "testMeasurements"};
        String directory = String.join(File.separator, directory_elements);
        String path = directory + File.separator + "measurement-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,date,waterLevel,stationName,stationId\r
                    1,2022-06-15,15.7,Kyiv,1\r
                    """);
        }
        testMeasurementStorage.setWorkingDirectory(directory);
        testMeasurementStorage.update(
                new Measurement(null, "2022-06-20", 32.3, "Kyiv", 2L), 1L);
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertNotEquals(null, line);
        String[] records = line.split(",");
        Assertions.assertEquals("1", records[0]);
        Assertions.assertEquals("2022-06-20", records[1]);
        Assertions.assertEquals("32.3", records[2]);
        Assertions.assertEquals("Kyiv", records[3]);
        Assertions.assertEquals("2", records[4]);
        reader.close();
        Files.delete(Path.of(path));
    }

    @Test
    void testDeletingMeasurementFromCSV() throws IOException {
        String[] directory_elements = {"src", "test", "java", "testresources", "testMeasurements"};
        String directory = String.join(File.separator, directory_elements);
        String path = directory + File.separator + "measurement-"
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write("""
                    id,date,waterLevel,stationName,stationId\r
                    1,2022-06-15,15.7,Kyiv,1\r
                    """);
        }
        testMeasurementStorage.setWorkingDirectory(directory);
        testMeasurementStorage.delete(1L);
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        Assertions.assertEquals("id,date,waterLevel,stationName,stationId", reader.readLine());
        reader.close();
        Files.delete(Path.of(path));
    }

}
