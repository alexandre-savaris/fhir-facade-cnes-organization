package org.alexandresavaris.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import java.util.List;

public class BearerAuthorizationInterceptor extends AuthorizationInterceptor {
    // The Bearer Token to authorize access with.
    private String bearerToken = null;
    
    public BearerAuthorizationInterceptor() {
    }

    public BearerAuthorizationInterceptor(String bearerToken) {
        
        this.bearerToken = bearerToken;
    }

    // Build the rules list to be verified on each request.
    @Override
    public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
        
        String authHeader = theRequestDetails.getHeader("Authorization");
        if (("Bearer " + this.bearerToken).equals(authHeader)) {
            return new RuleBuilder().allowAll().build();
        } else {
            throw new AuthenticationException(
                Msg.code(644) + "Missing or invalid Authorization header value"
            );
        }
    }
}
