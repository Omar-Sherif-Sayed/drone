package com.drone.entity;

import com.drone.constant.*;
import com.drone.dto.TripDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = TableName.TRIP)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TripColumn.ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = TripColumn.DRONE_ID, nullable = false,
            foreignKey = @ForeignKey(name = ForeignKeyName.TRIP_DRONE_ID_FK,
                    foreignKeyDefinition = "FOREIGN KEY (" + TripColumn.DRONE_ID + ") REFERENCES " + TableName.DRONE + " (" + DroneColumn.ID + ")"))
    private Drone drone;

    @OneToMany(targetEntity = TripItems.class, mappedBy = "trip")
    private List<TripItems> tripItems = Collections.emptyList();

    @Column(name = TripColumn.START_TIME)
    private Date startTime;

    @Column(name = TripColumn.END_TIME)
    private Date endTime;

    public TripDto toTripDto() {
//        List<TripItemsDto> tripItemsDto;
//        if(tripItems != null && !tripItems.isEmpty())
//            tripItemsDto = tripItems.stream().map(TripItems::toTripItemsDto).toList();
//        else
//            tripItemsDto = null;
        return new TripDto(id, drone.toDroneDto(), tripItems.stream().map(TripItems::toTripItemsDto).toList(), startTime, endTime);
    }

}
