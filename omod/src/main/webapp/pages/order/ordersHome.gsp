<%
    ui.decorateWith("wellness", "standardPage", [ layout: "sidebar" ])

    def menuItems = [
            [ label: "Photo uploads", iconProvider: "wellness", icon: "buttons/upload.png", href: ui.pageLink("wellness", "order/photoUpload") ]
    ]
%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("wellness", "patient/patientSearchForm", [ defaultWhich: "checked-in" ]) }
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
    ${ ui.includeFragment("wellness", "patient/patientSearchResults", [ pageProvider: "wellness", page: "order/orderViewPatient" ]) }
</div>

<script type="text/javascript">
    jQuery(function() {
        jQuery('input[name="query"]').focus();
    });
</script>