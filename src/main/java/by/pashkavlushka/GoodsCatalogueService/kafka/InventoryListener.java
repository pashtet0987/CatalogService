package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.async.AddFeedbackRunnable;
import by.pashkavlushka.GoodsCatalogueService.async.DeleteFeedbackRunnable;
import by.pashkavlushka.GoodsCatalogueService.async.FeedbackRequestExecutor;
import by.pashkavlushka.GoodsCatalogueService.async.UpdateFeedbackRunnable;
import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.DeleteGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.DeleteGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.entity.HandledAddEventEntity;
import by.pashkavlushka.GoodsCatalogueService.entity.HandledUpdateEventEntity;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsToRollbackUpdateEntity;
import by.pashkavlushka.GoodsCatalogueService.mapstruct.UpdateRequestMapper;
import by.pashkavlushka.GoodsCatalogueService.repository.HandledAddEventRepository;
import by.pashkavlushka.GoodsCatalogueService.repository.HandledUpdateEventRepository;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import by.pashkavlushka.GoodsCatalogueService.repository.RollbackUpdateEventRepository;
import java.time.Duration;

@Component
public class InventoryListener {

    private GoodsService goodsService;
    private KafkaTemplate<Long, AddGoodsFeedback> addGoodsFallbackTemplate;
    private KafkaTemplate<Long, UpdateGoodsFeedback> updateGoodsFallbackTemplate;
    private KafkaTemplate<Long, DeleteGoodsFeedback> deleteGoodsFallbackTemplate;
    private KafkaTemplate<Long, UpdateGoodsRequest> updateGoodsRequestTemplate;
    private HandledAddEventRepository addRepository;
    private HandledUpdateEventRepository handledUpdateRepository;
    private RollbackUpdateEventRepository toUpdateRepository;
    private UpdateRequestMapper updateRequestMapper;

    @Autowired
    public InventoryListener(GoodsService inventoryService,
            KafkaTemplate<Long, AddGoodsFeedback> addGoodsFallbackTemplate,
            KafkaTemplate<Long, UpdateGoodsFeedback> updateGoodsFallbackTemplate,
            KafkaTemplate<Long, DeleteGoodsFeedback> deleteGoodsFallbackTemplate,
            KafkaTemplate<Long, UpdateGoodsRequest> updateGoodsRequestTemplate,
            HandledAddEventRepository addRepository,
            HandledUpdateEventRepository updateRepository,
            RollbackUpdateEventRepository toUpdateRepository,
            UpdateRequestMapper updateRequestMapper) {
        this.goodsService = inventoryService;
        this.addGoodsFallbackTemplate = addGoodsFallbackTemplate;
        this.updateGoodsFallbackTemplate = updateGoodsFallbackTemplate;
        this.addRepository = addRepository;
        this.handledUpdateRepository = updateRepository;
        this.deleteGoodsFallbackTemplate = deleteGoodsFallbackTemplate;
        this.updateGoodsRequestTemplate = updateGoodsRequestTemplate;
        this.toUpdateRepository = toUpdateRepository;
        this.updateRequestMapper = updateRequestMapper;
    }

    @KafkaListener(topics = {"add-inventory-topic"}, groupId = "inventory", containerFactory = "kafkaListenerContainerFactory", autoStartup = "true")
    public void addToInventory(AddGoodsRequest request, Acknowledgment acknowledgment) {
        //default is bad
        AddGoodsFeedback feedback = new AddGoodsFeedback(request.getId(), false, false);
        if (!addRepository.existsById(request.getId())) {
            feedback = goodsService.addToInventory(request, acknowledgment);
            if (feedback.isStatus()) {
                addRepository.save(new HandledAddEventEntity(request.getId()));
            }
        }
        FeedbackRequestExecutor.submitAddTaskFeedback(new AddFeedbackRunnable(feedback, addGoodsFallbackTemplate, request.getSellerId()));

    }

    @KafkaListener(topics = {"update-inventory-topic"}, groupId = "update-inventory", containerFactory = "kafkaUpdateListenerContainerFactory", autoStartup = "true")
    public void updateGoodsListener(UpdateGoodsRequest request, Acknowledgment ack) {
        //default is bad
        UpdateGoodsFeedback feedback = new UpdateGoodsFeedback(request.getId(), false, false);
        if (!handledUpdateRepository.existsById(request.getId())) {
            feedback = goodsService.updateInventory(request, ack);
            if (feedback.isStatus()) {
                //proceed to cart service
                handledUpdateRepository.save(new HandledUpdateEventEntity(request.getId()));
                updateGoodsRequestTemplate.send("update-cart-inventory-topic", request.getSellerId(), request);
                return;
            }
        }
        //else send bac feedback
        FeedbackRequestExecutor.submitUpdateTaskFeedback(new UpdateFeedbackRunnable(feedback, updateGoodsFallbackTemplate, request.getSellerId()));
    }

    @KafkaListener(topics = {"delete-inventory-topic"}, groupId = "delete-inventory", containerFactory = "kafkaDeleteListenerContainerFactory", autoStartup = "true")
    public void deleteGoodsListener(DeleteGoodsRequest request, Acknowledgment ack) {
        DeleteGoodsFeedback feedback = goodsService.deleteFromInventory(request, ack);
        FeedbackRequestExecutor.submitDeleteTaskFeedback(new DeleteFeedbackRunnable(feedback, deleteGoodsFallbackTemplate, request.getSellerId()));
    }

    @KafkaListener(topics = {"inventory-update-cart-feedback-topic"}, groupId = "update-cart-inventory", containerFactory = "kafkaUpdateCartFeedbackListenerContainerFactory", autoStartup = "true")
    public void updateGoodsCartFeedbackListener(UpdateGoodsFeedback feedback, Acknowledgment ack) {
        if (handledUpdateRepository.existsById(feedback.getId())) {
            //на случай если null при рестарте приложения, когда время хранения обработки данного сообщения вышло
            Long sellerId = toUpdateRepository.findSellerIdById(feedback.getId());
            if (!feedback.isStatus()) {
                goodsService.rollbackUpdateInventory(feedback.getId(), ack);
                handledUpdateRepository.deleteById(feedback.getId());
            }
            
            toUpdateRepository.deleteById(feedback.getId());

            FeedbackRequestExecutor.submitUpdateTaskFeedback(new UpdateFeedbackRunnable(feedback, updateGoodsFallbackTemplate, sellerId == null ? 0L : sellerId));
        }
    }
}
