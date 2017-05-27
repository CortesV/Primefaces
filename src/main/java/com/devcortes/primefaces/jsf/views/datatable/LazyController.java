package com.devcortes.primefaces.jsf.views.datatable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
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
	private static final Logger LOGGER = Logger.getLogger(LazyController.class);

	@ManagedProperty("#{carService}")
	@Autowired
	private transient CarService carService;

	private LazyDataModel<Car> lazyModel;
	private List<Car> datasource;
	private List<Car> selectedCars;
	private Set<Car> selectedCarsHelp;
	private Set<Car> markedCars;
	private Set<Car> notMarkedCars;
	private boolean selectAllFlag;

	public LazyController() {
		markedCars = new HashSet<>();
		notMarkedCars = new HashSet<>();
		selectedCarsHelp = new HashSet<>();
		selectedCars = new ArrayList<>();
	}

	@PostConstruct
	public void init() {

		this.lazyModel = new LazyDataModel<Car>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Car> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				setRowCount(carService.getTotalRegistors().intValue());				
				int page = carService.getTotalRegistors().intValue()/pageSize - (carService.getTotalRegistors().intValue() - (first + pageSize))/pageSize;
				datasource = carService.load(pageSize, page, sortField, SortOrder.ASCENDING.equals(sortOrder));
				datasource = filter(first, pageSize, filters);

				Set<Car> localCars = new HashSet<>(datasource);
				Set<Car> localSelectedCars = new HashSet<>();
				if (selectAllFlag) {
					selectedCars = datasource;
					selectedCarsHelp = new HashSet<>(datasource);
					
					
					for (Car markCar : localCars) {
						if (!notMarkedCars.contains(markCar)) {
							localSelectedCars.add(markCar);
						}
					}
					selectedCars = new ArrayList<>();
					for (Car markCar : localSelectedCars) {
						selectedCars.add(markCar);
					}

				} else {
					for (Car markCar : markedCars) {
						if (localCars.contains(markCar)) {
							selectedCars.add(markCar);
						}
					}
				}
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

	public void selectAll() {
		notMarkedCars = new HashSet<>();
		selectedCars = datasource;
		markedCars = new HashSet<>(datasource);
		selectAllFlag = true;
	}

	public void deSelectAll() {
		selectedCars = new ArrayList<>();
		selectAllFlag = false;
		markedCars = new HashSet<>();
	}

	public void select() {

		markedCars.addAll(selectedCars);
		for (Car dataCar : datasource) {

			if (!selectedCars.contains(dataCar)) {
				notMarkedCars.add(dataCar);
			}

			if (selectedCars.contains(dataCar) && notMarkedCars.contains(dataCar)) {
				notMarkedCars.remove(dataCar);
			}
		}

	}

	public void unSelect() {

		for (Car dataCar : datasource) {
			if (!selectedCars.contains(dataCar)) {
				notMarkedCars.add(dataCar);
				if (markedCars.contains(dataCar)) {
					markedCars.remove(dataCar);
				}
			}
		}
		LOGGER.info("Mark is removed");
	}

	public LazyDataModel<Car> getLazyModel() {

		return lazyModel;
	}

	public List<Car> getSelectedCars() {

		return selectedCars;
	}

	public void setSelectedCars(List<Car> selectedCars) {

		this.selectedCars = selectedCars;
	}

	public void setService(CarService service) {

		this.carService = service;
	}

	public void onRowSelect(SelectEvent event) {

		FacesMessage msg = new FacesMessage("Car Selected", ((Car) event.getObject()).getUuid());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Car Unselected", ((Car) event.getObject()).getUuid());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<Car> filter(int first, int pageSize, Map<String, Object> filters) {

		List<Car> data = new ArrayList<>();
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
		// paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}

}
