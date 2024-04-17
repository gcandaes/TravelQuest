package technical.test.api.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import technical.test.api.record.FlightRecord;

import org.springframework.data.domain.Pageable;
import java.util.UUID;
import reactor.core.publisher.Flux;

@Repository
public interface FlightRepository extends ReactiveMongoRepository<FlightRecord, UUID> {
    Flux<FlightRecord> findAllBy(Pageable pageable);
    Flux<FlightRecord> findByOriginContaining(String origin, Pageable pageable);
}
