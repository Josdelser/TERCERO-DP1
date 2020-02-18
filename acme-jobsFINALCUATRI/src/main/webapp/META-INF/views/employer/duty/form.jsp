<%@page language="java" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%
	String miguel = request.getParameter("descriptor_id");
	session.setAttribute("idDesc", miguel);

%>
 

<acme:form readonly="false">

    <acme:form-textbox code="employer.duty.form.label.title" path ="title"/><br/>
    <acme:form-textbox code="employer.duty.form.label.description" path ="description"/><br/>
    <acme:form-textbox code="employer.duty.form.label.percentage" path ="percentage"/><br/>
   
    <acme:form-return code="employer.duty.form.button.return" />
    <acme:form-submit test="${command == 'show' and status =='false' }" code="employer.duty.form.button.update" action="/employer/duty/update" />
    <acme:form-submit test="${command == 'create'}" code="employer.duty.form.button.create" action="/employer/duty/create?descriptor_id2=${idDescr}" />
    <acme:form-submit test="${command == 'update'}" code="employer.duty.form.button.update" action="/employer/duty/update" />
	<acme:form-submit test="${command == 'show' and status == 'false' }" code="employer.duty.form.button.delete" action="/employer/duty/delete"/>
	<acme:form-submit test="${command == 'delete'}" code="employer.duty.form.button.delete" action="/employer/duty/delete" />
 
	
	
</acme:form>
