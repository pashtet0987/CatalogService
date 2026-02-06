/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.Map;


public class AddGoodsRequest {
    private String id;
    private String name;
    private long sellerId;
    private String category;
    private Map<String, String> characteristics;
    private int cost;
    private int amount;
    private boolean status;

    public AddGoodsRequest() {
    }

    public AddGoodsRequest(String name, long sellerId, String category, Map<String, String> characteristics, int cost, int amount, boolean status) {
        this.name = name;
        this.sellerId = sellerId;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.amount = amount;
        this.status = status;
    }

    public AddGoodsRequest(String id, String name, long sellerId, String category, Map<String, String> characteristics, int cost, int amount, boolean status) {
        this.id = id;
        this.name = name;
        this.sellerId = sellerId;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.amount = amount;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
}
