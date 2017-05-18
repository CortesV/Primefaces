package com.devcortes.primefaces.jsf.data;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.service.CarService;

@RestController
@ManagedBean
@Scope("view")
public class SaveDataController {

	@ManagedProperty("#{carService}")
	@Autowired
	private CarService carService;

	Car car;

	@PostConstruct
	private void init() {

		car = new Car();
	}

	public void save(ActionEvent actionEvent) {

		carService.saveCar(car);
		addMessage("Data saved");
	}
	
	public void saveGen(ActionEvent actionEvent) {

		carService.generateCars();
		addMessage("Data saved");
	}

	public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public CarService getCarService() {
		return carService;
	}

	public void setCarService(CarService carService) {
		this.carService = carService;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

}
