package rewardsonline.accounts;

public class SearchCriteriaException extends RuntimeException {

	/**
	 * Serialization requires a version id.
	 */
	private static final long serialVersionUID = 6292213784571039029L;

	public SearchCriteriaException(String message) {
		super(message);
	}

}
