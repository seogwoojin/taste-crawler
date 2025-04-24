package com.prography.restaurantscraper.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "raw_restaurant_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawRestaurantData {
    @Id
    private String id; // address + place name

    private String data; // "{ place }@@{ reviews }"
}
