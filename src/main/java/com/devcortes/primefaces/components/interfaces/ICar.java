package com.devcortes.primefaces.components.interfaces;

import java.util.List;

import org.primefaces.model.SortOrder;

import com.devcortes.primefaces.components.entity.Car;

public interface ICar {

	public Car getById(Integer id);

	public List<Car> getCars(Integer limit, Integer batch);

	public Integer saveCar(Car car);

	public void updateCar(Integer id, Car car);

	public void deleteCar(Integer id);
	
	public void generateCars();
	
    public List<Car> load(int first, int pageSize, String sortField, boolean asc);
    
    public Long getTotalRegistors();

}
