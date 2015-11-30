package rewardsonline.rewards.newreward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import rewards.AccountContribution;
import rewards.InvalidCreditCardException;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.RewardNetworkException;

/**
 * A helper that helps carry the back-end actions of the New Reward flow. This
 * Action acts as "glue" between the XML-based flow definition and the business
 * logic layer. It handles passing flow-scoped objects to the service-layer, as
 * well as translating any service-layer exceptions to flow events, recording
 * error messages for display as necessary.
 */
@Component("newRewardActions")
public class NewRewardActions extends MultiAction {

	private RewardNetwork rewardNetwork;

	/**
	 * Creates a new reward actions that invokes the reward network service.
	 * 
	 * @param rewardNetwork
	 *            the reward network
	 */
	@Autowired
	public NewRewardActions(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
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
		try {
			DiningForm diningForm = (DiningForm) context.getFlowScope().get(
					"diningForm");
			AccountContribution contribution = rewardNetwork
					.calculateContributionFor(diningForm.createDining());
			context.getFlowScope().put("accountContribution", contribution);
			return success();
		} catch (InvalidCreditCardException e) {
			context
					.getMessageContext()
					.addMessage(
							new MessageBuilder()
									.error()
									.source("creditCardNumber")
									.defaultText(
											"The credit card number is not associated with any account")
									.build());
			return error();
		}
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
		try {
			DiningForm diningForm = (DiningForm) context.getFlowScope().get(
					"diningForm");
			RewardConfirmation confirmation = rewardNetwork
					.rewardAccountFor(diningForm.createDining());
			context.getFlowScope().put("rewardConfirmation", confirmation);
			return success();
		} catch (InvalidCreditCardException e) {
			context
					.getMessageContext()
					.addMessage(
							new MessageBuilder()
									.error()
									.source("creditCardNumber")
									.defaultText(
											"The credit card number is not associated with any account")
									.build());
			return error();
		}
	}

}
