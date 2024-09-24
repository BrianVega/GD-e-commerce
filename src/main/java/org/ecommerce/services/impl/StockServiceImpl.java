package org.ecommerce.services.impl;

import org.ecommerce.enums.Error;
import org.ecommerce.exceptions.EntityNotFound;
import org.ecommerce.exceptions.InsertionException;
import org.ecommerce.exceptions.OutOfStock;
import org.ecommerce.exceptions.UpdateException;
import org.ecommerce.logs.Log;
import org.ecommerce.models.StockEntry;
import org.ecommerce.repositories.StockRepository;
import org.ecommerce.services.StockService;

public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void createStockEntry(StockEntry<Long, Long> stockEntry) {
        try {
            stockRepository.addStockEntry(stockEntry);
        } catch (Exception e) {
            throw new InsertionException(Error.INSERTION_ERROR.getDescription(), e);
        }
    }

    @Override
    public StockEntry<Long, Long> getStock(Long productId, Long locationId) {
        try {
            StockEntry<Long, Long> stockEntry = stockRepository.getStockEntry(productId, locationId);
            if (stockEntry.getStock() == 1) {
                Thread.sleep(1000);
            }
            return stockEntry;
        } catch (Exception e) {
            throw new EntityNotFound(Error.ENTITY_NOT_FOUND.getDescription(), e);
        }
    }

    @Override
    public void addStock(StockEntry<Long, Long> stockEntry, int quantityToAdd) {
        try {
            int currentStock = getStock(stockEntry.getProductId(), stockEntry.getLocationId()).getStock();
            stockRepository.updateStockEntry(stockEntry, currentStock + quantityToAdd);
        } catch (Exception e) {
            throw new UpdateException(Error.EXTRACTION_ERROR.getDescription(), e);
        }
    }

    @Override
    public void takeStock(StockEntry<Long, Long> stockEntry, int quantityToTake) {
        try {
            int currentStock = getStock(stockEntry.getProductId(), stockEntry.getLocationId()).getStock();
            Log.info(Thread.currentThread().getName() + " - Current Stock: " + currentStock);
            if (currentStock == 0) {
                throw new OutOfStock(Error.PRODUCT_OUT_OF_STOCK.getDescription());
            } else if (currentStock - quantityToTake < 0) {
                Log.info("Current stock is " + currentStock + " and quantity is " + quantityToTake);
            } else {
                stockRepository.updateStockEntry(stockEntry, currentStock - quantityToTake);
            }

            stockRepository.updateStockEntry(stockEntry, currentStock - quantityToTake);
        } catch (Exception e) {
            throw new UpdateException(Error.INSERTION_ERROR.getDescription(), e);
        }
    }
}

