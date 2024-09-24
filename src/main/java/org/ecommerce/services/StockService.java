package org.ecommerce.services;

import org.ecommerce.models.StockEntry;

public interface StockService {
    void createStockEntry(StockEntry<Long, Long> stockEntry);
    StockEntry<Long, Long> getStock(Long productId, Long locationId);
    void addStock(StockEntry<Long, Long> stockEntry, int quantityToAdd);
    void takeStock(StockEntry<Long, Long> stockEntry, int quantityToTake);
}
