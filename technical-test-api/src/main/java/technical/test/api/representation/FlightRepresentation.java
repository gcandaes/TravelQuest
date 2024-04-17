package technical.test.api.representation;

import lombok.Data;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FlightRepresentation {
    private UUID id;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private double price;
    private AirportRepresentation origin;
    private AirportRepresentation destination;

    @Cacheable("flightImages")
    public String getImage() {
        return image;
    }

    private String image;
}
