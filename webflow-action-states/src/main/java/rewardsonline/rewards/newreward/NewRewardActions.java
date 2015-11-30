package rewardsonline.rewards.newreward;

import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import rewards.RewardNetworkException;

/**
 * A helper that helps carry the back-end actions of the New Reward flow. This
 * Action acts as "glue" between the XML-based flow definition and the business
 * logic layer. It handles passing flow-scoped objects to the service-layer, as
 * well as translating any service-layer exceptions to flow events, recording
 * error messages for display as necessary.
 */
// TODO-01: Deploy this application to the server. Access the home page at
//            http://localhost:8080/webflow-action-states.
//          Remove any previously deployed applications from your server to
//            allow for faster startup.
//          Click "New Reward" and try entering a new reward with a bogus 16
//            digit credit-card number like: 1111222233334444.

// TODO-02: First step of REQUIREMENT #1.
//          Make this a Spring Component, with a name. Remember the name
//           you will need it when you modify the flow XML.
public class NewRewardActions extends MultiAction {

	/**
	 * Creates a new reward actions that invokes the reward network service.
	 */
	// TODO-03: You will need the RewardNetwork to do any work. Have Spring inject it
	//          here.
	public NewRewardActions() {
	}

	/**
	 * Calculate how much the account associated with an ongoing NewReward flow
	 * execution is eligible to be rewarded. This method extracts the DiningForm
	 * context from flowScope, invokes the RewardNetwork, then puts the returned
	 * AccountContribution into flow scope for rendering. This action also
	 * handles the case where the RewardNetwork throws an
	 * InvalidCreditCardException if the card number on the DiningForm is
	 * invalid.
	 * 
	 * @param context
	 *            the current flow execution request context
	 * @return the action result; either success or error; if an error is
	 *         returned an associated error message will have also been added to
	 *         the flow's message context
	 * @throws RewardNetworkException
	 *             an unexpected exception occurs
	 */
	public Event calculateAccountContribution(RequestContext context)
			throws RewardNetworkException {
		// TODO-04: Get the diningForm from the flow-scope using the context.
		//          Invoke RewardNetwork.calculateContributionFor().
		//         
		//          If it succeeds put the confirmation into context.
		//             Which scope will you use so the next state can find it?
		//             Return a success event.
		//
		//          If InvalidCreditCardException is thrown
		//             Specify the field specific error message to display (see
		//               DiningForm.validateEnterDiningInformation() for how).
		//             Return a failure event.
		//
		
		return success();
	}

	/**
	 * Reward the account associated with an ongoing NewReward flow execution
	 * the amount they are eligible to receive. This method extracts the
	 * DiningForm context from flowScope, invokes the RewardNetwork, then puts
	 * the returned RewardConfirmation into flow scope for rendering. This
	 * action also handles the case where the RewardNetwork throws an
	 * InvalidCreditCardException if the card number on the DiningForm is
	 * invalid.
	 * 
	 * @param context
	 *            the current flow execution request context
	 * @return the action result; either success or error; if an error is
	 *         returned an associated error message will have also been added to
	 *         the flow's message context
	 * @throws RewardNetworkException
	 *             an unexpected exception occurs
	 */
	public Event rewardAccountForDining(RequestContext context)
			throws RewardNetworkException {
		// TODO-06: Do the same for RewardNetwork.rewardAccoubtFor as you
		//            did above.
		return success();
	}

}
