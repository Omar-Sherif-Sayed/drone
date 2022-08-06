package com.drone.dto;

import com.drone.enums.DroneModelName;

public record DroneModelDto(Long id,
                            DroneModelName name,
                            Long maxWeightLimit) {
}
