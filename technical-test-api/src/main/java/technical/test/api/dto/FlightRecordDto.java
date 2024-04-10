package technical.test.api.dto;

import lombok.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightRecordDto {
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private double price;
    private String origin;
    private String destination;
    private String image;
}
