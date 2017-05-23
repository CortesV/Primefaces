package com.devcortes.primefaces.components.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.devcortes.primefaces.components.entity.Car;
import com.devcortes.primefaces.components.interfaces.ICar;
import com.devcortes.primefaces.service.HibernateUtil;

@Repository
public class CarDao implements ICar {

	private static final Logger LOGGER = Logger.getLogger(CarDao.class);

	private final static String SQL_GET_CARS = "select car from Car car";

	private final static String[] colors;

	private final static String[] brands;

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
			List<Car> query = session.createQuery(SQL_GET_CARS).setMaxResults(limit).setFirstResult((batch - 1) * limit)
					.list();
			session.getTransaction().commit();
			return query;

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
			session.save(new Car(UUID.randomUUID().toString(), car.getYearProduce(), car.getBrand(), car.getModel(),
					car.getColor(), car.getPrice()));
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

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void generateCars() {

		try {

			for (int i = 0; i < 10; i++) {

				Car randomCar = new Car(getRandomId(), getRandomYear(), getRandomBrand(), getRandomModel(),
						getRandomColor(), getRandomPrice());
				saveCar(randomCar);
			}

		} catch (Exception e) {

			LOGGER.error(e.getMessage());
		}
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

	@Override
	public List<Car> load(int first, int pageSize, String sortField, boolean asc) {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();

			/*
			 * CriteriaBuilder builder = session.getCriteriaBuilder();
			 * CriteriaQuery<Car> criteria = builder.createQuery(Car.class);
			 * Root<Car> contactRoot = criteria.from(Car.class);
			 * criteria.select(contactRoot); TypedQuery<Car> typedQuery =
			 * session.createQuery(criteria); typedQuery.setFirstResult(first);
			 * typedQuery.setMaxResults(pageSize);
			 * 
			 * if (sortField != null) {
			 * 
			 * if (asc) {
			 * 
			 * criteria.orderBy(builder.asc(contactRoot.get(sortField))); } else
			 * {
			 * 
			 * criteria.orderBy(builder.desc(contactRoot.get(sortField))); } }
			 * return typedQuery.getResultList();
			 */

			Criteria criteria = session.createCriteria(Car.class);
			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);
			if (sortField != null) {

				if (asc) {

					criteria.addOrder(Order.asc(sortField));
				} else {

					criteria.addOrder(Order.desc(sortField));
				}
			}

			session.getTransaction().commit();
			return criteria.list();

		} catch (Exception e) {

			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public Long getTotalRegistors() {

		try (Session session = hibernateUtil.getSessionFactory().openSession()) {

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
			criteria.select(builder.count(criteria.from(Car.class)));

			session.getTransaction().commit();

			return session.createQuery(criteria).getSingleResult();

		} catch (Exception e) {

			LOGGER.error(e.getMessage());
			return null;
		}
	}
}
