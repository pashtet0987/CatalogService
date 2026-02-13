package by.pashkavlushka.GoodsCatalogueService.async;

import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import org.springframework.kafka.core.KafkaTemplate;


public class AddFeedbackRunnable implements Runnable {
    
    private AddGoodsFeedback feedback;
    private KafkaTemplate<Long, AddGoodsFeedback> addGoodsFallbackTemplate;
    private AddGoodsRequest request;

    public AddFeedbackRunnable(AddGoodsFeedback feedback, KafkaTemplate<Long, AddGoodsFeedback> addGoodsFallbackTemplate, AddGoodsRequest request) {
        this.feedback = feedback;
        this.addGoodsFallbackTemplate = addGoodsFallbackTemplate;
        this.request = request;
    }

    @Override
    public void run() {
        addGoodsFallbackTemplate.send("inventory-add-feedback-topic", request.getSellerId(), feedback);
    }
    
}
