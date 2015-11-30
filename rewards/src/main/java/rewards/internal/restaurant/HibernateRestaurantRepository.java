package rewards.internal.restaurant;

import java.beans.ConstructorProperties;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Loads restaurants from a data source using Hibernate's Session API.
 */
@Profile("hibernate")
@Repository("restaurantRepository")
public class HibernateRestaurantRepository implements RestaurantRepository {

	protected SessionFactory sessionFactory;

	/**
	 * Creates a new Hibernate restaurant repository.
	 * 
	 * @param sessionFactory
	 *            the Hibernate session factory
	 */
	@Autowired
	@ConstructorProperties(value="sessionFactory")
	public HibernateRestaurantRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Restaurant findByMerchantNumber(String merchantNumber) {
		return (Restaurant) getCurrentSession()
				.createQuery(
						"SELECT r FROM Restaurant r WHERE r.number = :number")
				.setParameter("number", merchantNumber).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Restaurant> getAllRestaurants() {
		return (List<Restaurant>) getCurrentSession().createQuery(
				"SELECT r FROM Restaurant r ORDER BY name").list();
	}

	/**
	 * Returns the session associated with the ongoing transaction.
	 * 
	 * @return the current session
	 */
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
