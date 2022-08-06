package com.drone.enums;

import lombok.Getter;

@Getter
public enum DroneState {

    IDLE(1),
    LOADING(2),
    LOADED(3),
    DELIVERING(4),
    DELIVERED(5),
    RETURNING(6);

    private final Integer id;

    DroneState(Integer id) {
        this.id = id;
    }

}
