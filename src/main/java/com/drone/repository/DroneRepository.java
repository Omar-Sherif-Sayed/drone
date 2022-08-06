package com.drone.repository;

import com.drone.entity.Drone;
import com.drone.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    Drone findBySerial(String serial);

    List<Drone> findAllByState(DroneState state);
    
}
