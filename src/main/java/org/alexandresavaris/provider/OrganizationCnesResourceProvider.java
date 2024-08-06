package org.alexandresavaris.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.alexandresavaris.model.OrganizationCnes;
import org.alexandresavaris.util.NamespaceContextMap;
import org.alexandresavaris.util.Utils;
import org.hl7.fhir.r4.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Definition of a ResourceProvider for managing instances of the
 * <code>OrganizationCnes</code> class.
 */
public class OrganizationCnesResourceProvider implements IResourceProvider {
    // Endpoint for accessing the SOAP webservice.
    private String endpointEstabelecimentoSaudeService = null;
    // Username and password for the service.
    private String username;
    private String password;
    // The content of the SOAP envelope to be sent to the webservice.
    private String contentOfSoapEnvelope = null;
    // Code snippets for filtering.
    private String codeSnippetForFilteringByCnes = null;
    private String codeSnippetForFilteringByCnpj = null;

    // The empty constructor.
    public OrganizationCnesResourceProvider() {
    }
    
    // The parameterized constructor.
    public OrganizationCnesResourceProvider(
        String endpointEstabelecimentoSaudeService,
        String username,
        String password,
        String soapEnvelopeContent,
        String cnesFilter,
        String cnpjFilter) {
        
        this.endpointEstabelecimentoSaudeService
            = endpointEstabelecimentoSaudeService;
        this.username = username;
        this.password = password;
        this.contentOfSoapEnvelope = soapEnvelopeContent;
        this.codeSnippetForFilteringByCnes = cnesFilter;
        this.codeSnippetForFilteringByCnpj = cnpjFilter;
    }

    /**
     * The getResourceType method comes from IResourceProvider, and must be
     * overridden to indicate what type of resource this provider supplies.
     */
    @Override
    public Class<OrganizationCnes> getResourceType() {
       
        return OrganizationCnes.class;
    }

    /**
     * The "@Read" annotation indicates that this method supports the
     * read operation. It takes one argument, the Resource type being returned.
     *
     * @param theId The read operation takes one parameter, which must be
     * of type IdDt and must be annotated with the "@Read.IdParam" annotation.
     * @return Returns a resource matching this identifier, or null if none
     * exists.
     */
    @Read(type = OrganizationCnes.class)
    public OrganizationCnes getResourceById(@IdParam IdType theId) {
        // The expected length for a CNES value.
        final int expectedCnesLength = 7;
        // The expected length for a CNPJ value.
        final int expectedCnpjLength = 14;
        // The resource instance to be returned.
        OrganizationCnes retVal = null;
        
        try {

            // According to the length of the resource Id, replace the
            // corresponding placeholder.
            String snippetFilter = null;
            if (theId.getIdPart().length() == expectedCnesLength) {
                // Filter by CNES.
                snippetFilter = MessageFormat.format(
                    this.codeSnippetForFilteringByCnes, theId.getIdPart()
                );
            } else if (theId.getIdPart().length() == expectedCnpjLength) {
                // Filter by CNPJ.
                // TODO: following the documentation from DATASUS, it's not
                // working. Shall be reviewed in the next release.
                snippetFilter = MessageFormat.format(
                    this.codeSnippetForFilteringByCnpj, theId.getIdPart()
                );
            }
            String updatedContentOfSoapEnvelope = MessageFormat.format(
                this.contentOfSoapEnvelope, username, password, snippetFilter
            );
            
            // Access the SOAP webservice.
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(this.endpointEstabelecimentoSaudeService))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(
                    updatedContentOfSoapEnvelope)
                )
                .header("Content-Type", "text/xml")
                .header("charset", "UTF-8")
                .build();
            HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
            
            // For debugging purposes.
            // Uncomment to see the response body from the SOAP Webservice.
//            System.out.println(
//                "----------------------------------------------------------"
//            );
//            System.out.println(response.statusCode());
//            System.out.println(response.body());
//            System.out.println(
//                "----------------------------------------------------------"
//            );
            
            int responseStatusCode = response.statusCode();
            if (responseStatusCode != 200) {
                throw new Exception(
                    "O webservice SOAP retornou o código de status HTTP "
                        + responseStatusCode
                        + " ao acessar os dados do estabelecimento de saúde com o Id: "
                        + theId);
            }
            
            // Fill in the resource with data from the response.
            retVal = new OrganizationCnes();
            
            // The logical ID replicates the Organization's CNES.
            retVal.setId(theId);
            
            // XML parsing for content extraction.
            DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document
                = builder.parse(
                    new InputSource(new StringReader(response.body()))
                );
            
            // Setting namespaces for using XPath.
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            NamespaceContext context = new NamespaceContextMap(
                Utils.xmlNamespaces);
            xpath.setNamespaceContext(context);

            // CodigoCNES -> Identifier: CNES.
            String cnes
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("cnes"),
                    0
                );
            if (cnes != null) {
                retVal.addIdentifier()
                    .setSystem("urn:oid:" + Utils.oids.get("cnes"))
                    .setValue(cnes);
            }

            // CodigoUnidade -> Identifier: Unity code.
            String unityCode =
                extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("unityCode"),
                    0
                );
            if (unityCode != null) {
                retVal.addIdentifier()
                    .setSystem(Utils.namingSystems.get("unityCode"))
                    .setValue(unityCode);
            }

            // numeroCNPJ -> Identifier: CNPJ.
            String cnpj
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("cnpj"),
                    0
                );
            if (cnpj != null) {
                retVal.addIdentifier()
                    .setSystem(Utils.namingSystems.get("cnpj"))
                    .setValue(cnpj);
            }

            // nomeFantasia -> name.
            String name
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("name"),
                    0
                );
            if (name != null) {
                retVal.setName(name);
            }

            // nomeEmpresarial -> alias.
            String alias
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("alias"),
                    0
                );
            if (alias != null) {
                retVal.addAlias(alias);
            }
            
            // Endereco -> Address.
            String street = extractSingleValueFromXml(document, xpath,
                Utils.xpathExpressions.get("street"),
                0
            );
            String number = extractSingleValueFromXml(document, xpath,
                Utils.xpathExpressions.get("number"),
                0
            );
            String neighborhood = extractSingleValueFromXml(document, xpath,
                Utils.xpathExpressions.get("neighborhood"),
                0
            );
            String city = extractSingleValueFromXml(document, xpath,
                Utils.xpathExpressions.get("city"),
                0
            );
            String state = extractSingleValueFromXml(document, xpath,
                Utils.xpathExpressions.get("state"),
                0
            );
            String addressTextTemplate = "{0}, {1} - {2} - {3} - {4}";
            String addressText = java.text.MessageFormat.format(
                addressTextTemplate, street, number, neighborhood, city, state);
            
            Address address = new Address()
                .setUse(Address.AddressUse.WORK)
                .setType(Address.AddressType.BOTH)
                .setText(addressText)
                .setCity(city)
                .setState(state)
                .setPostalCode(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("postalCode"),
                        0
                    )
                )
                .setCountry("BRA");
            
            // Extensions for IBGE codes.
            String cityCodeIbge
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("cityCodeIbge"),
                    0
                );
            if (cityCodeIbge != null) {
                Extension cityCodeIbgeExtension
                    = new Extension(Utils.extensions.get("cityCodeIbge"));
                cityCodeIbgeExtension.setValue(
                    new Coding()
                        .setSystem("urn:oid:" + Utils.oids.get("ibgeCode"))
                        .setCode(cityCodeIbge)
                        .setDisplay(
                            new String(
                                "Código do município no IBGE"
                                    .getBytes("ISO-8859-1"),
                                "UTF-8"
                            )
                        )
                );
                address.addExtension(cityCodeIbgeExtension);
            }
            String stateCodeIbge
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("stateCodeIbge"),
                    0
                );
            if (stateCodeIbge != null) {
                Extension stateCodeIbgeExtension
                    = new Extension(Utils.extensions.get("stateCodeIbge"));
                stateCodeIbgeExtension.setValue(
                    new Coding()
                        .setSystem("urn:oid:" + Utils.oids.get("ibgeCode"))
                        .setCode(stateCodeIbge)
                        .setDisplay(
                            new String(
                                "Código da UF no IBGE".getBytes("ISO-8859-1"),
                                "UTF-8"
                            )
                        )
                );
                address.addExtension(stateCodeIbgeExtension);
            }
            
            // Geolocation extensions.
            String latitude
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("latitude"),
                    0
                );
            String longitude
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("longitude"),
                    0
                );
            if (latitude != null && longitude != null) {
                Extension geolocationExtension
                    = new Extension(Utils.extensions.get("geolocation"));
                Extension latitudeExtension
                    = new Extension(Utils.extensions.get("latitude"));
                latitudeExtension.setValue(
                    new DecimalType(latitude)
                );
                Extension longitudeExtension
                    = new Extension(Utils.extensions.get("longitude"));
                longitudeExtension.setValue(
                    new DecimalType(longitude)
                );
                geolocationExtension.addExtension(latitudeExtension);
                geolocationExtension.addExtension(longitudeExtension);
                // Completing the address to be returned.
                address.addExtension(geolocationExtension);
            }
            
            retVal.addAddress(address);
            
            // dataAtualizacao -> Extension (update date).
            String updateDate
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("updateDate"),
                    0
                );
            if (updateDate != null) {
                retVal.setUpdateDate(
                    new DateType(updateDate)
                );
            }

            // numeroCPF -> Extension (Director's CPF).
            // NOTE: despite its availability in the original response from the
            // SOAP Webservice, the Director's CPF is considered personal data
            // and is replaced by a fake one.
            retVal.setDirectorCpf(
                new Coding()
                    .setSystem("urn:oid:" + Utils.oids.get("cpf"))
                    .setCode("42424242424")
                    .setDisplay(
                        new String(
                            "Número do CPF do Diretor".getBytes("ISO-8859-1"),
                            "UTF-8"
                        )
                    )
            );

            // Nome -> Extension (Director's name).
            // NOTE: despite its availability in the original response from the
            // SOAP Webservice, the Director's name is considered personal data
            // and is replaced by a fake one.
            retVal.setDirectorName(
                new HumanName().setText("Marvin the Paranoid Android")
            );

            // tipoUnidade -> type.
            String unityType
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("unityType"),
                    0
                );
            if (unityType != null) {
                retVal.addType(
                    new CodeableConcept(
                        new Coding()
                            .setSystem(Utils.valueSets.get("type"))
                            .setCode(unityType)
                            .setDisplay(
                                extractSingleValueFromXml(document, xpath,
                                    Utils.xpathExpressions.get(
                                        "unityDescription"
                                    ),
                                    0
                                )
                            )
                    )
                );
            }
                
            // Telefone -> contact
            String phoneNumber
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("phoneNumber"),
                    0
                );
            if (phoneNumber != null) {
                String phoneTemplate = "{0} {1}";
                String phone = java.text.MessageFormat.format(
                    phoneTemplate,
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("phoneAreaCode"),
                        0
                    ),
                    phoneNumber
                );
                retVal.addContact()
                    .addTelecom(
                        new ContactPoint()
                            .setSystem(ContactPoint.ContactPointSystem.PHONE)
                            .setValue(phone)
                            .setUse(ContactPoint.ContactPointUse.WORK)
                    )
                    .setPurpose(
                        new CodeableConcept(
                            new Coding()
                                .setSystem(Utils.namingSystems.get("phoneType"))
                                .setCode(
                                    extractSingleValueFromXml(document, xpath,
                                        Utils.xpathExpressions.get("phoneType"),
                                        0
                                    )
                                )
                                .setDisplay(
                                    extractSingleValueFromXml(document, xpath,
                                        Utils.xpathExpressions.get(
                                            "phoneDescription"
                                        ),
                                        0
                                    )
                                )
                        )
                    );
            }

            // Email -> contact
            String email
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("email"),
                    0
                );
            if (email != null) {
                retVal.addContact()
                    .addTelecom(
                        new ContactPoint()
                            .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                            .setValue(email)
                            .setUse(ContactPoint.ContactPointUse.WORK)
                    )
                    .setPurpose(
                        new CodeableConcept(
                            new Coding()
                                .setSystem(Utils.namingSystems.get("emailType"))
                                .setCode(
                                    extractSingleValueFromXml(document, xpath,
                                        Utils.xpathExpressions.get("emailType"),
                                        0
                                    )
                                )
                        )
                    );
            }
            
            // perteceSistemaSUS -> Extension (Is the Organization part of SUS?).
            String isSus
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("isSus"),
                    0
                );
            if (isSus != null) {
                retVal.setIsSus(
                    new BooleanType(isSus)
                );
            }

            // fluxoClientela -> Extension (The client flow expected for the
            // Organization).
            String clientFlow
                = extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("clientFlow"),
                    0
                );
            if (clientFlow != null) {
                retVal.setClientFlow(
                    new CodeType(clientFlow)
                        .setSystem(Utils.namingSystems.get("clientFlow"))
                );
            }
            
            // servicoespecializados -> Extension (specializedServices).
            XPathExpression expr
                = xpath.compile(Utils.xpathExpressions.get("specializedService"));
            Object result = expr.evaluate(document, XPathConstants.NODESET);
            NodeList nodeList = (NodeList) result;
            
            // For debugging purposes.
            // Uncomment to see the nodes extracted from the XML received from the
            // SOAP Webservice.
//            printNode(nodeList, 0, "");
            
            List<OrganizationCnes.SpecializedService> specializedServices
                = new ArrayList<>();
            fillInResourceInstanceWithSpecializedServices(nodeList, "",
                specializedServices);
            retVal.setSpecializedServices(specializedServices);

        } catch (URISyntaxException | IOException | InterruptedException
            | ParserConfigurationException | SAXException
            | XPathExpressionException ex) {
            Logger.getLogger(OrganizationCnesResourceProvider.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OrganizationCnesResourceProvider.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return retVal;
    }

    // Extract a single value from the XML document based on an XPath expression.
    private String extractSingleValueFromXml(Document document, XPath xpath,
        String xpathExpression, int index) throws XPathExpressionException {
        
        XPathExpression expr = xpath.compile(xpathExpression);
        Object result = expr.evaluate(document, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        if (nodes == null || nodes.item(index) == null) {
            return null;
        }
        return nodes.item(index).getNodeValue();
    }
    
    // Loop through the XML content and fill in the resource instance with
    // Specialized Services.
    private void fillInResourceInstanceWithSpecializedServices(
        NodeList nodeList,
        String path,
        List<OrganizationCnes.SpecializedService> specializedServices)
        throws UnsupportedEncodingException {

        // Nothing to do here.
        if (nodeList == null || nodeList.getLength() == 0) {
            return;
        }
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            path = path + "/" + node.getNodeName();
            if (node.getNodeType() == Node.TEXT_NODE) {
                if (
                    path.endsWith(
                        Utils.xpathExpressionSuffixes.get(
                            "specializedServiceCode"
                        )
                    )
                ) {
                    // Specialized service.
                    // For the system and code values, create a new extension
                    // instance.
                    specializedServices.add(
                        new OrganizationCnes.SpecializedService()
                            .setSpecializedService(
                                new Coding()
                                    .setSystem(
                                        Utils.namingSystems.get(
                                            "specializedServiceType"
                                        )
                                    )
                                    .setCode(node.getNodeValue())
                            )
                    );
                } else if (
                    path.endsWith(
                        Utils.xpathExpressionSuffixes.get(
                            "specializedServiceDescription"
                        )
                    )
                ) {
                    // Specialized service.
                    // For the display, use the last created extension instance
                    // of Specialized Service.
                    OrganizationCnes.SpecializedService specializedService
                        = getLastSpecializedService(specializedServices);
                    specializedService.getSpecializedService()
                        .setDisplay(node.getNodeValue());
                } else if (
                    path.endsWith(
                        Utils.xpathExpressionSuffixes.get(
                            "specializedServiceClassificationCode"
                        )
                    )
                ) {
                    // Specialized service classification.
                    // For the system and code values, retrieve the last created
                    // extension instance of the Specialized service.
                    // Retrieve the list of Classifications, adding a new one
                    // afterward.
                    List<OrganizationCnes.SpecializedService.SpecializedServiceClassification>
                        specializedServiceClassifications
                        = getListOfSpecializedServiceClassifications(
                            specializedServices);
                    specializedServiceClassifications.add(
                        new OrganizationCnes.SpecializedService.SpecializedServiceClassification()
                            .setSpecializedServiceClassification(
                                new Coding()
                                    .setSystem(
                                        Utils.namingSystems.get(
                                            "specializedServiceClassification"
                                        )
                                    )
                                    .setCode(node.getNodeValue())
                            )
                    );
                } else if (
                    path.endsWith(
                        Utils.xpathExpressionSuffixes.get(
                            "specializedServiceClassificationDescription"
                        )
                    )
                ) {
                    // Specialized service classification.
                    // For the display value, retrieve the last created
                    // extension instance of the Specialized service
                    // classification.
                    OrganizationCnes.SpecializedService.SpecializedServiceClassification
                        specializedServiceClassification
                            = getLastSpecializedServiceClassification(
                                specializedServices
                            );
                    specializedServiceClassification.getSpecializedServiceClassification()
                        .setDisplay(node.getNodeValue());
                } else if (
                    path.endsWith(
                        Utils.xpathExpressionSuffixes.get(
                            "specializedServiceClassificationCharacteristicCode"
                        )
                    )
                ) {
                    // Specialized service classification characteristic.
                    // For the characteristic code, retrieve the last created
                    // extension instance of the Specialized service
                    // classification.
                    OrganizationCnes.SpecializedService.SpecializedServiceClassification
                        specializedServiceClassification
                            = getLastSpecializedServiceClassification(
                                specializedServices
                            );
                    specializedServiceClassification.getSpecializedServiceClassificationCharacteristic()
                        .setSystem(
                            Utils.namingSystems.get(
                                "specializedServiceClassificationCharacteristic"
                            )
                        )
                        .setValue(node.getNodeValue());
                } else if (
                    path.endsWith(
                        Utils.xpathExpressionSuffixes.get(
                            "specializedServiceClassificationCharacteristicCnes"
                        )
                    )
                ) {
                    // Specialized service classification characteristic.
                    // For the CNES code, retrieve the last created
                    // extension instance of the Specialized service
                    // classification.
                    OrganizationCnes.SpecializedService.SpecializedServiceClassification
                        specializedServiceClassification
                            = getLastSpecializedServiceClassification(
                                specializedServices
                            );
                    specializedServiceClassification.getSpecializedServiceClassificationCnes()
                        .setSystem("urn:oid:" + Utils.oids.get("cnes"))
                        .setCode(node.getNodeValue())
                        .setDisplay(
                            new String(
                                "Número no CNES".getBytes("ISO-8859-1"),
                                "UTF-8"
                            )
                        );
                }
            }
            fillInResourceInstanceWithSpecializedServices(
                node.getChildNodes(),
                path,
                specializedServices);
        }
    }
    
    // Get the last Specialized Service from the list.
    private OrganizationCnes.SpecializedService getLastSpecializedService
        (List<OrganizationCnes.SpecializedService> specializedServices) {
            
        return specializedServices.get(specializedServices.size() - 1);
    }
        
    // Get the list of Specialized Service Classifications from the last
    // Specialized Service.
    private List<OrganizationCnes.SpecializedService.SpecializedServiceClassification>
        getListOfSpecializedServiceClassifications(
            List<OrganizationCnes.SpecializedService> specializedServices) {
            
        OrganizationCnes.SpecializedService specializedService
            = getLastSpecializedService(specializedServices);
            
        return specializedService.getSpecializedServiceClassifications();
    }

    // Get the last Specialized Service Classification from the list.
    private OrganizationCnes.SpecializedService.SpecializedServiceClassification
        getLastSpecializedServiceClassification(
            List<OrganizationCnes.SpecializedService> specializedServices) {
            
        List<OrganizationCnes.SpecializedService.SpecializedServiceClassification>
            specializedServiceClassifications
                = getListOfSpecializedServiceClassifications(
                    specializedServices
                );
            
        return specializedServiceClassifications.get(
            specializedServiceClassifications.size() - 1
        );
    }
        
    // For debugging purposes.
    // Uncomment to see the nodes extracted from the XML received from the
    // SOAP Webservice.
//    private static void printNode(NodeList nodeList, int level, String path) {
//        
//        level++;
//        
//        if (nodeList != null && nodeList.getLength() > 0) {
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                path = path + "/" + node.getNodeName();
//                if (node.getNodeType() == Node.TEXT_NODE) {
//                    System.out.println(path + ": " + node.getNodeType());
//                }
//                printNode(node.getChildNodes(), level, path);
//            }
//        }
//    }
}
