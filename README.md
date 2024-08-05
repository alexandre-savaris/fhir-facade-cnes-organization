# fhir-facade-cnes-organization
A [FHIR®](https://hl7.org/fhir/R4/index.html) facade for accessing data from Brazil's National Registry of Health Facilities ([CNES](https://cnes.datasus.gov.br/) - Cadastro Nacional de Estabelecimentos de Saúde).

The facade accesses a [SOAP webservice](https://datasus.saude.gov.br/wp-content/uploads/2019/12/Especificacao-Tecnica-para-Integracao-com-o-Cadastro-Nacional-de-Estabelecimentos-de-Sa%C3%BAde.pdf) provided by the Department of Informatics of the Brazilian National Health System ([DATASUS](https://datasus.saude.gov.br/)), using its responses to build FHIR® R4 instances for the [`Organization`](https://hl7.org/fhir/r4/organization.html) resource.

The complementary Extensions and NamingSystems used by the project can be found in the [related project](https://simplifier.net/fhir-facade-cnes-organization/~introduction) on [SIMPLIFIER.NET](https://simplifier.net/).

[Java](https://www.oracle.com/br/java/) 17  
[HAPI FHIR](https://hapifhir.io/hapi-fhir/) 7.2.2 (released under the [Apache 2.0](https://hapifhir.io/hapi-fhir/license.html) Licence)  
[Apache Maven](https://maven.apache.org/) 3.9.6

## Usage

To run the REST server using the Jetty Maven Plugin (from the project's root directory):
1. Create dirs `/data` and `/logs` (`C:\data` and `C:\logs` on Windows)
2. `mvn jetty:run`

To run the REST server using Docker (from the project's root directory):
1. `mvn clean package`
2. `docker build -t fhir-facade-cnes-organization .`
3. `docker run -p 8080:8080 fhir-facade-cnes-organization`

Using a REST client, execute a GET request to the following URL: http://localhost:8080/fhir/Organization/`{9999999}`
- Replace the placeholder `{9999999}` with a valid CNES number. (You can search for valid CNES numbers [here](https://cnes.datasus.gov.br/pages/estabelecimentos/consulta.jsp)).
- Use the Bearer token `0190dfc9-288e-70b3-80a5-70eb9a03d68e` for authentication.

## Notes
The facade accesses the homologation instance of the SOAP webservice, whose data are available for __test use only__. 
