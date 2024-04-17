package technical.test.renderer.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.renderer.clients.TechnicalApiClient;
import technical.test.renderer.dto.FlightRecordDto;
import technical.test.renderer.viewmodels.FlightViewModel;

@Service
public class FlightService {
    private final TechnicalApiClient technicalApiClient;

    public FlightService(TechnicalApiClient technicalApiClient) {
        this.technicalApiClient = technicalApiClient;
    }

    public Flux<FlightViewModel> getFlights(String size) {
        return this.technicalApiClient.getFlights(size);
    }

    public void createFlight(FlightRecordDto flightRecordDto) {
        this.technicalApiClient.createFlight(flightRecordDto);
    }

    public Mono<FlightViewModel> getFlight(String flightId) {
        return this.technicalApiClient.getFlight(flightId);
    }


}
