package com.wanderaTech.inventory_service.Service;

import com.wanderaTech.common_events.productEvent.ProductCreatedEvent;
import com.wanderaTech.common_events.productEvent.StockReduceEvent;
import com.wanderaTech.inventory_service.InventoryDto.InventoryResponse;
import com.wanderaTech.inventory_service.InventoryDto.InventoryRestockRequest;
import com.wanderaTech.inventory_service.InventoryDto.UpdateRequest;
import com.wanderaTech.inventory_service.Model.Inventory;
import com.wanderaTech.inventory_service.Repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImplementation  {

    private final InventoryRepository inventoryRepository;


    //this initializes the stock of product.
    @KafkaListener(
            topics = "initialStock-topic",
            groupId = "inventory-group"
    )

    @Transactional
    public void InitialStock(ProductCreatedEvent event) {

        Optional<Inventory> existing = inventoryRepository.findByProductId(event.getProductId());
        if (existing.isPresent()) {
            log.info("Received ProductCreatedEvent for productId={} name={} stock={}",
                    event.getProductId(), event.getProductName(), event.getStock());
            return;
        }

        try {
            Inventory inventory=new Inventory();

            inventory.setSellerId(event.getSellerId());
            inventory.setProductId(event.getProductId());
            inventory.setProductName(event.getProductName());
            inventory.setStock(event.getStock());
            inventoryRepository.save(inventory);
            log.info("Stock initialized for productId={}, stock={}", event.getProductId(), event.getStock());
        } catch (Exception e) {
            log.error("Failed to initialize stock for productId={}", event.getProductId(), e);
            // add deadlock queue optionally send message to DLQ
        }

    }

    //This restocks the product under seller
    @Transactional
    public void restockProduct(InventoryRestockRequest restockRequest) {
        com.wanderaTech.inventory_service.Model.Inventory inventory = inventoryRepository.findByProductId(restockRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found for productId=" + restockRequest.getProductId()));

        var updateStock=
                inventory.getStock()+restockRequest.getStock();
        inventory.setStock(updateStock);
        inventoryRepository.save(inventory);

    }

    //this checks the stock of the product in the inventory
    public Inventory getStock(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in inventory"));

    }

    //this initializes the stock of product.
    @KafkaListener(
            topics = "reduceStock-topic",
            groupId = "inventory-group"
    )

    @Transactional
    public void reduceStock(StockReduceEvent stockReduceEvent) {

        log.info("Stock deduction has started successful of {}", stockReduceEvent.getQuantity());
            Inventory inventory = inventoryRepository.findByProductId(stockReduceEvent.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found in inventory"));

            if (inventory.getStock() < stockReduceEvent.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            inventory.setStock(inventory.getStock() - stockReduceEvent.getQuantity());

            inventoryRepository.save(inventory);

        log.info("Product Stock decreased to {}", inventory.getStock());

    }


    @Transactional
    //this check the stock before adding the product in the cart
    public boolean checkStockBeforeAddToCart(String productId, int quantity) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in inventory"));

        return inventory.getStock() >= quantity;
    }
}
