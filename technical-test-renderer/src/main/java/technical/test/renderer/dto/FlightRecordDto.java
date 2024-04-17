package technical.test.renderer.dto;

import lombok.*;
import technical.test.renderer.viewmodels.AirportViewModel;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class FlightRecordDto {

    private LocalDateTime departure;
    private LocalDateTime arrival;
    private double price;
    private String origin;
    private String destination;
    private String image;
}
