package org.alexandresavaris.fhir.facade.cnes.organization.util;

import java.util.HashMap;
import java.util.Map;

// Utility methods and constants.
public class Utils {
    // Map of namespaces for parsing XML content.
    public static final Map<String, String> xmlNamespaces = new HashMap<>();
    // Map of XPath expressions for extracting XML content.
    public static final Map<String, String> xpathExpressions = new HashMap<>();
    // Map of XPath expression suffixes for extracting XML content.
    public static final Map<String, String> xpathExpressionSuffixes
        = new HashMap<>();
    // Map of NamingSystems used in the OrganizationCnes instance.
    public static final Map<String, String> namingSystems = new HashMap<>();
    // Map of ValueSets used in the OrganizationCnes instance.
    public static final Map<String, String> valueSets = new HashMap<>();
    // Map of OIDs used in the OrganizationCnes instance.
    public static final Map<String, String> oids = new HashMap<>();
    // Map of Extensions used in the OrganizationCnes instance.
    public static final Map<String, String> extensions = new HashMap<>();
    // Map of Unique IDs used in the OrganizationCnes instance.
    public static final Map<String, String> uniqueIds = new HashMap<>();

    static {

        // Insert namespaces.
        xmlNamespaces.put("env",
            "http://www.w3.org/2003/05/soap-envelope");
        xmlNamespaces.put("soap",
            "http://www.w3.org/2003/05/soap-envelope");
        xmlNamespaces.put("S",
            "http://www.w3.org/2003/05/soap-envelope");
        xmlNamespaces.put("est",
            "http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice");
        xmlNamespaces.put("dad",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes");
        xmlNamespaces.put("ns0",
            "http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice");
        xmlNamespaces.put("ns2",
            "http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude");
        xmlNamespaces.put("ns3",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes");
        xmlNamespaces.put("ns4",
            "http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj");
        xmlNamespaces.put("ns5",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes");
        xmlNamespaces.put("ns6",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade");
        xmlNamespaces.put("ns7",
            "http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico");
        xmlNamespaces.put("ns8",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco");
        xmlNamespaces.put("ns9",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/tipologradouro");
        xmlNamespaces.put("ns10",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro");
        xmlNamespaces.put("ns11",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep");
        xmlNamespaces.put("ns12",
            "http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio");
        xmlNamespaces.put("ns13",
            "http://servicos.saude.gov.br/schema/corporativo/v1r1/uf");
        xmlNamespaces.put("ns14",
            "http://servicos.saude.gov.br/schema/corporativo/v1r2/pais");
        xmlNamespaces.put("ns15",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/diretor");
        xmlNamespaces.put("ns16",
            "http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf");
        xmlNamespaces.put("ns17",
            "http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto");
        xmlNamespaces.put("ns18",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade");
        xmlNamespaces.put("ns19",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/esferaadministrativa");
        xmlNamespaces.put("ns20",
            "http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone");
        xmlNamespaces.put("ns21",
            "http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone");
        xmlNamespaces.put("ns22",
            "http://servicos.saude.gov.br/schema/corporativo/v1r2/email");
        xmlNamespaces.put("ns23",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao");
        xmlNamespaces.put("ns24",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados");
        xmlNamespaces.put("ns25",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado");
        xmlNamespaces.put("ns26",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacoes");
        xmlNamespaces.put("ns27",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/servicoclassificacao");
        xmlNamespaces.put("ns28",
            "http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaprecadastrocnes");
        xmlNamespaces.put("ns29",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/dadosprecadastrocnes");
        xmlNamespaces.put("ns30",
            "http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/naturezajuridica");
        xmlNamespaces.put("ns31",
            "http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/tiponaturezajuridica");
        xmlNamespaces.put("ns32",
            "http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/filtropesquisaestabelecimento.v1r0");
        xmlNamespaces.put("ns33",
            "http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao");
        xmlNamespaces.put("ns34",
            "http://servicos.saude.gov.br/wsdl/mensageria/estabelecimentosaudeservice/v2r0/resultadopesquisaestabelecimento.v1r0");
        xmlNamespaces.put("ns35",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/listatipounidade");
        xmlNamespaces.put("ns37",
            "http://servicos.saude.gov.br/schema/corporativo/v1r2/uf");
        xmlNamespaces.put("ns38",
            "http://servicos.saude.gov.br/schema/corporativo/v1r3/municipio");

        // Insert XPath expressions.
        xpathExpressions.put("cnes",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns3:CodigoCNES/ns3:codigo/text()");
        xpathExpressions.put("unityCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns6:CodigoUnidade/ns6:codigo/text()");
        xpathExpressions.put("cnpj",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns4:CNPJ/ns4:numeroCNPJ/text()");
        xpathExpressions.put("name",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:nomeFantasia/ns7:Nome/text()");
        xpathExpressions.put("alias",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:nomeEmpresarial/ns7:Nome/text()");
        xpathExpressions.put("street",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:nomeLogradouro/text()");
        xpathExpressions.put("number",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:numero/text()");
        xpathExpressions.put("neighborhood",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:Bairro/ns10:descricaoBairro/text()");
        xpathExpressions.put("city",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:Municipio/ns12:nomeMunicipio/text()");
        xpathExpressions.put("state",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:Municipio/ns12:UF/ns13:siglaUF/text()");
        xpathExpressions.put("postalCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:CEP/ns11:numeroCEP/text()");
        xpathExpressions.put("cityCodeIbge",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:Municipio/ns12:codigoMunicipio/text()");
        xpathExpressions.put("stateCodeIbge",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns8:Endereco/ns8:Municipio/ns12:UF/ns13:codigoUF/text()");
        xpathExpressions.put("updateDate",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:dataAtualizacao/text()");
        xpathExpressions.put("directorCpf",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns15:Diretor/ns15:CPF/ns16:numeroCPF/text()");
        xpathExpressions.put("directorName",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns15:Diretor/ns15:nome/ns17:Nome/text()");
        xpathExpressions.put("unityType",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns18:tipoUnidade/ns18:codigo/text()");
        xpathExpressions.put("unityDescription",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns18:tipoUnidade/ns18:descricao/text()");
        xpathExpressions.put("phoneAreaCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Telefone/ns20:DDD/text()");
        xpathExpressions.put("phoneNumber",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Telefone/ns20:numeroTelefone/text()");
        xpathExpressions.put("phoneType",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Telefone/ns20:TipoTelefone/ns21:codigoTipoTelefone/text()");
        xpathExpressions.put("phoneDescription",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Telefone/ns20:TipoTelefone/ns21:descricaoTipoTelefone/text()");
        xpathExpressions.put("email",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Email/ns22:descricaoEmail/text()");
        xpathExpressions.put("emailType",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Email/ns22:tipoEmail/text()");
        xpathExpressions.put("latitude",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Localizacao/ns23:latitude/text()");
        xpathExpressions.put("longitude",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:Localizacao/ns23:longitude/text()");
        xpathExpressions.put("isSus",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:perteceSistemaSUS/text()");
        xpathExpressions.put("clientFlow",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:fluxoClientela/text()");
        xpathExpressions.put("specializedService",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns5:servicoespecializados/ns24:servicoespecializado");
        
        // Insert XPath expression suffixes.
        xpathExpressionSuffixes.put("specializedServiceCode",
            "ns25:codigo/#text");
        xpathExpressionSuffixes.put("specializedServiceDescription",
            "ns25:descricao/#text");
        xpathExpressionSuffixes.put("specializedServiceClassificationCode",
            "ns27:codigo/#text");
        xpathExpressionSuffixes.put("specializedServiceClassificationDescription",
            "ns27:descricao/#text");
        xpathExpressionSuffixes.put(
            "specializedServiceClassificationCharacteristicCode",
            "ns27:codigoCaracteristica/#text"
        );
        xpathExpressionSuffixes.put(
            "specializedServiceClassificationCharacteristicCnes",
            "ns27:cnes/#text"
        );

        // Insert NamingSystems.
        namingSystems.put("unityCode",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/CodigoUnidade");
        namingSystems.put("cnpj",
            "https://saude.gov.br/fhir/sid/cnpj");
        namingSystems.put("phoneType",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/TipoTelefone");
        namingSystems.put("emailType",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/TipoEmail");
        namingSystems.put("clientFlow",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/FluxoClientela");
        namingSystems.put("specializedServiceSpecification",
            "https://terminologia.saude.gov.br/fhir/ValueSet/BRServicoEspecializado");
        namingSystems.put("specializedServiceClassification",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/ClassificacaoServicoEspecializado");
        namingSystems.put("specializedServiceClassificationCharacteristic",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/CaracteristicaClassificacaoServicoEspecializado");
        namingSystems.put("cpf",
            "https://terminologia.saude.gov.br/fhir/NamingSystem/cpf");
        namingSystems.put("cnes",
            "https://terminologia.saude.gov.br/fhir/NamingSystem/cnes");

        // Insert ValueSets.
        valueSets.put("type",
            "https://terminologia.saude.gov.br/fhir/ValueSet/BRTipoEstabelecimentoSaude");

        // Insert OIDs.
        // https://www.hl7.org/oid/index.cfm
        oids.put("ibgeCode", "2.16.840.1.113883.4.707");

        // Insert Extensions.
        extensions.put(
            "geolocation", "http://hl7.org/fhir/StructureDefinition/geolocation"
        );
        extensions.put("latitude", "latitude");
        extensions.put("longitude", "longitude");
        extensions.put(
            "cityCodeIbge",
            "https://alexandresavaris.org/fhir/r4/Extension/cnes/CodigoMunicipioIbge"
        );
        extensions.put(
            "stateCodeIbge",
            "https://alexandresavaris.org/fhir/r4/Extension/cnes/CodigoUfIbge"
        );

        // Insert Unique IDs.
//        uniqueIds.put("cnes", "https://saude.gov.br/fhir/sid/cnes");
        uniqueIds.put("cnpj", "https://saude.gov.br/fhir/sid/cnpj");
    }
}
