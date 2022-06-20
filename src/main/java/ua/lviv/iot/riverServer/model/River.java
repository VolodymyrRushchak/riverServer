package ua.lviv.iot.riverServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class River {
    private Long id;
    private String name;
    private Double length;
    private Double flowRate;
    private Double basinArea;

    @Transient
    public String getHeaders() {
        return "id,name,length,flowRate,basinArea";
    }

    public String toCSV() {
        return id + "," + name + "," + length + "," + flowRate + "," + basinArea;
    }

}
