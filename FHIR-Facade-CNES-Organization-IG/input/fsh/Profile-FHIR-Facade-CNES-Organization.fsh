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
// Suppress the "endpoint" attribute.
* endpoint 0..0
// Adding extensions to the root of the resource.
* extension contains DataAtualizacao named dataAtualizacao 0..1
* extension contains CpfDiretor named cpfDiretor 0..1
* extension contains NomeDiretor named nomeDiretor 0..1
* extension contains BRAtendeSUS named atendeSus 0..1
* extension contains ExtensionFluxoClientela named fluxoClientela 0..1
* extension contains ServicosEspecializados named servicosEspecializados 0..1
