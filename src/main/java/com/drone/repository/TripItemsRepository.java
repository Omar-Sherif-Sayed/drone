package com.drone.repository;

import com.drone.entity.TripItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripItemsRepository extends JpaRepository<TripItems, Long> {

}
