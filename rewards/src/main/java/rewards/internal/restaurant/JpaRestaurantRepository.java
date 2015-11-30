package rewards.internal.restaurant;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Loads restaurants from a data source using pure JPA.
 */
@Profile("jpa")
@Repository("restaurantRepository")
public class JpaRestaurantRepository implements RestaurantRepository {

	EntityManager entityManager;

	public JpaRestaurantRepository() {
	}

	/**
	 * Set the entity manager - like @Autowired specifically for JPA. Doesn't
	 * support constructor injection, so we have to use a setter.
	 * 
	 * @param entityManager
	 *            When using Spring this is actually an entity manager proxy.
	 *            Each time it is used, it resolves to the current entity
	 *            manager for the current transaction and thread.
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Restaurant findByMerchantNumber(String merchantNumber) {
		return entityManager
				.createQuery("SELECT r FROM Restaurant r WHERE r.number = :number",
						Restaurant.class)
				.setParameter("number", merchantNumber).getSingleResult();
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		return entityManager.createQuery("SELECT r FROM Restaurant r ORDER BY name",
				Restaurant.class).getResultList();
	}

}
