<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<ol class="breadcrumb">
  <li><span class="breadcrumb-icon" title="Owner">O</span> ${owner.email}</li>
  <c:if test="${treatmentPlan!=null  && not empty treatmentPlan.title}"><li><span class="breadcrumb-icon" title="Treatment Plan">P</span> ${treatmentPlan.title}</li></c:if>
  <c:if test="${stage!=null  && not empty stage.title}"><li><span class="breadcrumb-icon" title="Stage">S</span> ${stage.title }</li></c:if>
  <c:if test="${task!=null && not empty task.title}"><li><span class="breadcrumb-icon" title="Task">T</span> ${task.title }</li></c:if>
</ol>
