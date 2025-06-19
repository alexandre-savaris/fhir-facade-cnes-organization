Extension: ExtensionFluxoClientela
Id: extension-cnes-fluxo-clientela
Title: "Fluxo de clientela esperado para o Estabelecimento de Saúde"
Description: "Código descritivo do fluxo de clientela esperado para o Estabelecimento de Saúde."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/FluxoClientela"
* ^version = "1.0.0"
* ^experimental = false
* ^date = "2024-07-17"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Caracterizar o fluxo de clientela esperado para um Estabelecimento de Saúde por meio de um código descritivo."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/FluxoClientela" (exactly)
* value[x] only code
* valueCode 1..1
