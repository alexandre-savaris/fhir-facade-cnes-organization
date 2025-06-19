Extension: CpfDiretor
Id: extension-cnes-cpf-diretor
Title: "Número do CPF do Diretor do Estabelecimento de Saúde"
Description: "Número do CPF (Cadastro de Pessoa Física) do Diretor responsável pelo Estabelecimento de Saúde."
Context: Organization
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CpfDiretor"
* ^version = "1.0.0"
* ^experimental = false
* ^date = "2024-07-11"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Identificar o Diretor do Estabelecimento de Saúde por meio do seu número de CPF."
* ^abstract = true
* id ..0
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CpfDiretor" (exactly)
* value[x] only Coding
* valueCoding 1..1
* valueCoding.system 1..1
* valueCoding.code 1..1
* valueCoding.display 1..1
