package by.pashkavlushka.GoodsCatalogueService.async;

import by.pashkavlushka.GoodsCatalogueService.dto.DeleteGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.DeleteGoodsRequest;
import org.springframework.kafka.core.KafkaTemplate;

public class DeleteFeedbackRunnable implements Runnable{

    private DeleteGoodsFeedback feedback;
    private KafkaTemplate<Long, DeleteGoodsFeedback> deleteGoodsFallbackTemplate;
    private long sellerId;

    public DeleteFeedbackRunnable() {
    }

    public DeleteFeedbackRunnable(DeleteGoodsFeedback feedback, KafkaTemplate<Long, DeleteGoodsFeedback> deleteGoodsFallbackTemplate, long sellerId) {
        this.feedback = feedback;
        this.deleteGoodsFallbackTemplate = deleteGoodsFallbackTemplate;
        this.sellerId = sellerId;
    }
    
    @Override
    public void run() {
        deleteGoodsFallbackTemplate.send("inventory-delete-feedback-topic", sellerId, feedback);
    }
}
