package rewardsonline.rewards;

import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

public class NewRewardFlowTests extends AbstractXmlFlowExecutionTests {

	private static final String ENTER_DINING_INFORMATION = "enterDiningInformation";

	private static final String REVIEW_REWARD = "reviewReward";

	private static final String REWARD_CONFIRMED = "rewardConfirmed";

	@Override
	protected FlowDefinitionResource getResource(
			FlowDefinitionResourceFactory resourceFactory) {
		return resourceFactory
				.createFileResource("src/main/webapp/WEB-INF/rewards/newReward/newReward-flow.xml");
	}

	public void testStart() throws Exception {
		startFlow(new MockExternalContext());
		assertCurrentStateEquals(ENTER_DINING_INFORMATION);
	}

	public void testEnterDiningInformation_Reward() throws Exception {
		setCurrentState(ENTER_DINING_INFORMATION);
		MockExternalContext externalContext = new MockExternalContext();
		externalContext.setEventId("reward");
		resumeFlow(externalContext);
		assertCurrentStateEquals(REVIEW_REWARD);
	}

	public void testReviewReward_Confirm() throws Exception {
		setCurrentState(REVIEW_REWARD);
		MockExternalContext externalContext = new MockExternalContext();
		externalContext.setEventId("confirm");
		resumeFlow(externalContext);
		assertFlowExecutionEnded();
		assertFlowExecutionOutcomeEquals(REWARD_CONFIRMED);
		assertTrue(externalContext.getExternalRedirectRequested());
		assertEquals("contextRelative:/rewards/1",
				externalContext.getExternalRedirectUrl());
	}

}