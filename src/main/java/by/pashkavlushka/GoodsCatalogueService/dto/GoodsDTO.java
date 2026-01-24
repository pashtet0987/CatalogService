/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.List;
import java.util.Map;


public class GoodsDTO {
    private String name;
    private String sellerId;
    private String category;
    private Map<String, String> characteristics;
    private int cost;

    public GoodsDTO() {
    }

    public GoodsDTO(String name, String category, Map<String, String> characteristics, int cost, String sellerId) {
        this.name = name;
        this.category = category;
        this.characteristics = characteristics;
        this.cost = cost;
        this.sellerId = sellerId;
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
    
}
