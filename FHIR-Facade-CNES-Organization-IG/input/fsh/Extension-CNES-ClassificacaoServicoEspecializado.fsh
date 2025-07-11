Extension: ClassificacaoServicoEspecializado
Id: extension-cnes-classificacao-servico-especializado
Title: "Classificação do Serviço especializado ofertado pelo Estabelecimento de Saúde"
Description: "Código, descrição e outras informações referentes à Classificação do Serviço Especializado ofertado pelo Estabelecimento de Saúde."
Context: Organization.extension.extension.extension
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacaoServicoEspecializado"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Caracterizar a Classificação de um Serviço Especializado ofertado por um Estabelecimento de Saúde por meio de um código, uma descrição e outras informações."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacaoServicoEspecializado" (exactly)
* value[x] only Coding
* valueCoding 1..1
* valueCoding.id 0..0
* valueCoding.extension 0..0
* valueCoding.system 1..1
* valueCoding.system = "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/ClassificacaoServicoEspecializado"
* valueCoding.version 0..0
* valueCoding.code 1..1
* valueCoding.display 1..1
* valueCoding.userSelected 0..0
