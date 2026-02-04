/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private KafkaTemplate<Long, RecomendationDTO> kafkaTemplate;
    private Random rndm;
    private int sampling;
    private String topic;

    @Autowired
    public KafkaService(KafkaTemplate<Long, RecomendationDTO> kafkaTemplate,
            @Value("${kafka.configuration.recomendations.sampling}") int sampling,
            @Value("${kafka.configuration.recomendations.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        rndm = new Random();
        this.sampling = sampling;
        this.topic = topic;
    }

    public void addRecomendation(Long userId, String category) {
        if (rndm.nextInt(0, 101) <= sampling) {
            CompletableFuture.runAsync(()->kafkaTemplate.send(topic, userId, new RecomendationDTO(userId, category)));
        }
    }
}
