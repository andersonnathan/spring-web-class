package rewardsonline.rewards.newreward;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rewards.internal.restaurant.Restaurant;
import rewards.internal.restaurant.RestaurantRepository;

/**
 * Uses {@link RestaurantRepository} to access restaurants for the dining form
 * data.
 */
@Service("diningFormDataProvider")
public class DiningFormDataProviderImpl implements DiningFormDataProvider {

	private RestaurantRepository restaurantRepository;

	@Autowired
	public DiningFormDataProviderImpl(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Transactional(readOnly = true)
	public Map<String, String> findAllRestaurants() {
		final Map<String, String> map = new LinkedHashMap<String, String>();

		List<Restaurant> restaurants = restaurantRepository.getAllRestaurants();

		for (Restaurant restaurant : restaurants) {
			map.put(restaurant.getNumber(), restaurant.getName());
		}

		return map;
	}

}
