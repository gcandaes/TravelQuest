package technical.test.api.endpoints;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.dto.FlightRecordDto;
import technical.test.api.facade.FlightFacade;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightEndpoint {
    private final FlightFacade flightFacade;

    Logger log = LoggerFactory.getLogger(FlightEndpoint.class);

    @GetMapping
    public Flux<FlightRepresentation> getAllFlights(@PageableDefault(page = 0, size = 50) Pageable pageable,
                                                    @RequestParam(required = false) String filterBy){
        log.info("Entering FlightEndPoint server side getAllFlights method");
        return flightFacade.getAllFlights(pageable, filterBy);
    }

    @GetMapping("/{flightId}")
    public Mono<FlightRepresentation> getFlightById(@PathVariable String flightId){
        return flightFacade.getFlightById(flightId);
    }

    @PostMapping
    public Mono<FlightRepresentation> createFlight(@Valid @RequestBody FlightRecordDto flightRecordDto) {
        return flightFacade.createFlight(flightRecordDto);
    }
}
