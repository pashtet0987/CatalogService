/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    
    private GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }
    
    @GetMapping("/{category}")
    public List<GoodsDTO> findByCategory(@PathVariable("category") String category){
        return goodsService.findByCategory(category);
    }
    
    @GetMapping("/")
    //сделать circuit breaker и fallback(дефолтный ответ)
    public List<GoodsDTO> recomendations(){
        return null;//переопределить с сервисом рекомендаций
    }
    
    @GetMapping("/by-seller")
    public List<GoodsDTO> findBySeller(@RequestParam("sellerId") Long sellerId){
        return goodsService.findBySellerId(sellerId);
    }
}
