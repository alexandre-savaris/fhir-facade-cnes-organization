package ca.uhn.example.provider;

import ca.uhn.example.model.MyOrganization;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.ContactPoint.ContactPointUse;

/**
 * This is a simple resource provider which only implements "read/GET" methods, but
 * which uses a custom subclassed resource definition to add statically bound
 * extensions.
 * <p>
 * See the MyOrganization definition to see how the custom resource
 * definition works.
 */
public class OrganizationResourceProvider implements IResourceProvider {
    // TODO: comment.
    private String soapEnvelopeContent = null;
    private String cnesFilter = null;
    private String cnpjFilter = null;
    
    public OrganizationResourceProvider() {
    }
    
    public OrganizationResourceProvider(String soapEnvelopeContent,
        String cnesFilter, String cnpjFilter) {
        
        this.soapEnvelopeContent = soapEnvelopeContent;
        this.cnesFilter = cnesFilter;
        this.cnpjFilter = cnpjFilter;
    }

    /**
     * The getResourceType method comes from IResourceProvider, and must be overridden to indicate what type of resource this provider supplies.
     */
    @Override
    public Class<MyOrganization> getResourceType() {
       
        return MyOrganization.class;
    }

    /**
     * The "@Read" annotation indicates that this method supports the read operation. It takes one argument, the Resource type being returned.
     *
     * @param theId The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
     * @return Returns a resource matching this identifier, or null if none exists.
     */
    @Read()
    public MyOrganization getResourceById(@IdParam IdType theId) {
        // TODO: comment.
        MyOrganization retVal = null;  // 2530031
        
        try {
            
            String snippetFilter = null;
            
            if (theId.getIdPart().length() == 7) {
                // Filter by CNES.
                snippetFilter = MessageFormat.format(
                    this.cnesFilter, theId.getIdPart());
            } else if (theId.getIdPart().length() == 14) {
                // Filter by CNPJ. TODO: rever.
                snippetFilter = MessageFormat.format(
                    this.cnpjFilter, theId.getIdPart());
            }
            this.soapEnvelopeContent = MessageFormat.format(
                this.soapEnvelopeContent, snippetFilter);

            // Replace the placeholder with the resource ID.
            // TODO: for tests. use the CNES 2530031.
            //this.soapEnvelopeContent = MessageFormat.format(
            //    this.soapEnvelopeContent, theId.getIdPart());
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(
                        "https://servicoshm.saude.gov.br/cnes/EstabelecimentoSaudeService/v1r0"))
                    .version(HttpClient.Version.HTTP_2)
                    .POST(HttpRequest.BodyPublishers.ofString(this.soapEnvelopeContent))
                    .header("Content-Type", "text/xml")
                    .header("charset", "UTF-8")
                    .build();
            
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("----------------------------------------------------------");
            System.out.println(response.statusCode());
            System.out.println(response.body());
            System.out.println("----------------------------------------------------------");
            
            /*
            * We only support one organization, so the follwing
            * exception causes an HTTP 404 response if the
            * ID of "1" isn't used.
            */
            if (!"1".equals(theId.getValue())) {
                throw new ResourceNotFoundException(theId);
            }
            
            retVal = new MyOrganization();
            retVal.setId("1");
            retVal.addIdentifier().setSystem("urn:example:orgs").setValue("FooOrganization");
            retVal.addContact().getAddress().addLine("123 Fake Street").setCity("Toronto");
            retVal.addContact().getTelecomFirstRep().setUse(ContactPointUse.WORK).setValue("1-888-123-4567");
            
            // Populate the first, primitive extension
            retVal.setBillingCode(new CodeType("00102-1"));
            
            // The second extension is repeatable and takes a block type
            MyOrganization.EmergencyContact contact = new MyOrganization.EmergencyContact();
            contact.setActive(new BooleanType(true));
            contact.setContact(new ContactPoint());
            retVal.getEmergencyContact().add(contact);
            
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(OrganizationResourceProvider.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return retVal;
    }
}
