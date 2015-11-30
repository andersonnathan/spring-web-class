package rewardsonline.rewards.newreward;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import rewards.AccountContribution;
import rewards.AccountContribution.Distribution;
import rewardsonline.accounts.test.AccountTestConstants;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.RewardNetworkException;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

/**
 * Tests the execution of the New Reward flow. Verifies that the flow starts
 * successfully and processes user events correctly.
 * 
 * The goal of this test is to exercise all paths through the flow. This is a
 * flow unit test with stub implementations of the RewardNetwork and
 * DiningFormDataProvider services configured.
 */
public class NewRewardFlowTests extends AbstractXmlFlowExecutionTests implements
		AccountTestConstants {

	private static final String ENTER_DINING_INFORMATION = "enterDiningInformation";

	private static final String REVIEW_REWARD = "reviewReward";

	private static final String REWARD_CONFIRMED = "rewardConfirmed";

	private DiningForm diningForm;

	@Override
	protected void setUp() throws Exception {
		diningForm = new DiningForm();
		diningForm.setCreditCardNumber(CC_1234123412341234);
		diningForm
				.setMerchantNumber(StubDiningFormDataProvider.RESTAURANT_APPLE_BEES);
		diningForm.setAmount(MonetaryAmount.valueOf("100"));
		diningForm.setDate(SimpleDate.today());
	}

	@Override
	protected FlowDefinitionResource getResource(
			FlowDefinitionResourceFactory resourceFactory) {
		return resourceFactory
				.createFileResource("src/main/webapp/WEB-INF/rewards/newReward/newReward-flow.xml");
	}

	@Override
	protected void configureFlowBuilderContext(
			MockFlowBuilderContext builderContext) {
		builderContext.registerBean("diningFormDataProvider",
				new StubDiningFormDataProvider());
		builderContext.registerBean("newRewardActions", new NewRewardActions(
				new StubRewardNetwork()));
	}

	/**
	 * Test flow startup.
	 */
	public void testStart() throws Exception {
		startFlow(new MockExternalContext());
		assertCurrentStateEquals(ENTER_DINING_INFORMATION);
		assertNotNull(getRequiredFlowAttribute("diningForm"));
	}

	/**
	 * From the enterDiningInformation state, test handling of the 'reward'
	 * event.
	 */
	public void testEnterDiningInformation_Reward() throws Exception {
		setCurrentState(ENTER_DINING_INFORMATION);
		getFlowScope().put("diningForm", diningForm);
		MockExternalContext externalContext = new MockExternalContext();
		externalContext.setEventId("reward");
		resumeFlow(externalContext);
		assertCurrentStateEquals(REVIEW_REWARD);
		AccountContribution contribution = (AccountContribution) getRequiredFlowAttribute("accountContribution");
		assertEquals(MonetaryAmount.valueOf("25.00"), contribution.getAmount());
	}

	/**
	 * From the enterDiningInformation state, test handling of the 'reward'
	 * event when the 'oneClickReward' DiningForm property is true.
	 */
	public void testEnterDiningInformation_RewardOneClick() throws Exception {
		setCurrentState(ENTER_DINING_INFORMATION);
		diningForm.setOneClickReward(true);
		getFlowScope().put("diningForm", diningForm);
		MockExternalContext externalContext = new MockExternalContext();
		externalContext.setEventId("reward");
		resumeFlow(externalContext);
		assertFlowExecutionEnded();
		assertFlowExecutionOutcomeEquals(REWARD_CONFIRMED);
		assertTrue(externalContext.getExternalRedirectRequested());
		assertEquals("contextRelative:/rewards/12345",
				externalContext.getExternalRedirectUrl());
	}

	/**
	 * From the review reward state, test handling of the 'recalculateReward'
	 * event.
	 */
	public void testReviewReward_RecalculateReward() throws Exception {
		setCurrentState(REVIEW_REWARD);
		diningForm.setAmount(MonetaryAmount.valueOf("200.00"));
		getFlowScope().put("diningForm", diningForm);
		MockExternalContext externalContext = new MockExternalContext();
		externalContext.setEventId("recalculateReward");
		resumeFlow(externalContext);
		assertCurrentStateEquals(REVIEW_REWARD);
		AccountContribution contribution = (AccountContribution) getRequiredFlowAttribute("accountContribution");
		assertEquals(MonetaryAmount.valueOf("25.00"), contribution.getAmount());
	}

	/**
	 * From the review reward state, test handling of the 'confirm' event.
	 */
	public void testReviewReward_Confirm() throws Exception {
		setCurrentState(REVIEW_REWARD);
		getFlowScope().put("diningForm", diningForm);
		MockExternalContext externalContext = new MockExternalContext();
		externalContext.setEventId("confirm");
		resumeFlow(externalContext);
		assertFlowExecutionEnded();
		assertFlowExecutionOutcomeEquals(REWARD_CONFIRMED);
		assertTrue(externalContext.getExternalRedirectRequested());
		assertEquals("contextRelative:/rewards/12345",
				externalContext.getExternalRedirectUrl());
	}

	/**
	 * Stub RewardNetwork used by this unit test.
	 */
	public static class StubRewardNetwork implements RewardNetwork {

		public AccountContribution calculateContributionFor(Dining dining)
				throws RewardNetworkException {
			Set<Distribution> distributions = new LinkedHashSet<Distribution>();
			AccountContribution contribution = new AccountContribution(
					ACC_123456789, new MonetaryAmount(25.00), distributions);
			return contribution;
		}

		public RewardConfirmation rewardAccountFor(Dining dining)
				throws RewardNetworkException {
			return new RewardConfirmation("12345",
					calculateContributionFor(dining));
		}

	}

	/**
	 * Stub DiningFormDataProvider used by this unit test.
	 */
	public static class StubDiningFormDataProvider implements
			DiningFormDataProvider, AccountTestConstants {

		private Map<String, String> restaurants;

		public StubDiningFormDataProvider() {
			final Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(RESTAURANT_APPLE_BEES, "Applebees");
			map.put(RESTAURANT_SUBWAY, "Subway");
		}

		public Map<String, String> findAllRestaurants() {
			return restaurants;
		}

	}
}