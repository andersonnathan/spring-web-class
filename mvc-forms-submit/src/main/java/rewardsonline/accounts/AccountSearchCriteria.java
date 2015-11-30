package rewardsonline.accounts;


/**
 * User-definable criteria for executing account search operations.
 */
public class AccountSearchCriteria {

	// TODO-07: (Optional Bonus 2) Add sensible JSR-303 validation annotations
	//         Remember to use @Valid in the right controller method
	private String searchString = "";

	private int maximumResults;

	private int page;

	/**
	 * The free-form account search string provided by the user.
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * Sets the free-form account search string provided by the user.
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * The maximum number of results to return to the user.
	 */
	public int getMaximumResults() {
		return maximumResults;
	}

	/**
	 * Sets the maximum number of results to return to the user.
	 */
	public void setMaximumResults(int maximumResults) {
		this.maximumResults = maximumResults;
	}

	/**
	 * The page of results being requested; initially 0 meaning the first page
	 * of results.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets the page of results being requested.
	 */
	public void setPage(int page) {
		this.page = page;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof AccountSearchCriteria) {
			AccountSearchCriteria other = (AccountSearchCriteria) obj;

			return searchString.equals(other.searchString)
					&& (maximumResults == other.maximumResults)
					&& (page == other.page);
		}

		return false;
	}

	@Override
	public String toString() {
		return "Search for '" + getSearchString() + "', max results='"
				+ getMaximumResults() + "'";
	}

}