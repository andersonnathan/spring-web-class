<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>
	<fmt:message key="enterDiningInformation.title" />
</h1>

<!-- TODO-03: Convert to use Spring form tags.  Don't forget the taglib declaration,
			    copy one of the taglibs above and use Ctrl+Space to pick a new uri.
			  The form model-object is the diningForm from the previous step.
              Use WEB-INF/accounts/search.jsp as a guide if stuck.
              Test the application still works - click "New Reward" and you should
                get a blank form.
  -->
<form id="diningForm" method="post">
	<fieldset> 
		<legend>
			<fmt:message key="enterDiningInformation.legend"/>
		</legend>
		<ul>
			<li>
				<label for="creditCardNumber">
					<fmt:message key="label.DiningForm.creditCard" />
				</label>
				<div class="control">
					<input id="creditCardNumber" name="creditCardNumber" type="text" />
				</div>
			</li>
			<li>
				<label for="merchantNumber">
					<fmt:message key="label.DiningForm.merchantNumber" />
				</label>
				<div class="control">
					<!--  Leave this <select> for now and come back to it at TO-DO 04 -->
					<!--  TODO-04: Now make this a form:select and populate it from
					               the 'restaurants' collection. -->
					<select id="merchantNumber" name="merchantNumber">
						<option value="1">Applebees</option>
						<option value="2">Subway</option>
					</select>
				</div>
			</li>
			<li>
				<label for="amount">
					<fmt:message key="label.DiningForm.amount" />
				</label>
				<div class="control">
					<input id="amount" name="amount" type="text" />
				</div>
			</li>
			<li>
				<label for="date">
					<fmt:message key="label.DiningForm.date" />
				</label>
				<div class="control">
					<input id="date" name="date" type="text" />
				</div>
			</li>
		</ul>
		
		<!--  No Spring form tag for button - leave this as is -->
		<button id="rewardButton" name="_eventId_reward" type="submit">
			<fmt:message key="command.reward" />
		</button>
		
	</fieldset>
</form>