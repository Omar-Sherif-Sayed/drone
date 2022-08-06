package com.drone.entity;

import com.drone.constant.DroneModelColumn;
import com.drone.constant.TableName;
import com.drone.dto.DroneModelDto;
import com.drone.enums.DroneModelName;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = TableName.DRONE_MODEL)
public class DroneModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DroneModelColumn.ID)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = DroneModelColumn.NAME)
    private DroneModelName name;

    @Column(name = DroneModelColumn.MAX_WEIGHT_LIMIT)
    private Long maxWeightLimit;

    public DroneModelDto toDroneModelDto() {
        return new DroneModelDto(id, name, maxWeightLimit);
    }

}
