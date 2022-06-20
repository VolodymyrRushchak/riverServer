package ua.lviv.iot.riverServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ua.lviv.iot.riverServer.controller", "ua.lviv.iot.riverServer.logic", "ua.lviv.iot.riverServer.dataaccess.file"
})
public class RiverServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiverServerApplication.class, args);
    }

}
