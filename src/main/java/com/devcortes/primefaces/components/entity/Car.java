package com.devcortes.primefaces.components.entity;

import java.io.Serializable;

public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String uuid;

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

	public void updateCar(Integer yearProduce, String brand, String model, String color) {

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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((yearProduce == null) ? 0 : yearProduce.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (yearProduce == null) {
			if (other.yearProduce != null)
				return false;
		} else if (!yearProduce.equals(other.yearProduce))
			return false;
		return true;
	}
}
