package com.ducnt.inventoryservice.services;

import com.ducnt.inventoryservice.dtos.InventoryResponse;
import com.ducnt.inventoryservice.models.Inventory;
import com.ducnt.inventoryservice.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream().map(this::toInventoryResponse)
                .collect(Collectors.toList());
    }

    private InventoryResponse toInventoryResponse(Inventory inventory) {
        return InventoryResponse
                .builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
