/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.repository.GoodsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.DataSource;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

//set profile to test
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GoodsServiceTest {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsService goodsService;

    
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
    public void addToCart_singleThread_Success() throws EntityException {
        Assertions.assertTrue(goodsService.addToCart(1L, 10));
        GoodsDTO dto = goodsService.findById(1L);
        Assertions.assertEquals(dto.getAmount(), 90);
    }

    @Test
    public void addToCart_manyThreads_Success() throws EntityException {
        ExecutorService executor = Executors.newFixedThreadPool(80);

        
        CountDownLatch latch = new CountDownLatch(80);
        Runnable runnable = () -> {
            try {
                latch.countDown();
                latch.await();
                goodsService.addToCart(1L, 1);
            } catch (InterruptedException ex) {
                System.getLogger(GoodsServiceTest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        };

        for (int i = 0; i < 80; i++) {
            executor.execute(runnable);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        GoodsDTO dto = goodsService.findById(1L);

        Assertions.assertEquals(20, dto.getAmount());
    }

    @Test
    public void addToCart_manyThreads_NotEnoughAmount() throws EntityException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        CountDownLatch latch = new CountDownLatch(10);
        AtomicBoolean forRes = new AtomicBoolean(true);
        Runnable runnable = () -> {
            try {
                latch.countDown();
                latch.await();
                //only last thread will set false, cause after 9 threads, amount will be equal to 1 (100 - 9 * 11)
                forRes.compareAndExchange(true, goodsService.addToCart(1L, 11));
            } catch (InterruptedException ex) {
                System.getLogger(GoodsServiceTest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        };

        for (int i = 0; i < 10; i++) {
            executor.execute(runnable);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        GoodsDTO dto = goodsService.findById(1L);

        Assertions.assertEquals(false, forRes.get());
        Assertions.assertEquals(1, dto.getAmount());
    }

    @Test
    public void addToCart_singleThread_EntityNotFound() {
        Assertions.assertEquals(false, goodsService.addToCart(5L, 10));

    }
    
    @Test
    public void findBySellerId_Success(){
        List<GoodsDTO> bySeller = goodsService.findBySellerId(1);
        Assertions.assertEquals(2, bySeller.size());
    }
    
    @Test
    public void findByCategory_Success(){
        List<GoodsDTO> bySeller = goodsService.findByCategory("electronics");
        Assertions.assertEquals(2, bySeller.size());
    }
    
    @Test
    public void findByCategory_categoryDoesNotExist(){
        List<GoodsDTO> bySeller = goodsService.findByCategory("keyboards");
        Assertions.assertEquals(0, bySeller.size());
    }
}
