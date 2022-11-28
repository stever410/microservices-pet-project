package com.ducnt.inventoryservice.repositories;

import com.ducnt.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
