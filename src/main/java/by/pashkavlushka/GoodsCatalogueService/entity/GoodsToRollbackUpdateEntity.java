package by.pashkavlushka.GoodsCatalogueService.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "to_handle_update", schema = "online_shop")
public class GoodsToRollbackUpdateEntity {
    @Id
    private String id;
    
    @Column(name = "item_id")
    private long itemId;
    
    @Column(name = "seller_id")
    private long sellerId;
    
    private String name;
    
    private String category;
    
    private int cost;
    
    @Column(name = "to_add_amount")
    private int toAddAmount;
    
    @ElementCollection
    @CollectionTable(name = "characteristics_to_rollback", schema = "online_shop")
    @MapKeyColumn(name = "characteristic_name")
    @Column(name = "characteristic_value")
    private Map<String, String> characteristics;

    public GoodsToRollbackUpdateEntity() {
    }

    public GoodsToRollbackUpdateEntity(long itemId, long sellerId, String name, String category, int cost, int toAddAmount, Map<String, String> characteristics) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.toAddAmount = toAddAmount;
        this.characteristics = characteristics;
    }

    public GoodsToRollbackUpdateEntity(String id, long itemId, long sellerId, String name, String category, int cost, int toAddAmount, Map<String, String> characteristics) {
        this.id = id;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.toAddAmount = toAddAmount;
        this.characteristics = characteristics;
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

    public int getToAddAmount() {
        return toAddAmount;
    }

    public void setToAddAmount(int toAddAmount) {
        this.toAddAmount = toAddAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, String> characteristics) {
        this.characteristics = characteristics;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + (int) (this.itemId ^ (this.itemId >>> 32));
        hash = 53 * hash + (int) (this.sellerId ^ (this.sellerId >>> 32));
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.category);
        hash = 53 * hash + this.cost;
        hash = 53 * hash + this.toAddAmount;
        hash = 53 * hash + Objects.hashCode(this.characteristics);
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
        final GoodsToRollbackUpdateEntity other = (GoodsToRollbackUpdateEntity) obj;
        if (this.itemId != other.itemId) {
            return false;
        }
        if (this.sellerId != other.sellerId) {
            return false;
        }
        if (this.cost != other.cost) {
            return false;
        }
        if (this.toAddAmount != other.toAddAmount) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        return Objects.equals(this.characteristics, other.characteristics);
    }

    
    
    
}
