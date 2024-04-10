package technical.test.api.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.mapper.AirportMapper;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

@Component
@RequiredArgsConstructor
public class FlightFacade {
    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;


    public Flux<FlightRepresentation> getAllFlights(String sortBy, String filterBy) {
        final int pageSize = 6;

        Flux<FlightRecord> flights = flightService.getAllFlights();

        final int pageNumber = Long.valueOf(flights.count().block()).intValue();


        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equals("price")) {
                flights = flights.sort((flight1, flight2) -> Double.compare(flight1.getPrice(), flight2.getPrice()));
            } else if (sortBy.equals("origin")) {
                flights = flights.sort((flight1, flight2) -> flight1.getOrigin().compareTo(flight2.getOrigin()));
            }
        }

        if (filterBy != null && !filterBy.isEmpty()) {
            flights = flights.filter(flight -> flight.getOrigin().equals(filterBy));
        }

        // Pagination
        flights = flights.take(pageSize);
/*
        int skip = (pageNumber) * pageSize;
        flights = flights.skip(skip).take(pageSize);*/



        return flights.flatMap(this::mapFlightRecordToRepresentation);
    }


    // Méthode utilitaire pour mapper un FlightRecord en une FlightRepresentation
    private Mono<FlightRepresentation> mapFlightRecordToRepresentation(FlightRecord flightRecord) {
        return airportService.findByIataCode(flightRecord.getOrigin())
                .zipWith(airportService.findByIataCode(flightRecord.getDestination()))
                .map(tuple -> {
                    AirportRecord origin = tuple.getT1();
                    AirportRecord destination = tuple.getT2();
                    FlightRepresentation flightRepresentation = this.flightMapper.convert(flightRecord);
                    flightRepresentation.setOrigin(this.airportMapper.convert(origin));
                    flightRepresentation.setDestination(this.airportMapper.convert(destination));
                    return flightRepresentation;
                });
    }


    public Mono<FlightRecord> createFlight(FlightRecord flightRecord) {
        return flightService.createFlight(flightRecord);
    }
}
