<%
	ui.decorateWith("kenyaui", "panel", [ heading: ui.format(program), frameOnly: true ])
%>
<% if (enrollments) { %>
<div class="ke-panel-content">
	<% enrollments.reverse().each { enrollment -> %>

		<% if (enrollment.dateCompleted) { %>
			<div class="ke-stack-item">
				${ ui.includeFragment("wellness", "program/programCompletion", [ patientProgram: enrollment, showClinicalData: config.showClinicalData ]) }
			</div>
		<% } %>

			<div class="ke-stack-item">
				${ ui.includeFragment("wellness", "program/programEnrollment", [ patientProgram: enrollment, showClinicalData: config.showClinicalData ]) }
			</div>
	<% } %>
</div>
<% } %>

<% if (currentEnrollment || patientIsEligible) { %>
<div class="ke-panel-footer">
	<% if (currentEnrollment) { %>

	<button type="button" onclick="ui.navigate('${ ui.pageLink("wellness", "enterForm", [ patientId: patient.id, formUuid: defaultCompletionForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
		<img src="${ ui.resourceLink("kenyaui", "images/glyphs/discontinue.png") }" /> Discontinue
	</button>

	<% } else if (patientIsEligible) { %>

	<button type="button" onclick="ui.navigate('${ ui.pageLink("wellness", "enterForm", [ patientId: patient.id, formUuid: defaultEnrollmentForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
		<img src="${ ui.resourceLink("kenyaui", "images/glyphs/enroll.png") }" /> Enroll
	</button>

	<% } %>
</div>
<% } %>