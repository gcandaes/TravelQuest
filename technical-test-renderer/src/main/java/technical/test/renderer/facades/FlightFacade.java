package technical.test.renderer.facades;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.renderer.dto.FlightRecordDto;
import technical.test.renderer.services.FlightService;
import technical.test.renderer.viewmodels.FlightViewModel;

@Component
public class FlightFacade {

    private final FlightService flightService;

    public FlightFacade(FlightService flightService) {
        this.flightService = flightService;
    }

    public Flux<FlightViewModel> getFlights(String size) {
        return this.flightService.getFlights(size);
    }
    public void createFlight(FlightRecordDto flightRecordDto) {
         flightService.createFlight(flightRecordDto);
    }

    public Mono<FlightViewModel> getFlight(String flightId) {
        return this.flightService.getFlight(flightId);
    }


}
