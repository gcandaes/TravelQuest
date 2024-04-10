package technical.test.renderer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import technical.test.renderer.dto.FlightRecordDto;
import technical.test.renderer.facades.FlightFacade;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class TechnicalController {

    @Autowired
    private FlightFacade flightFacade;

    @GetMapping
    public Mono<String> getMarketPlaceReturnCouponPage(final Model model) {
        model.addAttribute("flights", this.flightFacade.getFlights());
        return Mono.just("pages/index");
    }

    @GetMapping(path = "/addFlight")
    public Mono<String> getAddFlightPage(final Model model){
        model.addAttribute("flight", new FlightRecordDto());
        return Mono.just("pages/addFlight");
    }

    @PostMapping(
            path = "/addFlight"
    )
    public Mono<String> createFlight(@ModelAttribute("flight") FlightRecordDto flightRecordDto){
        flightFacade.createFlight(flightRecordDto);
        return Mono.just("pages/confirmationAddedFlight");

    }
}
