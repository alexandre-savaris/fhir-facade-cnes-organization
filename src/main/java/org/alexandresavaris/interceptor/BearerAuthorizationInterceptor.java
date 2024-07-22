package org.alexandresavaris.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import java.util.List;

public class BearerAuthorizationInterceptor extends AuthorizationInterceptor {

    // Build the rules list to be verified on each request.
    @Override
    public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
        
        String authHeader = theRequestDetails.getHeader("Authorization");
        // TODO: introduce DB.
        if ("Bearer dfw98h38r".equals(authHeader)) {
            return new RuleBuilder().allowAll().build();
        } else {
            throw new AuthenticationException(
                Msg.code(644) + "Missing or invalid Authorization header value"
            );
        }
    }
}
