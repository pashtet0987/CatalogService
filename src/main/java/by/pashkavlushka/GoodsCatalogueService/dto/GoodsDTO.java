package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.List;
import java.util.Map;


public class GoodsDTO {
    private long id;
    private String name;
    private long sellerId;
    private String category;
    private Map<String, String> characteristics;
    private int cost;
    private int amount;
    
    public GoodsDTO() {
    }

    public GoodsDTO(String name, String category, Map<String, String> characteristics, int cost, long sellerId, int amount) {
        this.name = name;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.sellerId = sellerId;
        this.amount = amount;
    }

    public GoodsDTO(long id, String name, long sellerId, String category, Map<String, String> characteristics, int cost, int amount) {
        this.id = id;
        this.name = name;
        this.sellerId = sellerId;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }
}
