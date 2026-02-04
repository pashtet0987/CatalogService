package by.pashkavlushka.GoodsCatalogueService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
    public RestClient restClient(){
        return RestClient.builder().build();
    }
}
