package com.ecart.inventory_service.controller;

import com.ecart.inventory_service.dto.InventoryResponse;
import com.ecart.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("skuCodes") List<String> skuCodes) {
        return inventoryService.isInStock(skuCodes);
    }
}
