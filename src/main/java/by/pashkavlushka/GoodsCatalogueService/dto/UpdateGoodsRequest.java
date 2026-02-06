package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.Map;
import java.util.Objects;


public class UpdateGoodsRequest {
    private String id;
    private long itemId;
    private long sellerId;
    private int oldPrice;
    private int newPrice;
    private int toAddAmount;
    private Map<String, String> characteristics;
    private boolean status;

    public UpdateGoodsRequest() {
    }
    
    public UpdateGoodsRequest(long itemId, long sellerId, int oldPrice, int newPrice, int toAddAmount, Map<String, String> characteristics, boolean status) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.toAddAmount = toAddAmount;
        this.characteristics = characteristics;
        this.status = status;
    }

    public UpdateGoodsRequest(String id, long itemId, long sellerId, int oldPrice, int newPrice, int toAddAmount, Map<String, String> characteristics, boolean status) {
        this.id = id;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.toAddAmount = toAddAmount;
        this.characteristics = characteristics;
        this.status = status;
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

    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, String> characteristics) {
        this.characteristics = characteristics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + (int) (this.itemId ^ (this.itemId >>> 32));
        hash = 97 * hash + (int) (this.sellerId ^ (this.sellerId >>> 32));
        hash = 97 * hash + this.oldPrice;
        hash = 97 * hash + this.newPrice;
        hash = 97 * hash + this.toAddAmount;
        hash = 97 * hash + Objects.hashCode(this.characteristics);
        hash = 97 * hash + (this.status ? 1 : 0);
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
        final UpdateGoodsRequest other = (UpdateGoodsRequest) obj;
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
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.characteristics, other.characteristics);
    }
    
    
}
