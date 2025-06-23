Extension: CnesClassificacaoServicoEspecializado
Id: extension-cnes-cnes-classificacao-servico-especializado
Title: "CNES da Classificação do Serviço Especializado ofertado pelo Estabelecimento de Saúde"
Description: "Número no CNES da Classificação referente ao Serviço Especializado ofertado pelo Estabelecimento de Saúde."
Context: Organization.extension.extension
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CnesClassificacaoServicoEspecializado"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Vincular a Classificação de um Serviço Especializado ofertado por um Estabelecimento de Saúde a outro Estabelecimento de Saúde por meio de um número no CNES."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CnesClassificacaoServicoEspecializado" (exactly)
* value[x] only Coding
* valueCoding 1..1
* valueCoding.id 0..0
* valueCoding.extension 0..0
* valueCoding.system 1..1
* valueCoding.system = "https://terminologia.saude.gov.br/fhir/NamingSystem/cnes"
* valueCoding.version 0..0
* valueCoding.code 1..1
* valueCoding.display 0..1
* valueCoding.userSelected 0..0
