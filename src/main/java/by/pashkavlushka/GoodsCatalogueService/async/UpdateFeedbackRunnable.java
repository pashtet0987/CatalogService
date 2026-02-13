package by.pashkavlushka.GoodsCatalogueService.async;

import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import org.springframework.kafka.core.KafkaTemplate;


public class UpdateFeedbackRunnable implements Runnable {
    
    private UpdateGoodsFeedback feedback;
    private KafkaTemplate<Long, UpdateGoodsFeedback> updateGoodsFallbackTemplate;
    private UpdateGoodsRequest request;
    
    public UpdateFeedbackRunnable(UpdateGoodsFeedback feedback, KafkaTemplate<Long, UpdateGoodsFeedback> updateGoodsFallbackTemplate, UpdateGoodsRequest request) {
        this.feedback = feedback;
        this.updateGoodsFallbackTemplate = updateGoodsFallbackTemplate;
        this.request = request;
    }

    @Override
    public void run() {
        updateGoodsFallbackTemplate.send("inventory-update-feedback-topic", request.getSellerId(), feedback);
    }
    
}
