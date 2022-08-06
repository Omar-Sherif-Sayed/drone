package com.drone.repository;

import com.drone.entity.DroneModel;
import com.drone.enums.DroneModelName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneModelRepository extends JpaRepository<DroneModel, Long> {

    DroneModel findByName(DroneModelName name);

    @Query("SELECT MAX(maxWeightLimit) FROM DroneModel")
    Long findMaxMaxWeightLimit();

}
