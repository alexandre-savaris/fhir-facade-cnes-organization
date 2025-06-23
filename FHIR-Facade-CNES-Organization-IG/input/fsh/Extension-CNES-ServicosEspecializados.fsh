Extension: ServicosEspecializados
Id: extension-cnes-servicos-especializados
Title: "Serviços Especializados ofertados pelo Estabelecimento de Saúde"
Description: "Relação de Serviços Especializados ofertados pelo Estabelecimento de Saúde."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicosEspecializados"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Relacionar os Serviços Especializados ofertados por um Estabelecimento de Saúde."
* ^abstract = true
* id ..0
* extension contains ServicoEspecializado named servicoEspecializado 1..*
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicosEspecializados" (exactly)
