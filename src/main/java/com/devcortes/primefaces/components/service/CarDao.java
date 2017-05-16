package com.devcortes.primefaces.components.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.components.interfaces.ICar;
import com.devcortes.primefaces.service.HibernateUtil;

@Repository
public class CarDao implements ICar {

	private static final Logger LOGGER = Logger.getLogger(CarDao.class);
	private final static String SQL_GET_CARS = "SELECT * FROM car LIMIT ? OFFSET ?";

	@Autowired
	HibernateUtil hibernateUtil;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Car getById(Integer id) {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();
			Car result = session.get(Car.class, id);
			session.getTransaction().commit();
			return result;

		} catch (Exception e) {

			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<Car> getCars(Integer limit, Integer batch) {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();
			List<Car> result = session.createNativeQuery(SQL_GET_CARS).setParameter(1, limit)
					.setParameter(2, (batch - 1) * limit).getResultList();
			session.getTransaction().commit();
			return result;

		} catch (Exception e) {

			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveCar(Car car) {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();
			session.save(new Car(car.getYearProduce(), car.getBrand(), car.getModel(), car.getColor()));
			session.getTransaction().commit();
		} catch (Exception e) {

			LOGGER.error(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateCar(Integer id, Car car) {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();
			Car updateCar = session.get(Car.class, id);
			updateCar.updateCar(car.getYearProduce(), car.getBrand(), car.getModel(), car.getColor());
			session.update(updateCar);
			session.getTransaction().commit();
		} catch (Exception e) {

			LOGGER.error(e.getMessage());
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteCar(Integer id) {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();
			Car delCar = session.get(Car.class, id);
			session.delete(delCar);
			session.getTransaction().commit();

		} catch (Exception e) {

			LOGGER.error(e.getMessage());
		}

	}

}
