package com.devcortes.primefaces.components.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.components.interfaces.ICar;
//import com.devcortes.primefaces.service.HibernateUtil;
import com.mysql.cj.api.jdbc.Statement;

@Repository
public class CarDao implements ICar {

	private final static String SQL_GET_CARS = "SELECT * FROM vechicle.car LIMIT ? OFFSET ?";
	private final static String SQL_GET_NUMBER_CARS = "SELECT COUNT(id) FROM car";
	private final static String SQL_GET_AND_ORDER_CARS_FIRST_PART = "SELECT * FROM car ORDER BY car.";
	private final static String SQL_GET_AND_ORDER_CARS_SECOND_PART = " LIMIT ? OFFSET ?";
	private final static String SQL_FOR_GETTING_CAR_BY_ID = "SELECT * FROM car  WHERE car.id = ? AND car.delete = 0";
	private final static String SQL_SAVE_CAR = "INSERT INTO car (uuid, year_produce, brand, model, color, price) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_CAR = "UPDATE car SET year_produce = ?, brand = ?, model = ?, color = ?, price = ? WHERE id = ?";
	private static final String DELETE_CAR = "UPDATE car as c SET c.`delete` = '1' WHERE c.id = ?";
	private static final String DESC = "DESC";
	private static final String ASC = "ASC";
	private static final String YEAR_PRODUCED = "year_produce";

	private final static String[] colors;

	private final static String[] brands;

	@Autowired
	private JdbcTemplate jdbc;

	static {
		colors = new String[10];
		colors[0] = "Black";
		colors[1] = "White";
		colors[2] = "Green";
		colors[3] = "Red";
		colors[4] = "Blue";
		colors[5] = "Orange";
		colors[6] = "Silver";
		colors[7] = "Yellow";
		colors[8] = "Brown";
		colors[9] = "Maroon";

		brands = new String[10];
		brands[0] = "BMW";
		brands[1] = "Mercedes";
		brands[2] = "Volvo";
		brands[3] = "Audi";
		brands[4] = "Renault";
		brands[5] = "Fiat";
		brands[6] = "Volkswagen";
		brands[7] = "Honda";
		brands[8] = "Jaguar";
		brands[9] = "Ford";
	}

	@Override
	public Car getById(Integer idCar) {
		return Optional.ofNullable(jdbc.query(SQL_FOR_GETTING_CAR_BY_ID, new BeanPropertyRowMapper<>(Car.class), idCar))
				.get().stream().findFirst().orElse(null);
	}

	@Override
	public List<Car> getCars(Integer limit, Integer batch) {
		List<Car> findCars = jdbc.query(SQL_GET_CARS, new BeanPropertyRowMapper<Car>(Car.class), limit,
				(batch - 1) * limit);
		return findCars.isEmpty() ? new ArrayList<>() : findCars;
	}

	@Override
	public Integer saveCar(Car car) {
		KeyHolder holder = new GeneratedKeyHolder();
		jdbc.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_CAR,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, UUID.randomUUID().toString());
				preparedStatement.setInt(2, car.getYearProduce());
				preparedStatement.setString(3, car.getBrand());
				preparedStatement.setString(4, car.getModel());
				preparedStatement.setString(5, car.getColor());
				preparedStatement.setInt(6, car.getPrice());
				return preparedStatement;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	@Override
	public void updateCar(Integer id, Car car) {
		jdbc.update(UPDATE_CAR, car.getYearProduce(), car.getBrand(), car.getModel(), car.getColor(), car.getPrice(),
				id);
	}

	@Override
	public void deleteCar(Integer id) {
		jdbc.update(DELETE_CAR, id);
	}

	@Override
	public void generateCars() {
		for (int i = 0; i < 200; i++) {
			Car randomCar = new Car(getRandomId(), getRandomYear(), getRandomBrand(), getRandomModel(),
					getRandomColor(), getRandomPrice());
			saveCar(randomCar);
		}
	}

	@Override
	public List<Car> load(int first, int pageSize, String sortField, boolean asc) {

		if (sortField != null) {
			sortField = sortField.equals("yearProduce") ? YEAR_PRODUCED : sortField;
			String sortTemplate = asc ? ASC : DESC;
			return jdbc.query(
					SQL_GET_AND_ORDER_CARS_FIRST_PART + sortField + " " + sortTemplate
							+ SQL_GET_AND_ORDER_CARS_SECOND_PART,
					new BeanPropertyRowMapper<Car>(Car.class), first, (pageSize - 1) * first);
		} else {
			return jdbc.query(SQL_GET_CARS, new BeanPropertyRowMapper<Car>(Car.class), first, (pageSize - 1) * first);
		}
	}

	@Override
	public Long getTotalRegistors() {
		return jdbc.queryForObject(SQL_GET_NUMBER_CARS, Long.class);
	}

	private String getRandomId() {
		return UUID.randomUUID().toString();
	}

	private String getRandomModel() {
		return UUID.randomUUID().toString().substring(0, 3);
	}

	private int getRandomYear() {
		return (int) (Math.random() * 50 + 1960);
	}

	private String getRandomColor() {
		return colors[(int) (Math.random() * 10)];
	}

	private String getRandomBrand() {
		return brands[(int) (Math.random() * 10)];
	}

	private int getRandomPrice() {
		return (int) (Math.random() * 100000);
	}
}
