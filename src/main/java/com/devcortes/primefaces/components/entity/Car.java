package com.devcortes.primefaces.components.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "car", catalog = "vechicle")
public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String uuid;

	@Column(name = "year_produce")
	private Integer yearProduce;

	private String brand;

	private String model;

	private String color;

	private Integer price;

	public Car() {

	}

	public Car(String uuid, Integer yearProduce, String brand, String model, String color, Integer price) {

		this.uuid = uuid;
		this.yearProduce = yearProduce;
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getYearProduce() {
		return yearProduce;
	}

	public void setYearProduce(Integer yearProduce) {
		this.yearProduce = yearProduce;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void updateCar(Integer yearProduce, String brand, String model, String color) {

		this.yearProduce = yearProduce;
		this.brand = brand;
		this.model = model;
		this.color = color;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	/*@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Car)) {
			return false;
		}

		Car other = (Car) obj;

		return id == other.getId() && uuid == other.getUuid() && yearProduce == other.getYearProduce()
				&& brand == other.getBrand() && model == other.getModel() && color == other.getColor()
				&& price == other.getPrice();
	}*/

}
