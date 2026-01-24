/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    private final GoodsService goodsService;
    private final RestClient restClient;

    public CartController(GoodsService goodsService, RestClient restClient) {
        this.goodsService = goodsService;
        this.restClient = restClient;
    }
    
    @PostMapping("/")
    public boolean cart(@RequestParam("cartId") Long cartId
            , @RequestParam("itemId") Long itemId
            , @RequestParam("amount") int amount) throws EntityException {
        if(goodsService.addToCart(itemId, amount)) {
            return restClient.post().uri("http://cart_service/cart/").body(goodsService.createAddToCartRequest(cartId, itemId, amount))
                    .retrieve().toEntity(Boolean.class).getBody();
        }
        return false;
    }
}
