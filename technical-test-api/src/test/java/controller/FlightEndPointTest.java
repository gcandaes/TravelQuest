package controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.TechnicalTestApiApplication;
import technical.test.api.dto.FlightRecordDto;
import technical.test.api.facade.FlightFacade;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TechnicalTestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightEndPointTest {

    @MockBean
    private FlightFacade flightFacade;

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void testGetAllFlights_OK() {
        // Given
        FlightRepresentation flight1 = new FlightRepresentation();
        FlightRepresentation flight2 = new FlightRepresentation();
        Flux<FlightRepresentation> expectedFlights = Flux.just(flight1, flight2);

        // Mocking
        when(flightFacade.getAllFlights(any(),any())).thenReturn(expectedFlights);

        // When
        webTestClient.get()
                .uri("/flight")
                .exchange()

                // Then
                .expectStatus().isOk()
                .expectBodyList(FlightRepresentation.class)
                .contains(flight1, flight2);
    }

    @Test
    public void testGetFlightById_OK() {
        // Given
        FlightRepresentation flight1 = new FlightRepresentation();
        Mono<FlightRepresentation> expectedFlight = Mono.just(flight1);
        String flightId = "bd93f677-6791-4734-b5fa-8063f79022f2";

        // Mocking
        when(flightFacade.getFlightById(flightId)).thenReturn(expectedFlight);

        // When
        webTestClient.get()
                .uri("/flight/{flightId}", flightId)
                .exchange()

                // Then
                .expectStatus().isOk()
                .expectBody(FlightRepresentation.class)
                .isEqualTo(flight1);
    }


    @Test
    public void testCreateFlight_OK() {
        // Given
        FlightRecordDto validFlightRecordDto = new FlightRecordDto(
                LocalDateTime.of(2024, Month.MARCH, 28, 14, 33),
                LocalDateTime.of(2024, Month.MARCH, 29, 14, 33),
                150.5,
                "LAX",
                "PEK",
                "https://www.hdwallpapers.in/download/high_resolution_flying_eagle_4k_8k_hd-3840x2160.jpg");


        FlightRepresentation flight1 = new FlightRepresentation();
        Mono<FlightRepresentation> expectedFlight = Mono.just(flight1);


        when(flightFacade.createFlight(any())).thenReturn(expectedFlight);

        // When
        webTestClient.post()
                .uri("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validFlightRecordDto)
                .exchange()

                // Then
                .expectStatus().isOk();
    }

    @Test
    public void testCreateFlight_badRequest() {
        // Given
        FlightRecordDto invalidFlightRecordDto = new FlightRecordDto(/* fill with invalid data */);
        when(flightFacade.createFlight(any())).thenThrow(new IllegalArgumentException("Invalid URL"));

        // When
        webTestClient.post()
                .uri("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidFlightRecordDto)
                .exchange()

                // Then
                .expectStatus().isBadRequest();
    }
}