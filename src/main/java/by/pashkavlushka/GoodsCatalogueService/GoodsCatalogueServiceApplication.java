package by.pashkavlushka.GoodsCatalogueService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableJpaRepositories
public class GoodsCatalogueServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsCatalogueServiceApplication.class, args);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
    
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    @Bean
    @DependsOn("circuitBreakerRegistry")
    public CircuitBreaker recomendationsCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry){
        return circuitBreakerRegistry.circuitBreaker("recomendations-breaker", circuitBreakerConfig());
    }
    
    private CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig
                .custom()
                .enableAutomaticTransitionFromOpenToHalfOpen()
                .slidingWindow(10, 10, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED, CircuitBreakerConfig.SlidingWindowSynchronizationStrategy.LOCK_FREE)
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(30))
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .failureRateThreshold(50)
                .permittedNumberOfCallsInHalfOpenState(10)
                .minimumNumberOfCalls(1)
                .build();
    }
}
