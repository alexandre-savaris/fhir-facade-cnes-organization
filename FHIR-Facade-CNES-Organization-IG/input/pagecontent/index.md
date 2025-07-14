### FHIR-Facade-CNES-Organization-IG

#### Introdução
Este Guia de Implementação lista e descreve os artefatos componentes da Fachada HL7® FHIR® utilizada para acesso aos dados do Cadastro Nacional de Estabelecimentos de Saúde (CNES), mantidos e disponibilizados pelo Departamento de Informática do Sistema Único de Saúde (DATASUS).  
A perfilização deste projeto é baseada nos Guias de Implementação (Implementation Guides - IGs) [Guia de implementação do Core do Brasil: Release 1 - BR Realm](https://hl7.org.br/fhir/core/) e [Guia de Implementação da RNDS](https://rnds-fhir.saude.gov.br/).

#### Para que serve
Originalmente, os dados do CNES são disponibilizados para acesso por entidades devidamente credenciadas através de *Webservices* SOAP (*Simple Object Access Protocol*). A Fachada descrita por este Guia viabiliza um acesso REST (*Representational State Transfer*) em conformidade às defiições do padrão HL7® FHIR®.

#### Como a Fachada pode ser acessada
Em observância à sua [Declaração de Conformidade](./CapabilityStatement-14a679be-5b12-41b6-85e7-8793f9b4a93e.html), a Fachada disponibiliza os seguintes *endpoints* REST que podem ser acessados via requisição HTTP por clientes devidamente autenticados e autorizados:

1. `GET http://{URL_BASE}/metadata` (para acesso à [Declaração de Conformidade](./CapabilityStatement-14a679be-5b12-41b6-85e7-8793f9b4a93e.html)).  
    1.1. Substituir `{URL_BASE}` pela URL na qual a Fachada foi disponibilizada.
2. `GET http://{URL_BASE}/Organization/{9999999}` (para acesso aos [dados de um Estabelecimento de Saúde](./StructureDefinition-FHIRFacadeCNESOrganization.html) identificado pelo seu número no CNES).  
    2.1. Substituir `{URL_BASE}` pela URL na qual a Fachada foi disponibilizada.  
    2.2. Substituir `{9999999}` pelo número no CNES do Estabelecimento de Saúde cujos dados devem ser recuperados.

Retornos com código `HTTP 200` indicam o sucesso das requisições com o conteúdo desejado sendo disponibilizado no corpo da resposta.  
Outros códigos HTTP de retorno indicam erros ou restrições, e devem ser avaliados caso a caso juntamente com o conteúdo (possivelmente) retornado no corpo da resposta.
