package com.yenlo.identity.application.authenticator.custom.internal;

import com.yenlo.identity.application.authenticator.custom.YenloCustomAuthenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Hashtable;


/**
 * @scr.component name="com.yenlo.identity.application.authenticator.custom.component" immediate="true"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */
public class YenloCustomAuthenticatorComponent {
    private static Log log = LogFactory.getLog(YenloCustomAuthenticatorComponent.class);

    private static RealmService realmService;

    protected void activate(ComponentContext ctxt) {

        YenloCustomAuthenticator customAuth = new YenloCustomAuthenticator();
        Hashtable<String, String> props = new Hashtable<String, String>();

        ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(), customAuth, props);

        if (log.isDebugEnabled()) {
            log.info("YenloCustomAuthenticator bundle is activated");
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.info("YenloCustomAuthenticator bundle is deactivated");
        }
    }

    protected void setRealmService(RealmService realmService) {
        log.debug("Setting the Realm Service");
        YenloCustomAuthenticatorComponent.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        log.debug("UnSetting the Realm Service");
        YenloCustomAuthenticatorComponent.realmService = null;
    }

    public static RealmService getRealmService() {
        return realmService;
    }
}
