package com.drone.entity;

import com.drone.constant.*;
import com.drone.dto.DroneDto;
import com.drone.enums.DroneState;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = TableName.DRONE)
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DroneColumn.ID)
    private Long id;

    @Column(name = DroneColumn.SERIAL, length = 100)
    private String serial;

    @Enumerated(EnumType.STRING)
    @Column(name = DroneColumn.STATE)
    private DroneState state;

    @Column(name = DroneColumn.BATTERY_CAPACITY)
    private Long batteryCapacity;

    @ManyToOne
    @JoinColumn(name = DroneColumn.DRONE_MODEL_ID, nullable = false,
            foreignKey = @ForeignKey(name = ForeignKeyName.DRONE_MODEL_ID_FK,
                    foreignKeyDefinition = "FOREIGN KEY (" + DroneColumn.DRONE_MODEL_ID + ") REFERENCES " + TableName.DRONE_MODEL + " (" + DroneModelColumn.ID + ")"))
    private DroneModel model;

    public DroneDto toDroneDto() {
        return new DroneDto(id, serial, state, batteryCapacity, model.toDroneModelDto());
    }

}
