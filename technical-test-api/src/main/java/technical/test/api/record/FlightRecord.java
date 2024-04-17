package technical.test.api.record;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document(collection = "flight")
@AllArgsConstructor
public class FlightRecord {
    @Id
    private UUID id;
    @NotNull
    private LocalDateTime departure;
    @NotNull
    private LocalDateTime arrival;
    @NotNull
    private double price;
    @NotNull
    private String origin;
    @NotNull
    private String destination;
    @NotNull
    private String image;
}
