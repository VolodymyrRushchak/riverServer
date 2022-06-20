package ua.lviv.iot.riverServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementStation {

    private Long id;
    private String name;
    private String gpsCoordinates;
    private Long riverId;

    @Transient
    public String getHeaders() {
        return "id,name,gpsCoordinates,riverId";
    }

    public String toCSV() {
        return id + "," + name + "," + gpsCoordinates + "," + riverId;
    }

}
