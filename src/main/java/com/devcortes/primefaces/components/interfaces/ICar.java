package com.devcortes.primefaces.components.interfaces;

import java.util.List;

import com.devcortes.primefaces.components.entity.Car;

public interface ICar {

	public Car getById(Integer id);

	public List<Car> getCars(Integer limit, Integer batch);

	public void saveCar(Car car);

	public void updateCar(Integer id, Car car);

	public void deleteCar(Integer id);
	
	public void generateCars();

}
