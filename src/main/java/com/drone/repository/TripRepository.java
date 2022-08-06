package com.drone.repository;

import com.drone.entity.Trip;
import com.drone.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    Trip findByDroneIdAndDroneStateAndEndTimeIsNull(Long droneId, DroneState droneState);

}
