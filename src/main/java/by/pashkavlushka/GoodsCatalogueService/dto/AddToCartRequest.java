/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.dto;


public class AddToCartRequest {
    private Long cartId;
    private Long itemId;
    private String itemName;
    private int cost;
    private int amount;
    private boolean status;
    
    public AddToCartRequest() {
    }

    public AddToCartRequest(Long cartId, Long itemId, String itemName, int cost, int amount, boolean status) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.cost = cost;
        this.amount = amount;
        this.status = status;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
