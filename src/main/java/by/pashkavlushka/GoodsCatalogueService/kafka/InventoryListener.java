package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.async.AddUpdateFallbackRequestExecutor;
import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    private GoodsService goodsService;
    private KafkaTemplate<String, AddGoodsFeedback> addGoodsFallbackTemplate;
    private KafkaTemplate<String, UpdateGoodsFeedback> updateGoodsFallbackTemplate;

    @Autowired
    public InventoryListener(GoodsService inventoryService,
            KafkaTemplate<String, AddGoodsFeedback> addGoodsFallbackTemplate,
            KafkaTemplate<String, UpdateGoodsFeedback> updateGoodsFallbackTemplate) {
        this.goodsService = inventoryService;
        this.addGoodsFallbackTemplate = addGoodsFallbackTemplate;
        this.updateGoodsFallbackTemplate = updateGoodsFallbackTemplate;
    }

    @KafkaListener(topics = {"add-inventory-topic"}, groupId = "inventory", containerFactory = "kafkaListenerContainerFactory", autoStartup = "true")
    public void addToInventory(AddGoodsRequest dto, Acknowledgment acknowledgment) {
        AddUpdateFallbackRequestExecutor.submitAddTaskFallback(() -> {
            AddGoodsFeedback feedback = goodsService.addToInventory(dto, acknowledgment);
            addGoodsFallbackTemplate.send("inventory-add-feedback-topic", feedback.getId(), feedback);
        });

    }

    @KafkaListener(topics = {"update-inventory-topic"}, groupId = "update-inventory", containerFactory = "kafkaUpdateListenerContainerFactory", autoStartup = "true")
    public void updateGoodsListener(UpdateGoodsRequest request, Acknowledgment ack) {
        AddUpdateFallbackRequestExecutor.submitUpdateTaskFallback(() -> {
            UpdateGoodsFeedback feedback = goodsService.updateInventory(request, ack);
            updateGoodsFallbackTemplate.send("inventory-update-feedback-topic", feedback.getId(), feedback);
        });
        
    }
}
