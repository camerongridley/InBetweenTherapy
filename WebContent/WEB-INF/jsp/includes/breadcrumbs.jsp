<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<ol class="breadcrumb">
  <li title="Owner"><strong>O</strong> ${owner.email}</li>
  <c:if test="${treatmentPlan!=null  && not empty treatmentPlan.title}"><li title="Treatment Plan"><strong>P</strong> ${treatmentPlan.title}</li></c:if>
  <c:if test="${stage!=null  && not empty stage.title}"><li title="Stage"><strong>S</strong> ${stage.title }</li></c:if>
  <c:if test="${task!=null && not empty task.title}"><li title="Task"><strong>T</strong> ${task.title }</li></c:if>
</ol>
