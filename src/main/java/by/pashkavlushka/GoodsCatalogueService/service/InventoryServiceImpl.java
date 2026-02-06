package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {
    
    private GoodsService goodsService;

    @Autowired
    public InventoryServiceImpl(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Override
    public void addToInventory(GoodsDTO dto, Acknowledgment acknowledgment) { 
        goodsService.save(dto);
        acknowledgment.acknowledge();
    }
    
}
