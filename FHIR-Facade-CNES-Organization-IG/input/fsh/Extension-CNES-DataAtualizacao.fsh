Extension: DataAtualizacao
Id: extension-cnes-data-atualizacao
Title: "Data da atualização dos dados do Estabelecimento de Saúde"
Description: "Data em que os dados do Estabelecimento de Saúde foram atualizados."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/DataAtualizacao"
* ^version = "1.0.0"
* ^experimental = false
* ^date = "2024-07-11"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Registrar a data em que os dados do Estabelecimento de Saúde foram atualizados."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/DataAtualizacao" (exactly)
* value[x] only date
* valueDate 1..1
