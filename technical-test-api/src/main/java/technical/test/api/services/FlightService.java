package technical.test.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.record.FlightRecord;
import technical.test.api.repository.FlightRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;


    public Flux<FlightRecord> getAllFlights(Pageable pageable) {
        return flightRepository.findAllBy(pageable);
    }

    public Flux<FlightRecord> getAllFlightsFiltered(String origin, Pageable pageable) {
        return flightRepository.findByOriginContaining(origin, pageable);

    }

    public Mono<FlightRecord> createFlight(FlightRecord flightRecord){
        return flightRepository.save(flightRecord);
    }

    public Mono<FlightRecord> getFlightById(String flightId) {
        return flightRepository.findById(UUID.fromString(flightId));
    }
}
