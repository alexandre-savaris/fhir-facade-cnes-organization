package org.alexandresavaris.util;

import java.util.HashMap;
import java.util.Map;

// Utility methods and constants.
public class Utils {
    // Map of namespaces for parsing XML content.
    public static final Map<String, String> xmlNamespaces = new HashMap<>();
    // Map of NamingSystems used by the OrganizationCnes instance.
    public static final Map<String, String> namingSystems = new HashMap<>();
    // Map of XPath expressions for extracting XML content.
    public static final Map<String, String> xpathExpressions = new HashMap<>();
    
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

        // Insert NamingSystems.
        namingSystems.put("cnes",
            "http://rnds.saude.gov.br/fhir/r4/NamingSystem/cnes");
        namingSystems.put("unityCode",
            "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/CodigoUnidade");
        namingSystems.put("cnpj",
            "http://rnds.saude.gov.br/fhir/r4/NamingSystem/cnpj");

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
        xpathExpressions.put("cityCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Municipio/ns15:codigoMunicipio/text()");
        xpathExpressions.put("stateCode",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns11:Endereco/ns11:Municipio/ns15:UF/ns16:codigoUF/text()");
        xpathExpressions.put("updateDate",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns27:dataAtualizacao/text()");
        xpathExpressions.put("cpfDirector",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns28:Diretor/ns28:CPF/ns5:numeroCPF/text()");
        xpathExpressions.put("nameDirector",
            "//soap:Envelope/S:Body/est:responseConsultarEstabelecimentoSaude/dad:DadosGeraisEstabelecimentoSaude/ns28:Diretor/ns28:nome/ns29:Nome/text()");

    }
}
