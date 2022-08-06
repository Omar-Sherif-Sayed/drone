package com.drone.dto;

import com.drone.enums.DroneState;

public record DroneDto(Long id,
                       String serial,
                       DroneState state,
                       Long batteryCapacity,
                       DroneModelDto modelDto) {
}
