package ca.uhn.example.provider;

import ca.uhn.example.model.MyOrganization;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.alexandresavaris.model.CnesOrganization;
import org.hl7.fhir.r4.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
    public CnesOrganization getResourceById(@IdParam IdType theId) {
        // The resource instance to be returned.
        CnesOrganization retVal = null;
        
        try {

            // Replace the placeholder with the resource ID.
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

            // TODO: parameterize URL.
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
            
            // TODO: remove.
            System.out.println("----------------------------------------------------------");
            System.out.println(response.statusCode());
            System.out.println(response.body());
            System.out.println("----------------------------------------------------------");
            
            if (response.statusCode() != 200) {
                throw new ResourceNotFoundException(theId);
            }

            retVal = new CnesOrganization();
            retVal.setId(IdType.newRandomUuid());
            
            DocumentBuilder builder
                = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document
                = builder.parse(new InputSource(new StringReader(response.body())));
            
            // Fill in the resource content.
            Node node;

            // Identifier: CNES.
            node = document.getElementsByTagName("ns2:codigo").item(0)
                .getChildNodes().item(0);
            retVal.addIdentifier()
                .setSystem("http://rnds.saude.gov.br/fhir/r4/NamingSystem/cnes")
                .setValue(node.getNodeValue());

            // Identifier: CNPJ.
            node = document.getElementsByTagName("ns6:numeroCNPJ").item(0)
                .getChildNodes().item(0);
            retVal.addIdentifier()
                .setSystem("http://rnds.saude.gov.br/fhir/r4/NamingSystem/cnpj")
                .setValue(node.getNodeValue());
            
            // Nome fantasia.
            node = document.getElementsByTagName("ns7:Nome").item(0)
                .getChildNodes().item(0);
            retVal.setName(node.getNodeValue());

/*            
            
            retVal.addContact().getAddress().addLine("123 Fake Street").setCity("Toronto");
            retVal.addContact().getTelecomFirstRep().setUse(ContactPointUse.WORK).setValue("1-888-123-4567");
            
            // Populate the first, primitive extension
            retVal.setBillingCode(new CodeType("00102-1"));
            
            // The second extension is repeatable and takes a block type
            MyOrganization.EmergencyContact contact = new MyOrganization.EmergencyContact();
            contact.setActive(new BooleanType(true));
            contact.setContact(new ContactPoint());
            retVal.getEmergencyContact().add(contact);
  */

        } catch (URISyntaxException | IOException | InterruptedException
            | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(OrganizationResourceProvider.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return retVal;
    }
}


/*
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope
	xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
	<S:Body
		xmlns:S="http://www.w3.org/2003/05/soap-envelope">
		<est:responseConsultarEstabelecimentoSaude
			xmlns:est="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice">
			<dad:DadosGeraisEstabelecimentoSaude
				xmlns:dad="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes">
				<ns26:CodigoUnidade
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns26:codigo>4205402691841</ns26:codigo>
				</ns26:CodigoUnidade>
				<ns27:nomeEmpresarial
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns7:Nome>SECRETARIA DE ESTADO DA SAUDE</ns7:Nome>
				</ns27:nomeEmpresarial>
				<ns11:Endereco
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns11:nomeLogradouro>IRMA BENWARDA</ns11:nomeLogradouro>
					<ns11:numero>297</ns11:numero>
					<ns11:Bairro>
						<ns13:descricaoBairro>CENTRO</ns13:descricaoBairro>
					</ns11:Bairro>
					<ns11:CEP>
						<ns14:numeroCEP>88015270</ns14:numeroCEP>
					</ns11:CEP>
					<ns11:Municipio>
						<ns15:codigoMunicipio>420540</ns15:codigoMunicipio>
						<ns15:nomeMunicipio>FLORIANOPOLIS</ns15:nomeMunicipio>
						<ns15:UF>
							<ns16:codigoUF>42</ns16:codigoUF>
							<ns16:siglaUF>SC</ns16:siglaUF>
						</ns15:UF>
					</ns11:Municipio>
				</ns11:Endereco>
				<ns27:dataAtualizacao
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">2024-06-20
				</ns27:dataAtualizacao>
				<ns28:Diretor
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns28:CPF>
						<ns5:numeroCPF>03267927919</ns5:numeroCPF>
					</ns28:CPF>
					<ns28:nome>
						<ns29:Nome>MICHEL MAXIMIANO FARACO</ns29:Nome>
					</ns28:nome>
				</ns28:Diretor>
				<ns30:tipoUnidade
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns30:codigo>05</ns30:codigo>
					<ns30:descricao>HOSPITAL GERAL</ns30:descricao>
				</ns30:tipoUnidade>
				<ns27:Telefone
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns18:TipoTelefone>
						<ns19:codigoTipoTelefone>2</ns19:codigoTipoTelefone>
						<ns19:descricaoTipoTelefone>COMERCIAL</ns19:descricaoTipoTelefone>
					</ns18:TipoTelefone>
					<ns18:DDD>48</ns18:DDD>
					<ns18:numeroTelefone>36643600</ns18:numeroTelefone>
				</ns27:Telefone>
				<ns27:Email
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns20:descricaoEmail>hcr@saude.sc.gov.br</ns20:descricaoEmail>
					<ns20:tipoEmail>P</ns20:tipoEmail>
				</ns27:Email>
				<ns27:Localizacao
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns23:longitude>-48.55086</ns23:longitude>
					<ns23:latitude>-27.58980</ns23:latitude>
					<ns23:geoJson>{"type":"Feature","geometry":{"type":"Point","coordinates":["-48.55086","-27.58980"]}}</ns23:geoJson>
				</ns27:Localizacao>
				<ns27:perteceSistemaSUS
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">true
				</ns27:perteceSistemaSUS>
				<ns27:servicoespecializados
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">
					<ns35:servicoespecializado>
						<ns32:codigo>105</ns32:codigo>
						<ns32:descricao>SERVICO DE ATENCAO EM NEUROLOGIA / NEUROCIRURGIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>NEUROCIRURGIA DO TRAUMA E ANOMALIAS DO DESENVOLVIMENTO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>COLUNA E NERVOS PERIFERICOS</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>TUMORES DO SISTEMA NERVOSO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>NEUROCIRURGIA VASCULAR</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>TRATAMENTO NEUROCIRURGICO DA DOR FUNCIONAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>006</ns33:codigo>
								<ns33:descricao>INVESTIGACAO E CIRURGIA DE EPILEPSIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>007</ns33:codigo>
								<ns33:descricao>TRATAMENTO ENDOVASCULAR</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>008</ns33:codigo>
								<ns33:descricao>NEUROCIRURGIA FUNCIONAL ESTEREOTAXICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>009</ns33:codigo>
								<ns33:descricao>POLISSONOGRAFIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>110</ns32:codigo>
						<ns32:descricao>SERVICO DE ATENCAO A SAUDE REPRODUTIVA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>LAQUEADURA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>114</ns32:codigo>
						<ns32:descricao>SERVICO DE ATENCAO EM SAUDE BUCAL</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>006</ns33:codigo>
								<ns33:descricao>CIRURGIA BUCOMAXILOFACIAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>117</ns32:codigo>
						<ns32:descricao>SERVICO DE CIRURGIA REPARADORA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>TRATAMENTO EM QUEIMADOS</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>118</ns32:codigo>
						<ns32:descricao>SERVICO DE CIRURGIA TORACICA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>CIRURGIA TORACICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>120</ns32:codigo>
						<ns32:descricao>SERVICO DE DIAGNOSTICO POR ANATOMIA PATOLOGICA EOU CITOPATO</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>EXAMES ANATOMOPATOLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2691582</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>EXAMES CITOPATOLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>3191117</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>121</ns32:codigo>
						<ns32:descricao>SERVICO DE DIAGNOSTICO POR IMAGEM</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>012</ns33:codigo>
								<ns33:descricao>MAMOGRAFIA</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>0019283</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>RADIOLOGIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>ULTRASONOGRAFIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>TOMOGRAFIA COMPUTADORIZADA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>RESSONANCIA MAGNETICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>006</ns33:codigo>
								<ns33:descricao>RADIOLOGIA INTERVENCIONISTA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>122</ns32:codigo>
						<ns32:descricao>SERVICO DE DIAGNOSTICO POR METODOS GRAFICOS DINAMICOS</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>EXAME  ELETROCARDIOGRAFICO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>EXAME ELETROENCEFALOGRAFICO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>125</ns32:codigo>
						<ns32:descricao>SERVICO DE FARMACIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>006</ns33:codigo>
								<ns33:descricao>FARMACIA HOSPITALAR</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>126</ns32:codigo>
						<ns32:descricao>SERVICO DE FISIOTERAPIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>ASSISTENCIA FISIOTERAPEUTICA EM ALTERACOES ONCOLOGICAS</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>ASSISTENCIA FISIOTERAPEUTICA CARDIOVASCULARES E PNEUMOFUNCI</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>ASSISTENCIA FISIOTERAPEUTICA NAS DISFUNCOES MUSCULO ESQUELET</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>007</ns33:codigo>
								<ns33:descricao>ASSISTENCIA FISIOTERAPEUTICA NAS ALTERACOES EM NEUROLOGIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>128</ns32:codigo>
						<ns32:descricao>SERVICO DE HEMOTERAPIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>DIAGNOSTICO EM HEMOTERAPIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>PROCEDIMENTOS ESPECIAIS EM HEMOTERAPIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>MEDICINA TRANSFUSIONAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>130</ns32:codigo>
						<ns32:descricao>ATENCAO A DOENCA RENAL CRONICA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>TRATAMENTO DIALITICO-PERITONEAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>TRATAMENTO DIALITICO-HEMODIALISE</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>CONFECCAO INTERVENCAO DE ACESSOS PARA DIALISE</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>131</ns32:codigo>
						<ns32:descricao>SERVICO DE OFTALMOLOGIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>DIAGNOSTICO EM OFTALMOLOGIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>TRATAMENTO CLINICO DO APARELHO DA VISAO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>TRATAMENTO CIRURGICO DO APARELHO DA VISAO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>132</ns32:codigo>
						<ns32:descricao>SERVICO DE ONCOLOGIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>ONCOLOGIA CLINICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>ONCOLOGIA CIRURGICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>133</ns32:codigo>
						<ns32:descricao>SERVICO DE PNEUMOLOGIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>DIAGNOSTICO EM PNEUMOLOGIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>TRATAMENTO DE DOENCAS DAS VIAS AEREAS INFERIORES</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>134</ns32:codigo>
						<ns32:descricao>SERVICO DE PRATICAS INTEGRATIVAS E COMPLEMENTARES</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>ACUPUNTURA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>135</ns32:codigo>
						<ns32:descricao>REABILITACAO</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>011</ns33:codigo>
								<ns33:descricao>ATENCAO FISIOTERAPEUTICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>010</ns33:codigo>
								<ns33:descricao>ATENCAO FONOAUDIOLOGICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>REABILITACAO VISUAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>REABILITACAO FISICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>136</ns32:codigo>
						<ns32:descricao>SERVICO DE SUPORTE NUTRICIONAL</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>ENTERAL  PARENTERAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>140</ns32:codigo>
						<ns32:descricao>SERVICO DE URGENCIA E EMERGENCIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>ESTABILIZACAO DE PACIENTE CRITICO/GRAVE EM SALA DE ESTABILIZ</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>ATENDIMENTO AO PACIENTE COM ACIDENTE VASCULAR CEREBRAL (AVC)</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>006</ns33:codigo>
								<ns33:descricao>PRONTO ATENDIMENTO CLINICO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>010</ns33:codigo>
								<ns33:descricao>PRONTO ATENDIMENTO OFTALMOLOGICO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>014</ns33:codigo>
								<ns33:descricao>PRONTO SOCORRO CARDIOVASCULAR</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>015</ns33:codigo>
								<ns33:descricao>PRONTO SOCORRO NEUROLOGIA/NEUROCIRURGIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>016</ns33:codigo>
								<ns33:descricao>PRONTO SOCORRO TRAUMATO ORTOPEDICO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>019</ns33:codigo>
								<ns33:descricao>PRONTO SOCORRO GERAL/CLINICO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>141</ns32:codigo>
						<ns32:descricao>SERVICO DE VIGILANCIA EM SAUDE</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>VIGILANCIA EPIDEMIOLOGICA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>142</ns32:codigo>
						<ns32:descricao>SERVICO DE ENDOSCOPIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>DO APARELHO DIGESTIVO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>DO APARELHO RESPIRATORIO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>DO APARELHO URINARIO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>145</ns32:codigo>
						<ns32:descricao>SERVICO DE DIAGNOSTICO DE LABORATORIO CLINICO</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>EXAMES BIOQUIMICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>EXAMES HEMATOLOGICOS E HEMOSTASIA</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>EXAMES SOROLOGICOS E IMUNOLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>004</ns33:codigo>
								<ns33:descricao>EXAMES COPROLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>EXAMES DE UROANALISE</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>006</ns33:codigo>
								<ns33:descricao>EXAMES HORMONAIS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>009</ns33:codigo>
								<ns33:descricao>EXAMES MICROBIOLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>010</ns33:codigo>
								<ns33:descricao>EXAMES EM OUTROS LIQUIDOS BIOLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>011</ns33:codigo>
								<ns33:descricao>EXAMES DE GENETICA</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>012</ns33:codigo>
								<ns33:descricao>EXAMES PARA TRIAGEM NEONATAL</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>013</ns33:codigo>
								<ns33:descricao>EXAMES IMUNOHEMATOLOGICOS</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2511762</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>149</ns32:codigo>
						<ns32:descricao>TRANSPLANTE</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>RIM</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>MEDULA OSSEA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>005</ns33:codigo>
								<ns33:descricao>CORNEA/ESCLERA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>008</ns33:codigo>
								<ns33:descricao>RETIRADA DE ORGAOS</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>011</ns33:codigo>
								<ns33:descricao>OSTEOCONDROLIGAMENTOS</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>014</ns33:codigo>
								<ns33:descricao>ACOMPANHAMENTO DE PACIENTE TRANSPLANTADO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>015</ns33:codigo>
								<ns33:descricao>ACOES PARA DOACAO E CAPTACAO DE ORGAOS E TECIDOS</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>016</ns33:codigo>
								<ns33:descricao>RETIRADA DE GLOBO OCULAR HUMANO PARA TRANSPLANTE</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>150</ns32:codigo>
						<ns32:descricao>CIRURGIA VASCULAR</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>FISTULA ARTERIOVENOSA SEM ENXERTO</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>154</ns32:codigo>
						<ns32:descricao>SERVICO DE BANCO DE TECIDOS</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>BANCO DE TECIDO MUSCULO ESQUELETICO</ns33:descricao>
								<ns33:codigoCaracteristica>2</ns33:codigoCaracteristica>
								<ns33:cnes>2273276</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>155</ns32:codigo>
						<ns32:descricao>SERVICO DE TRAUMATOLOGIA E ORTOPEDIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>SERVICO DE TRAUMATOLOGIA E ORTOPEDIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>SERVICO DE TRAUMATOLOGIA E ORTOPEDIA PEDIATRICA(ATE 21 ANOS)</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>003</ns33:codigo>
								<ns33:descricao>SERVICO DE TRAUMATOLOGIA E ORTOPEDIA DE URGENCIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>169</ns32:codigo>
						<ns32:descricao>ATENCAO EM UROLOGIA</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>002</ns33:codigo>
								<ns33:descricao>LITOTRIPSIA</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>UROLOGIA GERAL</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
					<ns35:servicoespecializado>
						<ns32:codigo>170</ns32:codigo>
						<ns32:descricao>COMISSOES E COMITES</ns32:descricao>
						<ns32:classificacoes>
							<ns34:servicoclassificacao>
								<ns33:codigo>001</ns33:codigo>
								<ns33:descricao>NUCLEO DE SEGURANCA DO PACIENTE</ns33:descricao>
								<ns33:codigoCaracteristica>1</ns33:codigoCaracteristica>
								<ns33:cnes>NAO INFORMADO</ns33:cnes>
							</ns34:servicoclassificacao>
						</ns32:classificacoes>
					</ns35:servicoespecializado>
				</ns27:servicoespecializados>
				<ns27:fluxoClientela
					xmlns:ns29="http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto"
					xmlns:ns25="http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao"
					xmlns:ns26="http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade"
					xmlns:ns27="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes"
					xmlns:ns28="http://servicos.saude.gov.br/schema/cnes/v1r0/diretor"
					xmlns:ns21="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha"
					xmlns:ns22="http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/mensagem"
					xmlns:ns23="http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao"
					xmlns:ns24="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0"
					xmlns:ns20="http://servicos.saude.gov.br/schema/corporativo/v1r2/email"
					xmlns:ns40="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude"
					xmlns:ns16="http://servicos.saude.gov.br/schema/corporativo/v1r1/uf"
					xmlns:ns17="http://servicos.saude.gov.br/schema/corporativo/v1r2/pais"
					xmlns:ns14="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep"
					xmlns:ns15="http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio"
					xmlns:ns38="http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio"
					xmlns:ns39="http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade"
					xmlns:ns36="http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0"
					xmlns:ns18="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone"
					xmlns:ns37="http://servicos.saude.gov.br/schema/corporativo/v1r2/uf"
					xmlns:ns19="http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone"
					xmlns:ns9="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica"
					xmlns:ns34="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes"
					xmlns:ns35="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados"
					xmlns:ns32="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado"
					xmlns:ns33="http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao"
					xmlns:ns5="http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf"
					xmlns:ns12="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro"
					xmlns:ns30="http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade"
					xmlns:ns6="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj"
					xmlns:ns13="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro"
					xmlns:ns31="http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa"
					xmlns:ns7="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico"
					xmlns:ns10="http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica"
					xmlns:ns8="http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes"
					xmlns:ns11="http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco"
					xmlns:ns2="http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes"
					xmlns:ns4="http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice"
					xmlns:ns3="http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes">ATENDIMENTO DE DEMANDA ESPONT�NEA E REFERENCIADA
				</ns27:fluxoClientela>
			</dad:DadosGeraisEstabelecimentoSaude>
		</est:responseConsultarEstabelecimentoSaude>
	</S:Body>
</soap:Envelope>
*/

/*
soap:Envelope
S:Header
work:WorkContext
S:Body
est:responseConsultarEstabelecimentoSaude
dad:DadosGeraisEstabelecimentoSaude
ns26:CodigoUnidade
ns26:codigo
ns27:nomeEmpresarial
ns7:Nome
ns11:Endereco
ns11:nomeLogradouro
ns11:numero
ns11:Bairro
ns13:descricaoBairro
ns11:CEP
ns14:numeroCEP
ns11:Municipio
ns15:codigoMunicipio
ns15:nomeMunicipio
ns15:UF
ns16:codigoUF
ns16:siglaUF
ns27:dataAtualizacao
ns28:Diretor
ns28:CPF
ns5:numeroCPF
ns28:nome
ns29:Nome
ns30:tipoUnidade
ns30:codigo
ns30:descricao
ns27:Telefone
ns18:TipoTelefone
ns19:codigoTipoTelefone
ns19:descricaoTipoTelefone
ns18:DDD
ns18:numeroTelefone
ns27:Email
ns20:descricaoEmail
ns20:tipoEmail
ns27:Localizacao
ns23:longitude
ns23:latitude
ns23:geoJson
ns27:perteceSistemaSUS
ns27:servicoespecializados
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns35:servicoespecializado
ns32:codigo
ns32:descricao
ns32:classificacoes
ns34:servicoclassificacao
ns33:codigo
ns33:descricao
ns33:codigoCaracteristica
ns33:cnes
ns27:fluxoClientela
*/