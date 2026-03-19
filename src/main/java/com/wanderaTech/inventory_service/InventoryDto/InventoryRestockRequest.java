package com.wanderaTech.inventory_service.InventoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRestockRequest {
    private String productId;
    private String sellerId;
    private String productName;
    private Integer stock;
}

