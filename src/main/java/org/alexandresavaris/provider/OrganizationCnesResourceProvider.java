package org.alexandresavaris.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.io.IOException;
import java.io.StringReader;
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
    @Read()
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
                // Filter by CNPJ. TODO: it's not working.
                snippetFilter = MessageFormat.format(
                    this.codeSnippetForFilteringByCnpj, theId.getIdPart()
                );
            }
            this.contentOfSoapEnvelope = MessageFormat.format(
                this.contentOfSoapEnvelope, username, password, snippetFilter
            );
            
            // Access the SOAP webservice.
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(this.endpointEstabelecimentoSaudeService))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(
                    this.contentOfSoapEnvelope))
                .header("Content-Type", "text/xml")
                .header("charset", "UTF-8")
                .build();
            HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
            
            // TODO: remove.
            System.out.println(
                "----------------------------------------------------------");
            System.out.println(response.statusCode());
            System.out.println(response.body());
            System.out.println(
                "----------------------------------------------------------");
            
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
            retVal.setId(IdType.newRandomUuid());

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
            retVal.addIdentifier()
                .setSystem(Utils.namingSystems.get("cnes"))
                .setValue(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("cnes"),
                        0
                    )
                );

            // CodigoUnidade -> Identifier: Unity code.
            retVal.addIdentifier()
                .setSystem(Utils.namingSystems.get("unityCode"))
                .setValue(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("unityCode"),
                        0
                    )
                );

            // numeroCNPJ -> Identifier: CNPJ.
            retVal.addIdentifier()
                .setSystem(Utils.namingSystems.get("cnpj"))
                .setValue(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("cnpj"),
                        0
                    )
                );

            // nomeFantasia -> name.
            retVal.setName(
                extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("name"),
                    0
                )
            );

            // nomeEmpresarial -> alias.
            retVal.addAlias(
                extractSingleValueFromXml(document, xpath,
                    Utils.xpathExpressions.get("alias"),
                    0
                )
            );
            
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
            retVal.addAddress(
                new Address()
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
                    .setCountry("BRA")
            );
            // Extension - city code.
            retVal.setCityCodeIbge(
                new CodeType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("cityCodeIbge"),
                        0
                    )
                )
            );
            // Extension - state code.
            retVal.setStateCodeIbge(
                new CodeType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("stateCodeIbge"),
                        0
                    )
                )
            );

            // dataAtualizacao -> Extension (update date).
            retVal.setUpdateDate(
                new DateType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("updateDate"),
                        0
                    )
                )
            );

            // numeroCPF -> Extension (Director's CPF).
            retVal.setDirectorCpf(
                new CodeType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("directorCpf"),
                        0
                    )
                )
            );

            // Nome -> Extension (Director's name).
            retVal.setDirectorName(
                new HumanName().setText(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("directorName"),
                        0
                    )
                )
            );

            // tipoUnidade -> type.
            retVal.addType(
                new CodeableConcept(
                    new Coding()
                        .setSystem(Utils.valueSets.get("type"))
                        .setCode(
                            extractSingleValueFromXml(document, xpath,
                                Utils.xpathExpressions.get("unityType"),
                                0
                            )
                        )
                        .setDisplay(
                            extractSingleValueFromXml(document, xpath,
                                Utils.xpathExpressions.get("unityDescription"),
                                0
                            )
                        )
                )
            );
                
            // Telefone -> contact
            String phoneTemplate = "{0} {1}";
            String phone = java.text.MessageFormat.format(
                phoneTemplate,
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("phoneAreaCode"),
                        0
                    ),
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("phoneNumber"),
                        0
                    )
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

            // Email -> contact
            retVal.addContact()
                .addTelecom(
                    new ContactPoint()
                        .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                        .setValue(
                            extractSingleValueFromXml(document, xpath,
                                Utils.xpathExpressions.get("email"),
                                0
                            )
                        )
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
            
            // Localizacao -> Extension (geolocation).
            OrganizationCnes.Geolocation geolocation
                = new OrganizationCnes.Geolocation();
            geolocation.setLatitude(
                new DecimalType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("latitude"),
                        0
                    )
                )
            );
            geolocation.setLongitude(
                new DecimalType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("longitude"),
                        0
                    )
                )
            );
            retVal.setGeolocation(geolocation);

            // perteceSistemaSUS -> Extension (Is the Organization part of SUS?).
            retVal.setIsSus(
                new BooleanType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("isSus"),
                        0
                    )
                )
            );

            // fluxoClientela -> Extension (The client flow expected for the
            // Organization).
            retVal.setClientFlow(
                new CodeType(
                    extractSingleValueFromXml(document, xpath,
                        Utils.xpathExpressions.get("clientFlow"),
                        0
                    )
                ).setSystem(Utils.namingSystems.get("clientFlow"))
            );
            
            // servicoespecializados -> Extension (specializedServices).
            XPathExpression expr
                = xpath.compile(Utils.xpathExpressions.get("specializedService"));
            Object result = expr.evaluate(document, XPathConstants.NODESET);
            NodeList nodeList = (NodeList) result;
            // TODO: remove.
            printNode(nodeList, 0, "");
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
        
        return nodes.item(index).getNodeValue();
    }
    
    // ///////////////////
    // Test for traversal.
    // ///////////////////
    private static void printNode(NodeList nodeList, int level, String path) {
        
        level++;
        
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                //if (node.getNodeType() == Node.ELEMENT_NODE) {
                    path = path + "/" + node.getNodeName();
//                    String result = String.format(
//                        "%" + level * 5 + "s : [%s]%n", node.getNodeName(), level);
//                    String result = String.format(
//                        "%" + level * 5 + "s : [%s]%n", path, level);
//                    System.out.print(result);
                    if (node.getNodeType() == Node.TEXT_NODE) {
                        System.out.println(path + ": " + node.getNodeType());
                    }
                    printNode(node.getChildNodes(), level, path);
                //}
            }
        }
    }
    // ///////////////////
    

    // Loop through the XML content and fill in the resource instance with
    // Specialized Services.
    private void fillInResourceInstanceWithSpecializedServices(
        NodeList nodeList,
        String path,
        List<OrganizationCnes.SpecializedService> specializedServices) {

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
                        .setSystem(Utils.namingSystems.get("cnes"))
                        .setValue(node.getNodeValue());
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
}
