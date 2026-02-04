/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.repository.GoodsRepository;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsServiceImpl;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class GoodsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private GoodsRepository goodsRepository;
    
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
    public void byCategory() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/electronics?userId=1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }
    
    @Test
    public void bySeller() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/by-seller?sellerId=1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }
}
