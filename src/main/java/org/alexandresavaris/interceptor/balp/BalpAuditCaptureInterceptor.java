/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.alexandresavaris.interceptor.balp;

import ca.uhn.fhir.storage.interceptor.balp.IBalpAuditContextServices;
import ca.uhn.fhir.storage.interceptor.balp.IBalpAuditEventSink;
import jakarta.annotation.Nonnull;

// ???
public class BalpAuditCaptureInterceptor
    extends ca.uhn.fhir.storage.interceptor.balp.BalpAuditCaptureInterceptor {
    
    public BalpAuditCaptureInterceptor(
        @Nonnull IBalpAuditEventSink theAuditEventSink,
        @Nonnull IBalpAuditContextServices theContextServices) {
        
        super(theAuditEventSink, theContextServices);
    }    
}
