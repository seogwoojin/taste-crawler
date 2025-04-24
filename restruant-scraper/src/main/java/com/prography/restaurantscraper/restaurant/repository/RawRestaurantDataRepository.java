package com.prography.restaurantscraper.restaurant.repository;

import com.prography.restaurantscraper.restaurant.domain.RawRestaurantData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RawRestaurantDataRepository extends MongoRepository<RawRestaurantData, String> {
}
