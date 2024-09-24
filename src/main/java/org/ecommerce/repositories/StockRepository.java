package org.ecommerce.repositories;

import org.ecommerce.enums.MeasurementUnit;
import org.ecommerce.models.StockEntry;
import org.ecommerce.util.database.Operations;

import java.sql.Connection;
import java.sql.SQLException;

public class StockRepository {
//    private final Map<Long, Map<Long, StockEntry<Long, Long>>> stockStore = new HashMap<>();

    private final Operations<StockEntry> operations;

    public StockRepository(Operations<StockEntry> operations) {
        this.operations = operations;
    }


    public void addStockEntry(StockEntry<Long, Long> stockEntry) throws SQLException {
//        stockStore
//                .computeIfAbsent(stockEntry.getProductId() , k -> new HashMap<>())
//                .put(stockEntry.getLocationId(), stockEntry);
        String query = "INSERT INTO stock(fk_storage_center_id, fk_inventory_id, measurement_unit, quantity) VALUES (?, ?, ?, ?)";
            operations.execute(query,
                    stockEntry.getLocationId(),
                    stockEntry.getProductId(),
                    stockEntry.getMeasurementUnit(),
                    stockEntry.getStock());
        }

    public StockEntry<Long, Long> getStockEntry(Long productId, Long locationId) throws SQLException {
//        return stockStore.getOrDefault(productId, Collections.emptyMap()).get(locationId);
        operations.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        String query = "SELECT measurement_unit, quantity FROM stock WHERE fk_inventory_id = ? AND fk_storage_center_id = ?";
        return operations.findOne(query,
                resultSet -> {
                    try {
                        resultSet.next();
                        return StockEntry.builder()
                                .productId(productId)
                                .locationId(locationId)
                                .measurementUnit(MeasurementUnit.valueOf(resultSet.getString(1).toUpperCase()))
                                .stock(resultSet.getInt(2))
                                .build();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, productId, locationId);
    }

    public void updateStockEntry(StockEntry<Long, Long> stockEntry, int newQuantity) throws SQLException {
//        if (stockStore.containsKey(stockEntry.getProductId())) {
//            stockStore.get(stockEntry.getProductId()).put(stockEntry.getLocationId(), stockEntry);
//        }
        String query = "UPDATE stock SET quantity = ? WHERE fk_inventory_id = ? AND fk_storage_center_id = ?";
        operations.execute(query, newQuantity, stockEntry.getProductId(), stockEntry.getLocationId());
    }
}
