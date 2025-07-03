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
        xmlNamespaces.put("soap",
            "http://www.w3.org/2003/05/soap-envelope");
        xmlNamespaces.put("S",
            "http://www.w3.org/2003/05/soap-envelope");
        xmlNamespaces.put("est",
            "http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice");
        xmlNamespaces.put("dad",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes");
        xmlNamespaces.put("ns2",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes");
        xmlNamespaces.put("ns5",
            "http://servicos.saude.gov.br/schema/corporativo/documento/v1r2/cpf");
        xmlNamespaces.put("ns6",
            "http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj");
        xmlNamespaces.put("ns7",
            "http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/nomejuridico");
        xmlNamespaces.put("ns11",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r2/endereco");
        xmlNamespaces.put("ns13",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/bairro");
        xmlNamespaces.put("ns14",
            "http://servicos.saude.gov.br/schema/corporativo/endereco/v1r1/cep");
        xmlNamespaces.put("ns15",
            "http://servicos.saude.gov.br/schema/corporativo/v1r2/municipio");
        xmlNamespaces.put("ns16",
            "http://servicos.saude.gov.br/schema/corporativo/v1r1/uf");
        xmlNamespaces.put("ns18",
            "http://servicos.saude.gov.br/schema/corporativo/telefone/v1r2/telefone");
        xmlNamespaces.put("ns19",
            "http://servicos.saude.gov.br/schema/corporativo/telefone/v1r1/tipotelefone");
        xmlNamespaces.put("ns20",
            "http://servicos.saude.gov.br/schema/corporativo/v1r2/email");
        xmlNamespaces.put("ns23",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/localizacao");
        xmlNamespaces.put("ns26",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/codigounidade");
        xmlNamespaces.put("ns27",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/dadosgeraiscnes");
        xmlNamespaces.put("ns28",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/diretor");
        xmlNamespaces.put("ns29",
            "http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v1r2/nomecompleto");
        xmlNamespaces.put("ns30",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/tipounidade");
        xmlNamespaces.put("ns32",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializado");
        xmlNamespaces.put("ns35",
            "http://servicos.saude.gov.br/schema/cnes/v1r0/servicoespecializados");

        // Insert XPath expressions.
        xpathExpressions.put("cnes",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns2:CodigoCNES/ns2:codigo/text()");
        xpathExpressions.put("unityCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns26:CodigoUnidade/ns26:codigo/text()");
        xpathExpressions.put("cnpj",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns6:CNPJ/ns6:numeroCNPJ/text()");
        xpathExpressions.put("name",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:nomeFantasia/ns7:Nome/text()");
        xpathExpressions.put("alias",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:nomeEmpresarial/ns7:Nome/text()");
        xpathExpressions.put("street",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:nomeLogradouro/text()");
        xpathExpressions.put("number",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:numero/text()");
        xpathExpressions.put("neighborhood",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Bairro/ns13:descricaoBairro/text()");
        xpathExpressions.put("city",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Municipio/ns15:nomeMunicipio/text()");
        xpathExpressions.put("state",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Municipio/ns15:UF/ns16:siglaUF/text()");
        xpathExpressions.put("postalCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:CEP/ns14:numeroCEP/text()");
        xpathExpressions.put("cityCodeIbge",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Municipio/ns15:codigoMunicipio/text()");
        xpathExpressions.put("stateCodeIbge",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Municipio/ns15:UF/ns16:codigoUF/text()");
        xpathExpressions.put("updateDate",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:dataAtualizacao/text()");
        xpathExpressions.put("directorCpf",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns28:Diretor/ns28:CPF/ns5:numeroCPF/text()");
        xpathExpressions.put("directorName",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns28:Diretor/ns28:nome/ns29:Nome/text()");
        xpathExpressions.put("unityType",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns30:tipoUnidade/ns30:codigo/text()");
        xpathExpressions.put("unityDescription",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns30:tipoUnidade/ns30:descricao/text()");
        xpathExpressions.put("phoneAreaCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Telefone/ns18:DDD/text()");
        xpathExpressions.put("phoneNumber",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Telefone/ns18:numeroTelefone/text()");
        xpathExpressions.put("phoneType",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Telefone/ns18:TipoTelefone/ns19:codigoTipoTelefone/text()");
        xpathExpressions.put("phoneDescription",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Telefone/ns18:TipoTelefone/ns19:descricaoTipoTelefone/text()");
        xpathExpressions.put("email",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Email/ns20:descricaoEmail/text()");
        xpathExpressions.put("emailType",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Email/ns20:tipoEmail/text()");
        xpathExpressions.put("latitude",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Localizacao/ns23:latitude/text()");
        xpathExpressions.put("longitude",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:Localizacao/ns23:longitude/text()");
        xpathExpressions.put("isSus",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:perteceSistemaSUS/text()");
        xpathExpressions.put("clientFlow",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:fluxoClientela/text()");
        xpathExpressions.put("specializedService",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:servicoespecializados/ns35:servicoespecializado");
        
        // Insert XPath expression suffixes.
        xpathExpressionSuffixes.put("specializedServiceCode",
            "ns32:codigo/#text");
        xpathExpressionSuffixes.put("specializedServiceDescription",
            "ns32:descricao/#text");
        xpathExpressionSuffixes.put("specializedServiceClassificationCode",
            "ns33:codigo/#text");
        xpathExpressionSuffixes.put("specializedServiceClassificationDescription",
            "ns33:descricao/#text");
        xpathExpressionSuffixes.put(
            "specializedServiceClassificationCharacteristicCode",
            "ns33:codigoCaracteristica/#text"
        );
        xpathExpressionSuffixes.put(
            "specializedServiceClassificationCharacteristicCnes",
            "ns33:cnes/#text"
        );
        
        // Insert NamingSystems.
        namingSystems.put("unityCode",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/CodigoUnidade");
//        namingSystems.put("cnpj",
//            "http://rnds.saude.gov.br/fhir/r4/NamingSystem/cnpj");
        namingSystems.put("phoneType",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/TipoTelefone");
        namingSystems.put("emailType",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/TipoEmail");
        namingSystems.put("clientFlow",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/FluxoClientela");
        namingSystems.put("specializedServiceType",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/TipoServicoEspecializado");
        namingSystems.put("specializedServiceClassification",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/ClassificacaoServicoEspecializado");
        namingSystems.put("specializedServiceClassificationCharacteristic",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/CaracteristicaClassificacaoServicoEspecializado");
        namingSystems.put("cpf",
            "https://terminologia.saude.gov.br/fhir/NamingSystem/cpf");

        // Insert ValueSets.
        valueSets.put("type",
            "https://terminologia.saude.gov.br/fhir/ValueSet/BRTipoEstabelecimentoSaude");

        // Insert OIDs.
        // https://www.hl7.org/oid/index.cfm
//        oids.put("cnes", "2.16.840.1.113883.13.36");
        oids.put("ibgeCode", "2.16.840.1.113883.4.707");
//        oids.put("cpf", "2.16.840.1.113883.13.237");
        
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
        uniqueIds.put("cnes", "https://saude.gov.br/fhir/sid/cnes");
        uniqueIds.put("cnpj", "https://saude.gov.br/fhir/sid/cnpj");
    }
}
