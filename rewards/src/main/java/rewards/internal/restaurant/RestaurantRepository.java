package rewards.internal.restaurant;

import java.util.List;

/**
 * Loads restaurant aggregates. Called by the reward network to find and
 * reconstitute Restaurant entities from an external form such as a set of RDMS
 * rows.
 * 
 * Objects returned by this repository are guaranteed to be fully-initialized
 * and ready to use.
 */
public interface RestaurantRepository {

	/**
	 * Load a Restaurant entity by its merchant number.
	 * 
	 * @param merchantNumber
	 *            the merchant number
	 * @return the restaurant
	 */
	public Restaurant findByMerchantNumber(String merchantNumber);

	/**
	 * Return all the restaurants in the system.
	 * <p>
	 * <b>Warning:</b> This is fine for a small application like this one, but
	 * could potentially return thousands of results in a real application.
	 * Always keep queries bounded.
	 * 
	 * @return
	 */
	public List<Restaurant> getAllRestaurants();

}
