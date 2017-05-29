package com.devcortes.primefaces.jsf.views.datatable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.service.CarService;

public class LazyCarDataModel extends LazyDataModel<Car> {

	private static final long serialVersionUID = 1L;
	@Autowired
	private transient CarService carService;

	private List<Car> datasource;

	public LazyCarDataModel() {

	}

	@Override
	public List<Car> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		setRowCount(carService.getTotalRegistors().intValue());
		datasource = carService.load(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder));
		datasource = filter(first, pageSize, filters);
		// viewData();
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

	public List<Car> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Car> datasource) {
		this.datasource = datasource;
	}

}
