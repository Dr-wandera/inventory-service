package com.wanderaTech.inventory_service.Controller;

import com.wanderaTech.inventory_service.InventoryDto.InventoryRestockRequest;
import com.wanderaTech.inventory_service.Model.Inventory;
import com.wanderaTech.inventory_service.Service.InventoryServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryServiceImplementation inventoryServiceImplementation;

    @PostMapping("/restock")
    public void restockProduct(@RequestBody InventoryRestockRequest restockRequest){
        inventoryServiceImplementation.restockProduct(restockRequest);

    }
    @GetMapping("/checkProduct/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getStock(@PathVariable String productId) {
        return inventoryServiceImplementation.getStock(productId);
    }

    @GetMapping("/checkStock/{productId}/{quantity}")
    public boolean checkStock(@PathVariable String productId, @PathVariable Integer quantity){
        return inventoryServiceImplementation.checkStockBeforeAddToCart(productId, quantity);
    }

}
