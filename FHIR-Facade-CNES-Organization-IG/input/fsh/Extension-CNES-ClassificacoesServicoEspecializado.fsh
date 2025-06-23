Extension: ClassificacoesServicoEspecializado
Id: extension-cnes-classificacoes-servico-especializado
Title: "Relação de Classificações de um Serviço especializado ofertado pelo Estabelecimento de Saúde"
Description: "Relação de Classificações de um Serviço Especializado ofertado pelo Estabelecimento de Saúde."
Context: Organization.extension
* ^language = #pt-BR
* ^url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacoesServicoEspecializado"
* ^version = "2.0.0"
* ^experimental = false
* ^date = "2025-06-22"
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
    ClassificacaoServicoEspecializado named classificacaoServicoEspecializado 1..1 and
    CaracteristicaClassificacaoServicoEspecializado named caracteristicaClassificacaoServicoEspecializado 1..1 and
    CnesClassificacaoServicoEspecializado named cnesClassificacaoServicoEspecializado 1..1
* url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacoesServicoEspecializado" (exactly)
