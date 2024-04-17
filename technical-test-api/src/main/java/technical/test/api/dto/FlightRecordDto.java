package technical.test.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class FlightRecordDto {
    @NotNull
    private LocalDateTime departure;
    @NotNull
    private LocalDateTime arrival;
    @NotNull
    private double price;
    @NotEmpty(message = "can't be empty")
    private String origin;
    @NotNull
    private String destination;
    @NotNull
    @URL(message = "image URL is not valid")
    private String image;


}
