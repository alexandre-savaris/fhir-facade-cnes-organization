package org.alexandresavaris.fhir.facade.cnes.organization.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.storage.interceptor.balp.IBalpAuditEventSink;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hl7.fhir.r4.model.AuditEvent;

/**
 * This implementation of the {@link IBalpAuditEventSink} saves audit events to
 * files, using a standard fhir <i>create</i> event.
 */
public class FileBalpSink implements IBalpAuditEventSink {
    // For logging purposes.
    private static final Logger logger
        = LoggerFactory.getLogger(FileBalpSink.class);
    // FHIR context for encoding the resource content.
    private final FhirContext ctx;
    // Path for storing the files.
    private final String path;
    
    public FileBalpSink(FhirContext ctx, String path) {
        
        this.ctx = ctx;
        this.path = path;
    }
    
    @Override
    public void recordAuditEvent(AuditEvent theAuditEvent) {
        
        IParser jsonParser = this.ctx.newJsonParser();
        jsonParser.setPrettyPrint(true);
        String encoded = jsonParser.encodeResourceToString(theAuditEvent);
        
        try {
            
            String fileNameTemplate = this.path + "/AuditEvent_{0}.json";
            String currentTimestamp
                = new SimpleDateFormat(
                    "yyyyMMdd_HHmmss_SSS").format(new Date()
                );
            String fileName = java.text.MessageFormat.format(
                fileNameTemplate, currentTimestamp);

            Files.write(
                Paths.get(fileName), encoded.getBytes()
            );
            
        } catch (IOException ex) {
            logger.error(
                "Error saving the AuditEvent resource instance: {}",
                ex.toString()
            );
        }
    }
}
