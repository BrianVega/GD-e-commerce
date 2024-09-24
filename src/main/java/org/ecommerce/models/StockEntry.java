package org.ecommerce.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.ecommerce.enums.MeasurementUnit;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockEntry <ProductID, LocationID> extends Identity {
    private ProductID productId;
    private LocationID locationId;
    private MeasurementUnit measurementUnit;
    private int stock;

}
