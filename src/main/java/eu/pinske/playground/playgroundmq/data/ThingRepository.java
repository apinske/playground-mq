package eu.pinske.playground.playgroundmq.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThingRepository extends JpaRepository<Thing, Long> {
}
