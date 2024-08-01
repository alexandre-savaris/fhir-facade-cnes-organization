package org.alexandresavaris.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import java.nio.charset.StandardCharsets;
import org.alexandresavaris.test.provider.OrganizationCnesResourceProvider;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import static org.assertj.core.api.Assertions.assertThat;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

// Tests regarding the Metadata Capability Statement.
public class MetadataCapabilityStatementTest {
    
    // For using the R4 cached context.
    private static final FhirContext ourCtx = FhirContext.forR4Cached();
    
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
    
    // Test for elements inside the response.
    @Test
    public void testElements() throws Exception {
        String output;
        
        HttpRequestBase httpGet
            = new HttpGet(
                ourServer.getBaseUrl()
                    + "/metadata?_elements=fhirVersion&_pretty=true"
            );
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<CapabilityStatement");
            assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }
    
    // Test for the correct use of HTTP methods.
    @Test
    public void testHttpMethods() throws Exception {
        String output;
        
        HttpRequestBase httpGet
            = new HttpGet(ourServer.getBaseUrl() + "/metadata");
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<CapabilityStatement");
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
                = new HttpPost(ourServer.getBaseUrl() + "/metadata");
            status = ourClient.execute(httpPost);
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(405, status.getStatusLine().getStatusCode());
            assertEquals(
                "<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\""
                    + Msg.code(388)
                    + "/metadata request must use HTTP GET\"/></issue></OperationOutcome>", output
            );
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
        
        /*
         * There is no @read on the RP below, so this should fail. Otherwise it
         * would be interpreted as a read on ID "metadata".
         */
        try {
            
            httpGet = new HttpGet(ourServer.getBaseUrl()
                + "/Organization/metadata");
            status = ourClient.execute(httpGet);
		assertEquals(400, status.getStatusLine().getStatusCode());
                
	} finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }

    // Test for content inside the response.
    @Test
    public void testResponseContainsBaseUrl() throws Exception {
        String output;
        
        HttpRequestBase httpGet
            = new HttpGet(ourServer.getBaseUrl() + "/metadata?_format=json");
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            CapabilityStatement cs = ourCtx.newJsonParser().parseResource(
                CapabilityStatement.class, output
            );
            assertEquals(
                ourServer.getBaseUrl() + "/", cs.getImplementation().getUrl()
            );
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }
    
    // Test for content inside the response.
    @Test
    public void testResponseContainsBaseUrlFixed() throws Exception {
        String output;
        
	ourServer.setServerAddressStrategy(
            new HardcodedServerAddressStrategy("http://foo/bar")
        );
        
        HttpRequestBase httpGet
            = new HttpGet(ourServer.getBaseUrl() + "/metadata?_format=json");
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
	try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            CapabilityStatement cs = ourCtx.newJsonParser().parseResource(
                CapabilityStatement.class, output
            );
            assertEquals("http://foo/bar", cs.getImplementation().getUrl());
            
        } finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
    }
    
    // Test for differences in the response by using the "summary" parameter.
    @Test
    public void testSummary() throws Exception {
        String output;
        
        // With summary.
        HttpRequestBase httpGet = new HttpGet(
            ourServer.getBaseUrl() + "/metadata?_summary=true&_pretty=true"
        );
        CloseableHttpResponse status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<CapabilityStatement");
            assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
            
	} finally {
            IOUtils.closeQuietly(status.getEntity().getContent());
        }
        
        // Without summary.
        httpGet
            = new HttpGet(ourServer.getBaseUrl() + "/metadata?_pretty=true");
        status = ourClient.execute(httpGet);
        
        try {
            
            output = IOUtils.toString(
                status.getEntity().getContent(), StandardCharsets.UTF_8
            );
            assertEquals(200, status.getStatusLine().getStatusCode());
            assertThat(output).contains("<CapabilityStatement");
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
