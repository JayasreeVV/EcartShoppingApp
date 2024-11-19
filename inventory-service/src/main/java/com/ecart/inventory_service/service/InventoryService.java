package com.ecart.inventory_service.service;

import com.ecart.inventory_service.dto.InventoryResponse;
import com.ecart.inventory_service.repository.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        log.info("wait started");
        //Thread.sleep(10000);
        log.info("wait ended");
        return  inventoryRepository.findBySkuCodeIn(skuCodes).stream().map(inventory ->
                InventoryResponse.builder().inStock(inventory.getQuantity()>0)
                .skuCode(inventory.getSkuCode())
                .build())
                .toList();
    }
}
