Extension: NomeDiretor
Id: extension-cnes-nome-diretor
Title: "Nome do Diretor do Estabelecimento de Saúde"
Description: "Nome do Diretor responsável pelo Estabelecimento de Saúde."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/NomeDiretor"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Identificar o Diretor responsável pelo Estabelecimento de Saúde por meio do seu nome."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/NomeDiretor" (exactly)
* value[x] only HumanName
* valueHumanName 1..1
* valueHumanName.use 0..0
* valueHumanName.text 1..1
* valueHumanName.family 0..0
* valueHumanName.given 0..0
* valueHumanName.prefix 0..0
* valueHumanName.suffix 0..0
* valueHumanName.period 0..0
