<%@ page import="org.wso2.carbon.identity.application.authentication.endpoint.util.CharacterEncoder"%>
<div id="loginTable1" class="identity-box">
    <%
        loginFailed = CharacterEncoder.getSafeText(request.getParameter("loginFailed"));
        if (loginFailed != null) {

    %>
            <div class="alert alert-error">
                <fmt:message key='<%=CharacterEncoder.getSafeText(request.getParameter
                ("errorMessage"))%>'/>
            </div>
    <% } %>
	
    <!--Password-->
    <div class="control-group">
        <label class="control-label" for="password">Confirmation code:</label>

        <div class="controls">
            <input type="password" id='confirmationCode' name="confirmationCode"  class="input-xlarge" size='30'/>
            <input type="hidden" name="sessionDataKey" value='<%=CharacterEncoder.getSafeText(request.getParameter("sessionDataKey"))%>'/>
         </div>
    </div>

    <div class="form-actions">
        <input type="submit" value='Submit confirmation code' class="btn btn-primary">
    </div>

</div>

