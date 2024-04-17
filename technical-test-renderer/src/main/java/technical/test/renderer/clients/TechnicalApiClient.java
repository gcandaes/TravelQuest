package technical.test.renderer.clients;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.renderer.dto.FlightRecordDto;
import technical.test.renderer.properties.TechnicalApiProperties;
import technical.test.renderer.viewmodels.FlightViewModel;

@Component
@Slf4j
public class TechnicalApiClient {

    private final TechnicalApiProperties technicalApiProperties;
    private final WebClient webClient;
    Logger log = LoggerFactory.getLogger(TechnicalApiClient.class);



    public TechnicalApiClient(TechnicalApiProperties technicalApiProperties, final WebClient.Builder webClientBuilder) {
        this.technicalApiProperties = technicalApiProperties;
        this.webClient = webClientBuilder.build();
    }

/*    public Flux<FlightViewModel> getFlights() {
        return webClient
                .get()
                .uri(technicalApiProperties.getUrl() + technicalApiProperties.getFlightPath())
                .retrieve()
                .bodyToFlux(FlightViewModel.class);
    }*/

       public Flux<FlightViewModel> getFlights(String size) {
           log.info("Entering TechnicalApiClient!");

           String urlWithParams = technicalApiProperties.getUrl() + technicalApiProperties.getFlightPath() + "?size=" + size;

           Flux<FlightViewModel> fligthFlux =  webClient
                   .get()
                   .uri(urlWithParams)
                   .retrieve()
                   .bodyToFlux(FlightViewModel.class);

           fligthFlux.subscribe(flight -> log.info(flight.toString()));
           log.info("Exiting TechnicalApiClient!");
           return fligthFlux;
    }



public Mono<FlightViewModel> createFlight(FlightRecordDto flightRecordDto) {

    Mono<FlightViewModel> response =  webClient.post()
            .uri("http://localhost:8086/flight")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(flightRecordDto)
            .retrieve()
            .bodyToMono(FlightViewModel.class);

    response.subscribe(System.out::println);

    return response;
}


    public Mono<FlightViewModel> getFlight(String flightId) {
        log.info("Entering NON-BLOCKING TechnicalApiClient!");

        String urlWithParams = technicalApiProperties.getUrl() + technicalApiProperties.getFlightPath() + "/" + flightId;

        Mono<FlightViewModel> fligthFlux =  webClient
                .get()
                .uri(urlWithParams)
                .retrieve()
                .bodyToMono(FlightViewModel.class);

        fligthFlux.subscribe(flight -> log.info(flight.toString()));
        log.info("Exiting NON-BLOCKING TechnicalApiClient!");
        return fligthFlux;
    }
}
