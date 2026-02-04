/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.repository.GoodsRepository;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private GoodsRepository goodsRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeAll
    public static void setProfile(){
        System.setProperty("spring.profiles.active", "test");
    }
    
    @BeforeEach
    public void insertDataIntoDatabase() {

        if (goodsRepository.findById(1).isPresent()) {
            goodsRepository.findById(1L).ifPresent((proc) -> {
                proc.setAmount(100);
                goodsRepository.save(proc);
            });
            goodsRepository.findById(2L).ifPresent((vid) -> {
                vid.setAmount(100);
                goodsRepository.save(vid);
            });
        } else {
            GoodsEntity processor = new GoodsEntity("Процессор", "electronics", Map.of(), 4000, 1, 100);
            GoodsEntity videoCard = new GoodsEntity("Видеокарта", "electronics", Map.of(), 2000, 1, 100);
            processor = goodsRepository.save(processor);
            videoCard = goodsRepository.save(videoCard);
        }
    }
    
    @Test
    public void addToCart_singleRequest_Success() throws Exception{
        AddToCartRequest request = new AddToCartRequest(1L, 1L, "Процессор", 4000, 10, false);
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true));
    }
    
    @Test
    public void addToCart_manyRequests_Success() throws Exception{
        AddToCartRequest request = new AddToCartRequest(1L, 1L, "Процессор", 4000, 1, false);
        ExecutorService service = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(10);
        Runnable task = ()->{
            try {
                latch.countDown();
                latch.await();
                mockMvc.perform(MockMvcRequestBuilders.post("/cart/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true));
            } catch (Exception ex) {
                System.getLogger(CartControllerTest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        };
        
        for(int i = 0; i < 100; i++) {
            service.submit(task);
        }
        service.shutdown();
        while(!service.isTerminated()){}
        Assertions.assertEquals(goodsRepository.findById(1L).get().getAmount(), 0);
    }
}
