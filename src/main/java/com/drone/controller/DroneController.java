package com.drone.controller;

import com.drone.dto.DroneDto;
import com.drone.model.CreateDroneRequest;
import com.drone.response.BaseResponse;
import com.drone.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    @PostMapping
    public ResponseEntity<BaseResponse<DroneDto>> registerDrone(@RequestBody CreateDroneRequest createDroneRequest) {
        return ResponseEntity.ok(droneService.registerDrone(createDroneRequest)
                .fold(left -> new BaseResponse<>(false, left, null),
                        right -> new BaseResponse<>(true, null, right)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<DroneDto>>> findAll() {
        return ResponseEntity.ok(new BaseResponse<>(true, null, droneService.findAll()));
    }

    /**
     * find all information about specific drone <br>
     * (id, name, serial, state, batteryCapacity, model)
     *
     * @param id Drone Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DroneDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, null, droneService.findById(id)));
    }

    @GetMapping("/available-drones-for-loading")
    public ResponseEntity<BaseResponse<List<DroneDto>>> availableDronesForLoading() {
        return ResponseEntity.ok(new BaseResponse<>(true, null, droneService.availableDronesForLoading()));
    }

    @PatchMapping("/next-drone-state")
    public ResponseEntity<BaseResponse<DroneDto>> nextDroneState(@RequestParam Long id) {
        return ResponseEntity.ok(droneService.nextDroneState(id)
                .fold(left -> new BaseResponse<>(false, left, null),
                        right -> new BaseResponse<>(true, null, right)));
    }

    @PatchMapping("/recharge-battery-capacity")
    public ResponseEntity<BaseResponse<DroneDto>> rechargeBatteryCapacity(@RequestParam Long id) {
        return ResponseEntity.ok(droneService.rechargeBatteryCapacity(id)
                .fold(left -> new BaseResponse<>(false, left, null),
                        right -> new BaseResponse<>(true, null, right)));
    }


}
