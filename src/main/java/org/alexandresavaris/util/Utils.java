package org.alexandresavaris.util;

// Utility methods and constants.

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // Map of xmlNamespaces for parsing XML content.
    public static final Map<String, String> xmlNamespaces = new HashMap<>();
    
    static{
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
    }
}
