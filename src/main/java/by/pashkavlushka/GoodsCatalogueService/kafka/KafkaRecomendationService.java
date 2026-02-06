package by.pashkavlushka.GoodsCatalogueService.kafka;


public interface KafkaRecomendationService {
    public void addRecomendation(Long userId, String category);
}
