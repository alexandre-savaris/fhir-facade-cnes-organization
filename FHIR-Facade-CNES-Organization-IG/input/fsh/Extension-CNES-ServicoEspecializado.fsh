Extension: ServicoEspecializado
Id: extension-cnes-servico-especializado
Title: "Serviço Especializado ofertado pelo Estabelecimento de Saúde"
Description: "Código, descrição e classificações do Serviço Especializado ofertado pelo Estabelecimento de Saúde."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicoEspecializado"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Caracterizar um Serviço Especializado ofertado por um Estabelecimento de Saúde por meio de um código, uma descrição e uma ou mais classificações."
* ^abstract = true
* id ..0
* extension contains
    EspecificacaoServicoEspecializado named especificacaoServicoEspecializado 1..1 and
    ClassificacoesServicoEspecializado named classificacoesServicoEspecializado 1..*
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicoEspecializado" (exactly)
