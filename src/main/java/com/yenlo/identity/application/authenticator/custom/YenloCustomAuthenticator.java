package com.yenlo.identity.application.authenticator.custom;

import com.yenlo.identity.application.authenticator.custom.internal.YenloCustomAuthenticatorConstants;
import com.yenlo.identity.application.authenticator.custom.internal.YenloCustomAuthenticatorEmailSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AbstractApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorFlowStatus;
import org.wso2.carbon.identity.application.authentication.framework.LocalApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.config.ConfigurationFacade;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.authentication.framework.exception.InvalidCredentialsException;
import org.wso2.carbon.identity.application.authentication.framework.exception.LogoutFailedException;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vklevko on 20-7-2015.
 */
public class YenloCustomAuthenticator extends AbstractApplicationAuthenticator implements LocalApplicationAuthenticator {

    private static Log log = LogFactory.getLog(YenloCustomAuthenticator.class);
    public static final String CONFIRMATION_CODE = "1234";

    @Override
    public boolean canHandle(HttpServletRequest request) {
        String confirmationCode = request.getParameter("confirmationCode");


        return confirmationCode != null;

    }

    @Override
    public AuthenticatorFlowStatus process(HttpServletRequest request,
                                           HttpServletResponse response, AuthenticationContext context)
            throws AuthenticationFailedException, LogoutFailedException {

        if (context.isLogoutRequest()) {
            return AuthenticatorFlowStatus.SUCCESS_COMPLETED;
        } else {
            return super.process(request, response, context);
        }
    }

    @Override
    protected void initiateAuthenticationRequest(HttpServletRequest request,
                                                 HttpServletResponse response, AuthenticationContext context)
            throws AuthenticationFailedException {

        String loginPage = ConfigurationFacade.getInstance().getAuthenticationEndpointURL();
        String queryParams = FrameworkUtils
                .getQueryStringWithFrameworkContextId(context.getQueryParams(),
                        context.getCallerSessionKey(),
                        context.getContextIdentifier());

        try {
            YenloCustomAuthenticatorEmailSender.sendEmail(request.getParameter("username"), CONFIRMATION_CODE);

            String retryParam = "";

            if (context.isRetrying()) {
                retryParam = "&authFailure=true&authFailureMsg=login.fail.message";
            }

            response.sendRedirect(response.encodeRedirectURL(loginPage + ("?" + queryParams))
                    + "&authenticators=" + getName() + ":" + "LOCAL" + retryParam);
        } catch (IOException e) {
            throw new AuthenticationFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected void processAuthenticationResponse(HttpServletRequest request,
                                                 HttpServletResponse response, AuthenticationContext context)
            throws AuthenticationFailedException {

        String confirmationCode = request.getParameter("confirmationCode");

        boolean isAuthenticated = false;


        if (confirmationCode != null) {
            isAuthenticated = CONFIRMATION_CODE.equals(confirmationCode);
        } else {
            throw new AuthenticationFailedException("Can not confirm authorization code.");
        }


        if (!isAuthenticated) {
            if (log.isDebugEnabled()) {
                log.debug("user authentication failed due to invalid credentials.");
            }

            throw new InvalidCredentialsException();
        }
        else context.setSubject(context.getCurrentAuthenticatedIdPs().get("LOCAL").getUsername());
    }

    @Override
    protected boolean retryAuthenticationEnabled() {
        return true;
    }

    @Override
    public String getContextIdentifier(HttpServletRequest request) {
        return request.getParameter("sessionDataKey");
    }

    @Override
    public String getFriendlyName() {
        return YenloCustomAuthenticatorConstants.AUTHENTICATOR_FRIENDLY_NAME;
    }

    @Override
    public String getName() {
        return YenloCustomAuthenticatorConstants.AUTHENTICATOR_NAME;
    }
}

