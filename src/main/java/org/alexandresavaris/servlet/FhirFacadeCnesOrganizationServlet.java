package org.alexandresavaris.servlet;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.example.provider.OrganizationResourceProvider;
import ca.uhn.example.provider.PatientResourceProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This servlet is the actual FHIR server itself
 */
public class FhirFacadeCnesOrganizationServlet extends RestfulServer {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public FhirFacadeCnesOrganizationServlet() {
        
    	super(FhirContext.forR5Cached()); // This is an R5 server
    }
	
    /**
     * This method is called automatically when the
     * servlet is initializing.
     */
    @Override
    public void initialize() {
            
        try {
                
            // Load the content of the SOAP envelope to be sent to the endpoint.
            URL url = this.getClass()
                .getClassLoader()
                .getResource("soap/envelopes/consultarEstabelecimentoSaude.xml");
            File file = new File(url.getFile());
            String soapEnvelopeContent = new String(Files.readAllBytes(file.toPath()));

            // Load the XML snippet for filtering by CNES.
            url = this.getClass()
                .getClassLoader()
                .getResource("soap/envelopes/filtroCnes.xml");
            file = new File(url.getFile());
            String cnesFilter = new String(Files.readAllBytes(file.toPath()));

            // Load the XML snippet for filtering by CNPJ.
            url = this.getClass()
                .getClassLoader()
                .getResource("soap/envelopes/filtroCnes.xml");
            file = new File(url.getFile());
            String cnpjFilter = new String(Files.readAllBytes(file.toPath()));

            /*
             * Two resource providers are defined. Each one handles a specific
             * type of resource.
             */
            List<IResourceProvider> providers = new ArrayList<>();
            providers.add(new PatientResourceProvider());
            providers.add(new OrganizationResourceProvider(
                soapEnvelopeContent, cnesFilter, cnpjFilter));
            setResourceProviders(providers);
                
            /*
             * Use a narrative generator. This is a completely optional step,
             * but can be useful as it causes HAPI to generate narratives for
             * resources which don't otherwise have one.
             */
            INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
            getFhirContext().setNarrativeGenerator(narrativeGen);
                
            /*
             * Use nice coloured HTML when a browser is used to request the content
             */
            registerInterceptor(new ResponseHighlighterInterceptor());
                
        } catch (IOException ex) {
            Logger.getLogger(FhirFacadeCnesOrganizationServlet.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
}
