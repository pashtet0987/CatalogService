/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    private final GoodsService goodsService;

    public CartController(GoodsService goodsService) {
        this.goodsService = goodsService;

    }
    
    @PostMapping("/")
    public AddToCartRequest cart(@RequestBody AddToCartRequest request) throws EntityException {
        AddToCartRequest validatedRequest = goodsService.validateAddToCartRequest(request);
        if(goodsService.addToCart(validatedRequest.getItemId(), validatedRequest.getAmount())) {
            request.setStatus(true);
        }
        return request;
    }
}
