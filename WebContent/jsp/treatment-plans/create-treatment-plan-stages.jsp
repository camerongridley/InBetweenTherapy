<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:import url="../header.jsp"/>

    <div class="page-header">
        <h1>Create a Treatment Plan</h1>

        <h2>Select or Create the Plan's Stages</h2>
    </div>
    <p>
        Plan ID: ${newPlan.treatmentPlanID} - Plan Name: ${newPlan.name} - Plan Description: ${newPlan.description} - Issue ID: ${newPlan.treatmentIssueID}
    </p>

    <p>

    <form class="form-inline" action="/CreateTreatmentPlan" method="POST">
        <div class="row">
            <div class="col-md-6">

            </div>


            <input type="hidden" name="requestedAction" value="planNameAndIssue"/>
            <button type="submit" class="btn btn-primary">Next Step >></button>

        </div>
    </form>
    </p>


<c:import url="/jsp/footer.jsp"/>