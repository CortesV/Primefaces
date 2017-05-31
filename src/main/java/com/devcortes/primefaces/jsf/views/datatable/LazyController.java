package com.devcortes.primefaces.jsf.views.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
import com.devcortes.primefaces.service.FilterDataService;

@RestController
@ViewScoped
public class LazyController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LazyController.class);
	private static final String MARK_ADDED = "Mark is added. Id = ";
	private static final String MARK_REMOVED = "Mark is removed. Id = ";

	@Autowired
	private transient CarService carService;

	@Autowired
	private transient FilterDataService filterDataService;

	private LazyDataModel<Car> lazyModel;
	private List<Car> datasource;
	private List<Car> selectedCars;
	private Set<Integer> markedCars;
	private boolean selectAllFlag;

	public LazyController() {
		markedCars = new HashSet<>();
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
				int page = carService.getTotalRegistors().intValue() / pageSize
						- (carService.getTotalRegistors().intValue() - (first + pageSize)) / pageSize;
				datasource = carService.load(pageSize, page, sortField, SortOrder.ASCENDING.equals(sortOrder));
				datasource = filterDataService.filter(first, pageSize, filters, datasource);

				if (selectAllFlag) {
					for (Car datasourceCar : datasource) {
						if (!markedCars.contains(datasourceCar.getId())) {
							selectedCars.add(datasourceCar);
						}
					}
				} else {
					for (Car datasourceCar : datasource) {
						if (markedCars.contains(datasourceCar.getId())) {
							selectedCars.add(datasourceCar);
						}
					}
				}

				return datasource;
			}

			@Override
			public Car getRowData(String rowKey) {
				return datasource.stream()
						.filter(car -> Optional.of(car).isPresent() && car.getId().toString().equals(rowKey))
						.findFirst().orElse(null);
			}

			@Override
			public Object getRowKey(Car car) {
				return Optional.of(car).map(Car::getId).orElse(null);
			}
		};
	}

	public void selectAll() {
		markedCars = new HashSet<>();
		selectAllFlag = true;
		datasource.stream().forEach(datasourceCar -> {
			selectedCars.add(datasourceCar);
		});
	}

	public void deSelectAll() {
		selectedCars = new ArrayList<>();
		markedCars = new HashSet<>();
		selectAllFlag = false;
	}

	public void selectCheckbox(SelectEvent event) {
		Integer idNewCar = ((Car) event.getObject()).getId();
		markedCars.add(idNewCar);
		LOGGER.info(MARK_ADDED + idNewCar);
	}

	public void unSelectCheckbox(UnselectEvent event) {
		Integer idCar = ((Car) event.getObject()).getId();
		if (selectAllFlag) {
			markedCars.add(idCar);
			LOGGER.info(MARK_ADDED + idCar);
		} else {
			markedCars.remove(idCar);
			LOGGER.info(MARK_REMOVED + idCar);
		}

	}

	public void selectRow(SelectEvent event) {
		selectCheckbox(event);
	}

	public void unSelectRow(UnselectEvent event) {
		unSelectCheckbox(event);
	}

	public void toggleSelect() {
		datasource.stream().forEach(dataCar -> {
			if (selectAllFlag) {
				markedCars.add(dataCar.getId());
				LOGGER.info(MARK_REMOVED + dataCar.getId());
			} else {
				markedCars.add(dataCar.getId());
				LOGGER.info(MARK_ADDED + dataCar.getId());
			}
		});
	}

	public void onRowSelect(SelectEvent event) {

		FacesMessage msg = new FacesMessage("Car Selected", ((Car) event.getObject()).getId().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Car Unselected", ((Car) event.getObject()).getId().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
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

}
