package com.vip.gardenapp.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data(staticConstructor = "newInstance")
@Accessors(chain = true)
@Table(name = "current_location")
public class CurrentLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String xCoordinate;

	private String yCoordinate;

}
