Extension: ClassificacoesServicoEspecializado
Id: extension-cnes-classificacoes-servico-especializado
Title: "Relação de Classificações de um Serviço especializado ofertado pelo Estabelecimento de Saúde"
Description: "Relação de Classificações de um Serviço Especializado ofertado pelo Estabelecimento de Saúde."
Context: Organization.extension
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacoesServicoEspecializado"
* ^version = "1.0.0"
* ^experimental = false
* ^date = "2024-08-03"
* ^publisher = "Alexandre Savaris"
* ^contact.name = "Alexandre Savaris"
* ^contact.telecom.system = #email
* ^contact.telecom.value = "alexandre.savaris@gmail.com"
* ^jurisdiction.coding = urn:iso:std:iso:3166#BRA "Brazil"
* ^jurisdiction.coding.userSelected = false
* ^purpose = "Relacionar as Classificações de um Serviço Especializado ofertado por um Estabelecimento de Saúde."
* ^abstract = true
* id ..0
* extension contains
    ClassificacaoServicoEspecializado 0..* and
    CaracteristicaClassificacaoServicoEspecializado 0..* and
    CnesClassificacaoServicoEspecializado 0..*
* extension[ClassificacaoServicoEspecializado].id ..0
* extension[ClassificacaoServicoEspecializado].value[x] only Coding
* extension[CaracteristicaClassificacaoServicoEspecializado].id ..0
* extension[CaracteristicaClassificacaoServicoEspecializado].value[x] only code
* extension[CnesClassificacaoServicoEspecializado].id ..0
* extension[CnesClassificacaoServicoEspecializado].value[x] only Coding
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacoesServicoEspecializado" (exactly)