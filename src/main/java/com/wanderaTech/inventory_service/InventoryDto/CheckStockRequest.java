package com.wanderaTech.inventory_service.InventoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckStockRequest {
    private Integer quantity;
}
