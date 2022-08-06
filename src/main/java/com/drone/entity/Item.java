package com.drone.entity;

import com.drone.constant.ItemColumn;
import com.drone.constant.TableName;
import com.drone.dto.ItemDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = TableName.ITEM)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ItemColumn.ID)
    private Long id;

    @Column(name = ItemColumn.NAME)
    private String name;

    @Column(name = ItemColumn.CODE)
    private String code;

    @Column(name = ItemColumn.WEIGHT)
    private Long weight;

    @Column(name = ItemColumn.IMAGE_URL)
    private String imageUrl;

    public ItemDto toItemDto() {
        return new ItemDto(id, name, code, weight, imageUrl);
    }

}
