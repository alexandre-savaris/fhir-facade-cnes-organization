Extension: ServicosEspecializados
Id: extension-cnes-servicos-especializados
Title: "Serviços Especializados ofertados pelo Estabelecimento de Saúde"
Description: "Relação de Serviços Especializados ofertados pelo Estabelecimento de Saúde."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicosEspecializados"
* ^version = "1.0.0"
* ^experimental = false
* ^date = "2024-08-03"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Relacionar os Serviços Especializados ofertados por um Estabelecimento de Saúde."
* ^abstract = true
* id ..0
* extension contains
    ServicoEspecializado 0..* and
    ClassificacoesServicoEspecializado 0..*
* extension[ServicoEspecializado].id ..0
* extension[ServicoEspecializado].value[x] only Coding
* extension[ClassificacoesServicoEspecializado].id ..0
* extension[ClassificacoesServicoEspecializado].value[x] ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicosEspecializados" (exactly)