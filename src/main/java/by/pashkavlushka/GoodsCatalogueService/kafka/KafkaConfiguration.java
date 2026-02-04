package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Configuration
public class KafkaConfiguration {
    
    @Bean
    public DefaultKafkaProducerFactory<Long, RecomendationDTO> kafkaProducerFactory(@Value("${kafka.configuration.bootstrapServers}") String bootstrapServers) {
        Map<String, Object> properties = new HashMap();
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.RETRIES_CONFIG, "3");
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "10000");
        DefaultKafkaProducerFactory<Long, RecomendationDTO> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(properties);
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<Long, RecomendationDTO> kafkaTemplate(DefaultKafkaProducerFactory<Long, RecomendationDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
