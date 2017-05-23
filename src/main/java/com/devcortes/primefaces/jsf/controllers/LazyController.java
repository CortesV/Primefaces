package com.devcortes.primefaces.jsf.controllers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.service.CarService;

@RestController
@ViewScoped
public class LazyController implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{carService}")
	@Autowired
	private CarService carService;

	private LazyDataModel<Car> lazyModel;

	private List<Car> datasource;

	private Car selectedCar;

	@PostConstruct
	public void init() {

		this.lazyModel = new LazyDataModel<Car>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Car> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				setRowCount(carService.getTotalRegistors().intValue());
				datasource = carService.load(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder));

				/*List<Car> data = new ArrayList<>();
				for (Car car : datasource) {
					boolean match = true;

					if (filters != null) {
						for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
							try {
								String filterProperty = it.next();
								Object filterValue = filters.get(filterProperty);
								Field field = car.getClass().getDeclaredField(filterProperty);
								field.setAccessible(true);
								String fieldValue = String.valueOf(field.get(car));

								if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
									match = true;
								} else {
									match = false;
									break;
								}
							} catch (Exception e) {
								match = false;
							}
						}
					}

					if (match) {
						data.add(car);
					}
				}

				int dataSize = data.size();
				//this.setRowCount(dataSize);

				// paginate
				if (dataSize > pageSize) {
					try {
						return data.subList(first, first + pageSize);
					} catch (IndexOutOfBoundsException e) {
						return data.subList(first, first + (dataSize % pageSize));
					}
				} else {
					return data;
				}*/
				
				return datasource;

			}

			@Override
			public Car getRowData(String rowKey) {

				return datasource.stream().filter(car -> Optional.of(car).isPresent() && car.getUuid().equals(rowKey))
						.findFirst().orElse(null);
			}

			@Override
			public Object getRowKey(Car car) {

				return Optional.of(car).map(Car::getUuid).orElse(null);
			}

		};
	}

	public LazyDataModel<Car> getLazyModel() {
		return lazyModel;
	}

	public Car getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(Car selectedCar) {
		this.selectedCar = selectedCar;
	}

	public void setService(CarService service) {
		this.carService = service;
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Car Selected", ((Car) event.getObject()).getUuid().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
