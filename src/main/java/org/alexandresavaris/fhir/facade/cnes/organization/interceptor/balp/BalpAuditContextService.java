package org.alexandresavaris.fhir.facade.cnes.organization.interceptor.balp;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.storage.interceptor.balp.IBalpAuditContextServices;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Reference;

// Complementary information to the AuditEvent instance.
public class BalpAuditContextService implements IBalpAuditContextServices {
    
    @Nonnull
    @Override
    public Reference getAgentClientWho(RequestDetails theRequestDetails) {
        
        Reference client = new Reference();
        client.setDisplay("<REPLACE_WITH_CLIENT_AGENT>");
//        client.getIdentifier()
//            .setSystem("http://example.org/clients")
//            .setValue("growth_chart");
        
        return client;
    }
    
    @Nonnull
    @Override
    public Reference getAgentUserWho(RequestDetails theRequestDetails) {
        
        Reference user = new Reference();
        user.setDisplay("<REPLACE_WITH_CLIENT_USER>");
//        user.getIdentifier()
//            .setSystem("http://example.org/users")
//            .setValue("my_username");
        
        return user;
    }
}
