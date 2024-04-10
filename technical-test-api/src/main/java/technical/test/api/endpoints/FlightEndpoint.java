package technical.test.api.endpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping
    public Flux<FlightRepresentation> getAllFlights(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String filterBy) {
        return flightFacade.getAllFlights(sortBy, filterBy);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<FlightRecord> createFlight(@RequestBody FlightRecord flightRecord){
        return flightFacade.createFlight(flightRecord);
    }

/*    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<FlightRecord> createFlight(@RequestBody FlightRecordDto flightRecord){
        System.out.println("coucou");
        return null;
        //return flightFacade.createFlight(flightRecord);
    }*/
}
