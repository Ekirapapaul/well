<%
    ui.includeJavascript("wellness", "controllers/appointments.js")
%>
<style>
table.toggle tr th {
    background-color: #ACD244;
    text-align: left;
}
table.toggle tr td {
    text-align: left;
}
table.toggle tr:nth-child(even) {
    background-color: #E3E4FA;
}

table.toggle tr:nth-child(odd) {
    background-color: #FDEEF4;
}
</style>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Appointment Management</div>
    <div class="ke-page-content">
        <fieldset>
            <legend>Filtering Options</legend>
            <table>
                <tr>
                    <td>
                        <span class="ke-field-content">
                            <label class="ke-field-label">Appointment date</label>
                            <input type="text" name="appointmentDate" id="appointmentDate" onclick="startDate()" value="${today}"/>
                            <img
                                    src="${ui.resourceLink("wellness", "images/buttons/calendarIcon.png")}"
                                    class="calendarIcon" alt=""
                                    onClick="document.getElementById('appointmentDate').focus();" />
                        </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span class="ke-field-content">
                            <label class="ke-field-label">Provider</label>
                            <select name="provider" id="provider">
                                <% providerList.each{%>
                                    <option value="${it.providerId}">${it.name}</option>
                                <%}%>
                            </select>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span class="ke-field-content">
                            <label class="ke-field-label">Appointment Type</label>
                            <select name="appointmentType" id="appointmentType">
                                <% appointmentTypeList.each{%>
                                   <option value="${it.appointmentTypeId}">${it.name}</option>
                                <%}%>
                            </select>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span class="ke-field-content">
                            <label class="ke-field-label">Appointment Status</label>
                            <select name="appointmentStatus" id="status">
                                <option value="Cancelled">Cancel</option>
                                <option value="Missed">Missed</option>
                                <option value="Scheduled">Scheduled</option>
                                <option value="Completed">Completed</option>
                            </select>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" name="apply" id="apply" value="Apply filters">
                    </td>
                </tr>
            </table>
        </fieldset>
        </div>
</div>
<br />
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Available Appointments</div>
        <div class="ke-page-content"ng-controller="ActiveAppointmentsBlocks" ng-init="init()" >
            <table id="availableAppointments" class="toggle" width="100%">
                    <tr>
                        <th>Client</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Provider</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Notes</th>
                    </tr>
                    <tr ng-repeat="appointment in activeAppointments">
                        <td><a href="manageScheduledAppointments.page?appointmentId={{appointment.id}}"> {{appointment.names}}</a></td>
                        <td>{{appointment.date}}</td>
                        <td>{{appointment.time}}</td>
                        <td>{{appointment.provider}}</td>
                        <td>{{appointment.type}}</td>
                        <td>{{appointment.status}}</td>
                        <td>{{appointment.notes}}</td>
                    </tr>
            </table>
        </div>
</div><script type="text/javascript">
    function startDate() {
        jQuery("#appointmentDate").datepicker({
            dateFormat: 'dd/mm/yy',
            gotoCurrent: true,
            minDate: new Date()
        });
    }
    jQuery(function () {
        startDate();
    })
</script>


