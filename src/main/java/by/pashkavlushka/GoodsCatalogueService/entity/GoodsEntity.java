 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "goods", schema = "online_shop")
@SequenceGenerator(name = "goodsEntitySeq", sequenceName = "goods_entity_seq", schema = "online_shop", initialValue = 1, allocationSize = 1)
public class GoodsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goodsEntitySeq")
    private long id;
    
    @Version
    private long version;
    
    private String sellerId;
    
    private String name;
    
    private String category;
    
    @ElementCollection
    @CollectionTable(name = "characteristics", schema = "online_shop")
    @MapKeyColumn(name = "characteristic_name")
    @Column(name = "characteristic_value")
    private Map<String, String> characteristics;
    
    private int cost;
    
    private int amount;

    public GoodsEntity() {
    }

    public GoodsEntity(String name, String category, Map<String, String> characteristics, int cost, String sellerId) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    
}
