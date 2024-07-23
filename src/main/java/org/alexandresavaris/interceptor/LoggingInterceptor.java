package org.alexandresavaris.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The logging interceptor implementation.
public class LoggingInterceptor {
    private static final Logger logger
        = LoggerFactory.getLogger(LoggingInterceptor.class);
    
    @Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
    public boolean logRequestDetails(RequestDetails theRequest) {
        
        logger.info(
            "Handling {} client operation on ID {}",
            theRequest.getRequestType(),
            theRequest.getId());
        
        return true; // Processing should continue.
    }
}
