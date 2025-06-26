Instance: 14a679be-5b12-41b6-85e7-8793f9b4a93e
InstanceOf: CapabilityStatement
Usage: #definition
* name = "RestServer"
* status = #active
* date = "2025-06-26T17:26:52.359-03:00"
* publisher = "Not provided"
* kind = #instance
* software.name = "HAPI FHIR Server"
* software.version = "7.4.0"
* implementation.description = "HAPI FHIR"
* implementation.url = "http://localhost:8080/fhir"
* fhirVersion = #4.0.1
* format[0] = #application/fhir+xml
* format[+] = #xml
* format[+] = #application/fhir+json
* format[+] = #json
* format[+] = #application/x-turtle
* format[+] = #ttl
* format[+] = #html/json
* format[+] = #html/xml
* format[+] = #html/turtle
* rest.mode = #server
* rest.resource[0].type = #OperationDefinition
* rest.resource[=].profile = "http://hl7.org/fhir/StructureDefinition/OperationDefinition"
* rest.resource[=].interaction.code = #read
* rest.resource[=].searchInclude = "*"
* rest.resource[+].type = #Organization
* rest.resource[=].interaction.code = #read
* rest.resource[=].searchInclude = "*"