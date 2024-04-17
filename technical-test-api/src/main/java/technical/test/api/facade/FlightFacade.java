package technical.test.api.facade;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.dto.FlightRecordDto;
import technical.test.api.mapper.AirportMapper;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.UUID;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class FlightFacade {

    Logger log = LoggerFactory.getLogger(FlightFacade.class);

    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;
    public Flux<FlightRepresentation> getAllFlights(Pageable pageable, String filterBy) {

        Flux<FlightRecord> flights;

        if (filterBy != null && !filterBy.isEmpty()) {
            flights = flightService.getAllFlightsFiltered(filterBy, pageable);
        }
        else{
            flights =  flightService.getAllFlights(pageable);
        }
        log.info("flights.count() = " + flights.count().block());

        //flights = sort(sortBy, flights);
        //flights = filter(filterBy, flights);
        return flights.flatMap(this::mapFlightRecordToRepresentation);
    }


/*    private static Flux<FlightRecord> sort(String sortBy, Flux<FlightRecord> flights) {
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equals("price")) {
                flights = flights.sort((flight1, flight2) -> Double.compare(flight1.getPrice(), flight2.getPrice()));
            } else if (sortBy.equals("origin")) {
                flights = flights.sort((flight1, flight2) -> flight1.getOrigin().compareTo(flight2.getOrigin()));
            }
        }

        return flights;
    }

    private static Flux<FlightRecord> filter(String filterBy, Flux<FlightRecord> flights) {
        if (filterBy != null && !filterBy.isEmpty()) {
            flights = flights.filter(flight -> flight.getOrigin().equals(filterBy));
        }
        return flights;
    }*/


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


    public Mono<FlightRepresentation> createFlight(FlightRecordDto flightRecordDto) {
        try {
            if (isValidURL(flightRecordDto.getImage())) {
                FlightRecord flightRecord = convertToFlightRecord(flightRecordDto);
                return flightService.createFlight(flightRecord).flatMap(this::mapFlightRecordToRepresentation);
            } else {
                throw new IllegalArgumentException("L'URL de l'image fournie n'est pas valide.");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Une erreur est survenue lors de la validation de l'URL de l'image.");
        }
    }

    private FlightRecord convertToFlightRecord(FlightRecordDto flightRecordDto) {
        return FlightRecord.builder()
                .id(UUID.randomUUID())
                .departure(flightRecordDto.getDeparture())
                .arrival(flightRecordDto.getArrival())
                .price(flightRecordDto.getPrice())
                .origin(flightRecordDto.getOrigin())
                .destination(flightRecordDto.getDestination())
                .image(flightRecordDto.getImage())
                .build();

    }

    private boolean isValidURL(String url) throws MalformedURLException {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException | NullPointerException e) {
            throw new MalformedURLException("URL is not valid");        }
    }

    public Mono<FlightRepresentation> getFlightById(String flightId) {
        return flightService.getFlightById(flightId).flatMap(this::mapFlightRecordToRepresentation);
    }
}
