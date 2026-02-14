package by.pashkavlushka.GoodsCatalogueService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "to_handle_update", schema = "online_shop")
public class ToHandleUpdateEventEntity {
    @Id
    private String id;
    
    @Column(name = "item_id")
    private long itemId;
    
    @Column(name = "seller_id")
    private long sellerId;
    
    @Column(name = "old_price")
    private int oldPrice;
    
    @Column(name = "new_price")
    private int newPrice;
    
    @Column(name = "to_add_amount")
    private int toAddAmount;

    public ToHandleUpdateEventEntity() {
    }

    public ToHandleUpdateEventEntity(String id, long itemId, long sellerId, int oldPrice, int newPrice, int toAddAmount) {
        this.id = id;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.toAddAmount = toAddAmount;
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

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public int getToAddAmount() {
        return toAddAmount;
    }

    public void setToAddAmount(int toAddAmount) {
        this.toAddAmount = toAddAmount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + (int) (this.itemId ^ (this.itemId >>> 32));
        hash = 53 * hash + (int) (this.sellerId ^ (this.sellerId >>> 32));
        hash = 53 * hash + this.oldPrice;
        hash = 53 * hash + this.newPrice;
        hash = 53 * hash + this.toAddAmount;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ToHandleUpdateEventEntity other = (ToHandleUpdateEventEntity) obj;
        if (this.itemId != other.itemId) {
            return false;
        }
        if (this.sellerId != other.sellerId) {
            return false;
        }
        if (this.oldPrice != other.oldPrice) {
            return false;
        }
        if (this.newPrice != other.newPrice) {
            return false;
        }
        if (this.toAddAmount != other.toAddAmount) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
    
    
}
