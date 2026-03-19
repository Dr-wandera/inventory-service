package com.wanderaTech.inventory_service.InventoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryResponse {

    private String productId;
    private Integer stock;
}

