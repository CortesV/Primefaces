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

	@Column(name = "year_produce")
	private Integer yearProduce;

	private String brand;

	private String model;

	private String color;

	public Car() {

	}

	public Car(Integer yearProduce, String brand, String model, String color) {

		this.yearProduce = yearProduce;
		this.brand = brand;
		this.model = model;
		this.color = color;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

}
