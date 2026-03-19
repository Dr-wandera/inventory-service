package com.wanderaTech.inventory_service.Exception;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(String productId) {
        super("Inventory not found for productId: " + productId);
    }
}
