Extension: ServicoEspecializado
Id: extension-cnes-servico-especializado
Title: "Serviço Especializado ofertado pelo Estabelecimento de Saúde"
Description: "Código, descrição e classificações do Serviço Especializado ofertado pelo Estabelecimento de Saúde."
Context: Organization.extension
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicoEspecializado"
* ^version = "1.0.0"
* ^experimental = false
* ^date = "2024-07-14"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Caracterizar um Serviço Especializado ofertado por um Estabelecimento de Saúde por meio de um código, uma descrição e uma ou mais classificações."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicoEspecializado" (exactly)
* value[x] only Coding
* valueCoding.system = "https://terminologia.saude.gov.br/fhir/ValueSet/BRServicoEspecializado"
