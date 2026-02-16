package by.pashkavlushka.GoodsCatalogueService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "to_delete_requests", schema = "online_shop")
public class DeleteGoodsRequestEntity {
    @Id
    private String id;
    
    @Column(name = "item_id")
    private long itemId;
    
    @Column(name = "seller_id")
    private long sellerId;

    public DeleteGoodsRequestEntity() {
    }

    public DeleteGoodsRequestEntity(String id, long itemId, long sellerId) {
        this.id = id;
        this.itemId = itemId;
        this.sellerId = sellerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }
}
