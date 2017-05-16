package com.devcortes.primefaces.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.components.interfaces.ICar;

@Service
public class CarService {

	@Autowired
	ICar iCar;

	public Car getById(Integer id) {

		return iCar.getById(id);
	}

	public List<Car> getCars(Integer limit, Integer batch) {

		return iCar.getCars(limit, batch);
	}

	public void saveCar(Car car) {

		iCar.saveCar(car);
	}

	public void updateCar(Integer id, Car car) {

		iCar.updateCar(id, car);
	}

	public void deleteCar(Integer id) {

		iCar.deleteCar(id);
	}
}
