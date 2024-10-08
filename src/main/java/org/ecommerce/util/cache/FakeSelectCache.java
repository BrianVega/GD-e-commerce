package org.ecommerce.util.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// This class simulates a cache implementation when getting an order
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FakeSelectCache <T> {
    private LocalDateTime dateTime;
    private T data;
}
