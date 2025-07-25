Extension: CodigoUfIbge
Id: extension-cnes-codigo-uf-ibge
Title: "Código da UF no IBGE"
Description: "Código da Unidade Federativa (UF) mantido pelo Instituto Brasileiro de Geografia e Estatística (IBGE)."
Context: Organization.address
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CodigoUfIbge"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Identificar uma UF por meio de um código definido e mantido pelo IBGE."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CodigoUfIbge" (exactly)
* value[x] only Coding
* valueCoding 1..1
* valueCoding.id 0..0
* valueCoding.extension 0..0
* valueCoding.system 1..1
* valueCoding.system = "urn:oid:2.16.840.1.113883.4.707"
* valueCoding.version 0..0
* valueCoding.code 1..1
* valueCoding.display 1..1
* valueCoding.userSelected 0..0
