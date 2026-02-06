package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
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
    
    public static Map<String, Object> consumerProperties(String kafkaBrokers, String groupId, Class<?> valueClass) {
        Map<String, Object> properties = new HashMap();
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        properties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class.getName());
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class.getName());
        properties.put(JacksonJsonDeserializer.KEY_DEFAULT_TYPE, Long.class);
        properties.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, valueClass.getName());
        properties.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Bean
    public ConsumerFactory<Long, AddGoodsRequest> kafkaAddConsumerFactory(@Value("${kafka.configuration.bootstrapServers}") String kafkaBrokers) {
        DefaultKafkaConsumerFactory<Long, AddGoodsRequest> factory = new DefaultKafkaConsumerFactory<>(consumerProperties(kafkaBrokers, "inventory", AddGoodsRequest.class));
        return factory;
    }
    
    @Bean
    public ConsumerFactory<Long, UpdateGoodsRequest> kafkaUpdateConsumerFactory(@Value("${kafka.configuration.bootstrapServers}") String kafkaBrokers) {
        DefaultKafkaConsumerFactory<Long, UpdateGoodsRequest> factory = new DefaultKafkaConsumerFactory<>(consumerProperties(kafkaBrokers, "update-inventory", UpdateGoodsRequest.class));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        ConsumerRecordRecoverer emptyRecoverer = new ConsumerRecordRecoverer() {
            @Override
            public void accept(ConsumerRecord<?, ?> t, Exception u) {
                u.printStackTrace();
            }
        };
        DefaultErrorHandler handler = new DefaultErrorHandler(emptyRecoverer);

        return handler;
    }

    @Bean
    @DependsOn({"kafkaAddConsumerFactory", "errorHandler"})
    public ConcurrentKafkaListenerContainerFactory<Long, AddGoodsRequest> kafkaListenerContainerFactory(@Qualifier("kafkaAddConsumerFactory") ConsumerFactory<Long, AddGoodsRequest> consumerFactory, DefaultErrorHandler handler) {
        ConcurrentKafkaListenerContainerFactory<Long, AddGoodsRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(handler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
    
    @Bean
    @DependsOn({"kafkaUpdateConsumerFactory", "errorHandler"})
    public ConcurrentKafkaListenerContainerFactory<Long, UpdateGoodsRequest> kafkaUpdateListenerContainerFactory(@Qualifier("kafkaUpdateConsumerFactory") ConsumerFactory<Long, UpdateGoodsRequest> consumerFactory, DefaultErrorHandler handler) {
        ConcurrentKafkaListenerContainerFactory<Long, UpdateGoodsRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(handler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public NewTopic inventoryTopic() {
        return new NewTopic("inventory-topic", 3, (short) 1);
    }
    
    @Bean
    public NewTopic updateInventoryTopic() {
        return new NewTopic("update-inventory-topic", 3, (short) 1);
    }
}
