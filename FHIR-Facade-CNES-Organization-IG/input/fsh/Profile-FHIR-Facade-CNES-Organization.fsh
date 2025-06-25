Profile: FHIRFacadeCNESOrganization
Parent: br-core-organization
// "identifier" must be supported and must be included in summaries.
* identifier MS SU
// Suppress the "cpf" slice.
* identifier[cpf] 0..0
// Configure the remaining slices.
* identifier[cnes].use 0..0
* identifier[cnpj].use 0..0
// Add a new slice for "codigoUnidade".
* identifier contains codigoUnidade 0..1
* identifier[codigoUnidade] ^short = "Identificador do estabelecimento"
* identifier[codigoUnidade] ^definition = "Identificador do estabelecimento"
* identifier[codigoUnidade].use 0..0
* identifier[codigoUnidade].system ^short = "Sistema do identificador do estabelecimento"
* identifier[codigoUnidade].system ^definition = "Sistema que identifica o tipo do identificador do estabelecimento"
* identifier[codigoUnidade].system = "https://alexandresavaris.org/fhir/r4/NamingSystem/cnes/CodigoUnidade"  (exactly)
* identifier[codigoUnidade].value ^short = "Valor do identificador do estabelecimento"
* identifier[codigoUnidade].value ^definition = "Valor do identificador do estabelecimento"
* identifier[codigoUnidade].value 1..
* identifier[codigoUnidade].value only string
// Close the slicing for "identifier".
* identifier ^slicing.rules = #closed
// Suppress the "active" attribute.
* active 0..0
// There can be at most 1 value for the "type" attribute.
* type ..1
// Suppress the "telecom" attribute.
* telecom 0..0
// Adding extensions to the "address" attribute.
* address.extension contains CodigoMunicipioIbge named codigoMunicipioIbge 0..1
* address.extension contains CodigoUfIbge named codigoUfIbge 0..1
// Suppress the "partOf" attribute.
* partOf 0..0
// Configure the "address" attribute.
* address.use 1..1
* address.type 1..1
* address.text 1..1
* address.line 0..0
* address.city 1..1
* address.district 0..0
* address.state 1..1
* address.postalCode 1..1
* address.country 1..1
* address.period 0..0
* address.extension contains geolocation named geolocation 0..1
// Configure the "contact" attribute.
* contact.id 0..0
* contact.extension 0..0
* contact.purpose 1..1
* contact.purpose.coding 1..1
* contact.purpose.coding.system 1..1
* contact.purpose.coding.version 0..0
* contact.purpose.coding.code 1..1
* contact.purpose.coding.display 1..1
* contact.purpose.coding.userSelected 0..0
* contact.purpose.text 0..0
* contact.name 0..0
* contact.telecom 1..1
* contact.telecom.system 1..1
* contact.telecom.value 1..1
* contact.telecom.use 1..1
* contact.telecom.rank 0..0
* contact.telecom.period 0..0
* contact.address 0..0
// Suppress the "endpoint" attribute.
* endpoint 0..0
// Adding extensions to the root of the resource.
* extension contains DataAtualizacao named dataAtualizacao 0..1
* extension contains CpfDiretor named cpfDiretor 0..1
* extension contains NomeDiretor named nomeDiretor 0..1
* extension contains BRAtendeSUS named atendeSus 0..1
* extension contains ExtensionFluxoClientela named fluxoClientela 0..1
* extension contains ServicosEspecializados named servicosEspecializados 0..1
