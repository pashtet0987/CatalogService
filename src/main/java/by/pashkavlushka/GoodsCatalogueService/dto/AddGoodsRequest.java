package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.Map;
import java.util.Objects;


public class AddGoodsRequest {
    private String id;
    private String name;
    private long sellerId;
    private String category;
    private Map<String, String> characteristics;
    private int cost;
    private int amount;

    public AddGoodsRequest() {
    }

    public AddGoodsRequest(String name, long sellerId, String category, Map<String, String> characteristics, int cost, int amount) {
        this.name = name;
        this.sellerId = sellerId;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.amount = amount;
    }

    public AddGoodsRequest(String id, String name, long sellerId, String category, Map<String, String> characteristics, int cost, int amount) {
        this.id = id;
        this.name = name;
        this.sellerId = sellerId;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.amount = amount;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, String> characteristics) {
        this.characteristics = characteristics;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + (int) (this.sellerId ^ (this.sellerId >>> 32));
        hash = 47 * hash + Objects.hashCode(this.category);
        hash = 47 * hash + Objects.hashCode(this.characteristics);
        hash = 47 * hash + this.cost;
        hash = 47 * hash + this.amount;
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
        final AddGoodsRequest other = (AddGoodsRequest) obj;
        if (this.sellerId != other.sellerId) {
            return false;
        }
        if (this.cost != other.cost) {
            return false;
        }
        if (this.amount != other.amount) {
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
