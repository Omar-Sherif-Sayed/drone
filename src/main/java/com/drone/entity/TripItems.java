package com.drone.entity;

import com.drone.constant.*;
import com.drone.dto.TripItemsDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = TableName.TRIP_ITEMS)
public class TripItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TripItemsColumn.ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = TripItemsColumn.TRIP_ID, nullable = false,
            foreignKey = @ForeignKey(name = ForeignKeyName.TRIP_ITEMS_TRIP_ID_FK,
                    foreignKeyDefinition = "FOREIGN KEY (" + TripItemsColumn.TRIP_ID + ") REFERENCES " + TableName.TRIP + " (" + TripColumn.ID + ")"))
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = TripItemsColumn.ITEM_ID, nullable = false,
            foreignKey = @ForeignKey(name = ForeignKeyName.TRIP_ITEMS_ITEM_ID_FK,
                    foreignKeyDefinition = "FOREIGN KEY (" + TripItemsColumn.ITEM_ID + ") REFERENCES " + TableName.ITEM + " (" + ItemColumn.ID + ")"))
    private Item item;

    @Column(name = TripItemsColumn.ITEM_COUNT, nullable = false)
    private Integer itemCount;

    public TripItemsDto toTripItemsDto() {
        return new TripItemsDto(id, trip.getId(), item.toItemDto(), itemCount);
    }

}
