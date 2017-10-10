<%
	ui.includeJavascript("wellness", "controllers/patient.js")

	def heading = "Scheduled for "
	if (isToday)
		heading += "Today"
	else if (isTomorrow)
		heading += "Tomorrow"
	else if (isYesterday)
		heading = "missed appointments Yesterday"
	else if(beyondTomorrow)
		heading += kenyaui.formatDate(date)
	else
		heading ="missed appointments on "+ kenyaui.formatDate(date)
%>

<div class="ke-panel-frame" ng-controller="DailySchedule" ng-init="init('${ currentApp.id }', '${ kenyaui.formatDateParam(date) }', '${ config.pageProvider }', '${ config.page }')">
	<div class="ke-panel-heading">{{ scheduled.length }} clients ${ heading }</div>
	<div class="ke-panel-content">
		<div class="ke-stack-item ke-navigable" ng-repeat="patient in scheduled" ng-click="onResultClick(patient)">
			${ ui.includeFragment("wellness", "patient/result.full") }
		</div>
		<div ng-if="scheduled.length == 0" style="text-align: center; font-style: italic">None</div>
	</div>
</div>