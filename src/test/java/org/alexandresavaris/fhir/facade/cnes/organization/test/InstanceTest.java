package org.alexandresavaris.fhir.facade.cnes.organization.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import java.nio.charset.StandardCharsets;
import org.alexandresavaris.fhir.facade.cnes.organization.test.provider.OrganizationCnesResourceProvider;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

// Tests regarding the retrieval of instances for the OrganizationCnes resource.
public class InstanceTest {
    // The logical ID for all the tests.
    private final String logicalId = "1234567";
    
    /*
     * Use a narrative generator. This is a completely optional step, but can be
     * useful as it causes HAPI to generate narratives for resources which don't
     * otherwise have one.
     */    
    CustomThymeleafNarrativeGenerator narrativeGen
        = new CustomThymeleafNarrativeGenerator(
            "classpath:org/alexandresavaris/fhir/facade/cnes/organization/narratives/narratives.properties"
        );

    // For using the R4 cached context.
    private final FhirContext ourCtx
        = FhirContext.forR4Cached().setNarrativeGenerator(narrativeGen);
    
    // The mock server.
    @RegisterExtension
    private final RestfulServerExtension ourServer
        = new RestfulServerExtension(ourCtx)
            .setDefaultResponseEncoding(EncodingEnum.XML)
            .registerProvider(new OrganizationCnesResourceProvider())
            .setDefaultPrettyPrint(false)
            .withServer(
                s -> s.setServerConformanceProvider(
                    new ServerCapabilityStatementProvider(s)
                )
            );

    // The client.
    @RegisterExtension
    private final HttpClientExtension ourClient = new HttpClientExtension();
    
    // To control the mock server's shutdown behavior.
    static {
        HapiSystemProperties.enableTestMode();
    }

    // After each test, execute.
    @AfterEach
    public void after() {
        
        ourServer.setServerAddressStrategy(
            new IncomingRequestAddressStrategy()
        );
    }
    
    // Test the logical ID.
    @Test
    public void testTheLogicalId() throws Exception {
        String output;
        
        HttpRequestBase httpGet
            = new HttpGet(
                ourServer.getBaseUrl()
                    + "/Organization/" + this.logicalId
            );
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains(
                "<id value=\"" + this.logicalId + "\"/>"
            );
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }

    // Test for other elements inside the response.
    @Test
    public void testOtherElements() throws Exception {
        String output;
        
        HttpRequestBase httpGet
            = new HttpGet(
                ourServer.getBaseUrl()
                    + "/Organization/" + this.logicalId
            );
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertThat(output).contains("<Organization", "</Organization>");
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<text>", "</text>");
            assertThat(output).contains("<extension", "</extension>");
            assertThat(output).contains("<identifier>", "</identifier>");
            assertThat(output).contains("<type>", "</type>");
            assertThat(output).contains("<name");
            assertThat(output).contains("<alias");
            assertThat(output).contains("<address>", "</address>");
            assertThat(output).contains("<contact>", "</contact>");
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }
    
    // Test for the correct use of HTTP methods.
    @Test
    public void testHttpMethods() throws Exception {
        String output;

        HttpRequestBase httpGet
            = new HttpGet(
                ourServer.getBaseUrl()
                    + "/Organization/" + this.logicalId
            );
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<Organization");
            assertThat(status.getFirstHeader("X-Powered-By").getValue())
                .contains("HAPI FHIR " + VersionUtil.getVersion());
            assertThat(status.getFirstHeader("X-Powered-By").getValue())
                .contains("REST Server (FHIR Server; FHIR "
                    + ourCtx.getVersion().getVersion().getFhirVersionString()
                    + "/" + ourCtx.getVersion().getVersion().name() + ")");
            
	} finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }

	try {

            HttpRequestBase httpPost
                = new HttpPost(
                    ourServer.getBaseUrl()
                        + "/Organization/" + this.logicalId
                );
            status = ourClient.execute(httpPost);
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(400, status.getStatusLine().getStatusCode());
            assertThat(output)
                .contains(
                    "Invalid request: The FHIR endpoint on this server does not know how to handle POST operation"
                );
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }

    // Test for differences in the response by using the "summary" parameter.
    @Test
    public void testSummary() throws Exception {
        String output;
        
        // With summary.
        HttpRequestBase httpGet
            = new HttpGet(
                ourServer.getBaseUrl()
                    + "/Organization/" + this.logicalId + "?_summary=true"
            );
        CloseableHttpResponse status = ourClient.execute(httpGet);

        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<Organization");
            assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
        
        // Without summary.
        httpGet
            = new HttpGet(
                ourServer.getBaseUrl()
                    + "/Organization/" + this.logicalId
            );
        status = ourClient.execute(httpGet);

        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<Organization");
            assertThat(output).doesNotContain("<meta>", "SUBSETTED", "</meta>");
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }
    
    // After all tests, execute.
    @AfterAll
    public static void afterClassClearContext() throws Exception {
        
	TestUtil.randomizeLocaleAndTimezone();
    }
}
