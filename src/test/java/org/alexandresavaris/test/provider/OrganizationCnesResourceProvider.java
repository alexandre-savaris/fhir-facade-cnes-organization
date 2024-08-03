package org.alexandresavaris.test.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.List;
import org.alexandresavaris.model.OrganizationCnes;
import org.alexandresavaris.util.Utils;
import org.hl7.fhir.r4.model.*;

/**
 * Definition of a ResourceProvider for managing instances of the
 * <code>OrganizationCnes</code> class used in tests.
 */
public class OrganizationCnesResourceProvider implements IResourceProvider {

    // The empty constructor.
    public OrganizationCnesResourceProvider() {
    }
    
    /**
     * The getResourceType method comes from IResourceProvider, and must be
     * overridden to indicate what type of resource this provider supplies.
     */
    @Override
    public Class<OrganizationCnes> getResourceType() {
       
        return OrganizationCnes.class;
    }

    /**
     * The "@Read" annotation indicates that this method supports the
     * read operation. It takes one argument, the Resource type being returned.
     *
     * @param theId The read operation takes one parameter, which must be
     * of type IdDt and must be annotated with the "@Read.IdParam" annotation.
     * @return Returns a resource matching this identifier, or null if none
     * exists.
     * @throws java.lang.Exception
     */
    @Read(type = OrganizationCnes.class)
    public OrganizationCnes getResourceById(@IdParam IdType theId)
        throws Exception {
        
        // Fill in the resource with test data.
        OrganizationCnes retVal = new OrganizationCnes();
        
        // The logical ID replicates the Organization's CNES.
        retVal.setId(theId);
            
        // CodigoCNES -> Identifier: CNES.
        retVal.addIdentifier()
            .setSystem("urn:oid:" + Utils.oids.get("cnes"))
            .setValue(theId.getValue());

        // CodigoUnidade -> Identifier: Unity code.
        retVal.addIdentifier()
            .setSystem(Utils.namingSystems.get("unityCode"))
            .setValue("1111111111111");

        // numeroCNPJ -> Identifier: CNPJ.
        retVal.addIdentifier()
            .setSystem(Utils.namingSystems.get("cnpj"))
            .setValue("22222222222222");

        // nomeFantasia -> name.
        retVal.setName("THE TEST ORGANIZATION'S NAME");

        // nomeEmpresarial -> alias.
        retVal.addAlias("THE TEST ORGANIZATION'S ALIAS");
            
        // Endereco -> Address.
        String street = "THE STREET";
        String number = "1";
        String neighborhood = "THE NEIGHBORHOOD";
        String city = "THE CITY";
        String state = "THE STATE";
        String addressTextTemplate = "{0}, {1} - {2} - {3} - {4}";
        String addressText = java.text.MessageFormat.format(
            addressTextTemplate, street, number, neighborhood, city, state);
        
        Address address = new Address()
            .setUse(Address.AddressUse.WORK)
            .setType(Address.AddressType.BOTH)
            .setText(addressText)
            .setCity(city)
            .setState(state)
            .setPostalCode("33333")
            .setCountry("BRA");

        // Extensions for IBGE codes.
        Extension cityCodeIbgeExtension
            = new Extension(Utils.extensions.get("cityCodeIbge"));
        cityCodeIbgeExtension.setValue(
            new Coding()
                .setSystem("urn:oid:" + Utils.oids.get("ibgeCode"))
                .setCode("444444")
                .setDisplay(
                    new String(
                        "Código do município no IBGE".getBytes("ISO-8859-1"),
                        "UTF-8"
                    )
                )
        );

        Extension stateCodeIbgeExtension
            = new Extension(Utils.extensions.get("stateCodeIbge"));
        stateCodeIbgeExtension.setValue(
            new Coding()
                .setSystem("urn:oid:" + Utils.oids.get("ibgeCode"))
                .setCode("55")
                .setDisplay(
                    new String(
                        "Código da UF no IBGE".getBytes("ISO-8859-1"),
                        "UTF-8"
                    )
                )
        );

        // Completing the address to be returned.
        address.addExtension(cityCodeIbgeExtension);
        address.addExtension(stateCodeIbgeExtension);
        
        // Geolocation extensions.
        Extension geolocationExtension
            = new Extension(Utils.extensions.get("geolocation"));
        Extension latitudeExtension
            = new Extension(Utils.extensions.get("latitude"));
        latitudeExtension.setValue(new DecimalType("-99.99999"));
        Extension longitudeExtension
            = new Extension(Utils.extensions.get("longitude"));
        longitudeExtension.setValue(new DecimalType("-99.99999"));
        geolocationExtension.addExtension(latitudeExtension);
        geolocationExtension.addExtension(longitudeExtension);
        
        // Completing the address to be returned.
        address.addExtension(geolocationExtension);
        retVal.addAddress(address);
        
        // dataAtualizacao -> Extension (update date).
        retVal.setUpdateDate(new DateType("1980-01-01"));

        // numeroCPF -> Extension (Director's CPF).
        retVal.setDirectorCpf(
            new Coding()
                .setSystem("urn:oid:" + Utils.oids.get("cpf"))
                .setCode("42424242424")
                .setDisplay(
                    new String(
                        "Número do CPF do Diretor".getBytes("ISO-8859-1"),
                        "UTF-8"
                    )
                )
        );

        // Nome -> Extension (Director's name).
        retVal.setDirectorName(
            new HumanName().setText("Marvin the Paranoid Android")
        );

        // tipoUnidade -> type.
        retVal.addType(
            new CodeableConcept(
                new Coding()
                    .setSystem(Utils.valueSets.get("type"))
                    .setCode("77")
                    .setDisplay("THE UNITY TYPE")
            )
        );
                
        // Telefone -> contact
        String phoneTemplate = "{0} {1}";
        String phone = java.text.MessageFormat.format(
            phoneTemplate, "88", "999999999");
        retVal.addContact()
            .addTelecom(
                new ContactPoint()
                    .setSystem(ContactPoint.ContactPointSystem.PHONE)
                    .setValue(phone)
                    .setUse(ContactPoint.ContactPointUse.WORK)
            )
            .setPurpose(
                new CodeableConcept(
                    new Coding()
                        .setSystem(Utils.namingSystems.get("phoneType"))
                        .setCode("1")
                        .setDisplay("THE PHONE TYPE")
                )
            );

        // Email -> contact
        retVal.addContact()
            .addTelecom(
                new ContactPoint()
                    .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                    .setValue("aa@bb.cc")
                    .setUse(ContactPoint.ContactPointUse.WORK)
            )
            .setPurpose(
                new CodeableConcept(
                    new Coding()
                        .setSystem(Utils.namingSystems.get("emailType"))
                        .setCode("1")
                )
            );
            
        // perteceSistemaSUS -> Extension (Is the Organization part of SUS?).
        retVal.setIsSus(new BooleanType("true"));

        // fluxoClientela -> Extension (The client flow expected for the
        // Organization).
        retVal.setClientFlow(
            new CodeType("THE EXPECTED CLIENT FLOW")
                .setSystem(Utils.namingSystems.get("clientFlow")
            )
        );
            
        // servicoespecializados -> Extension (specializedServices).
        List<OrganizationCnes.SpecializedService> specializedServices
            = new ArrayList<>();

        OrganizationCnes.SpecializedService specializedService
            = new OrganizationCnes.SpecializedService()
                .setSpecializedService(
                    new Coding()
                        .setSystem(
                            Utils.namingSystems.get("specializedServiceType")
                        )
                        .setCode("999")
                        .setDisplay("THE SPECIALIZED SERVICE")
                );

        List<OrganizationCnes.SpecializedService.SpecializedServiceClassification>
            specializedServiceClassifications
                = specializedService.getSpecializedServiceClassifications();
        
        OrganizationCnes.SpecializedService.SpecializedServiceClassification
            specializedServiceClassification = 
                new OrganizationCnes.SpecializedService.SpecializedServiceClassification()
                    .setSpecializedServiceClassification(
                        new Coding()
                            .setSystem(
                                Utils.namingSystems.get(
                                    "specializedServiceClassification"
                                )
                            )
                            .setCode("999")
                            .setDisplay("THE SPECIALIZED SERVICE CLASSIFICATION")
                    );
        specializedServiceClassification
            .getSpecializedServiceClassificationCharacteristic()
            .setSystem(
                Utils.namingSystems.get(
                    "specializedServiceClassificationCharacteristic"
                )
            )
            .setValue("9");
        specializedServiceClassification
            .getSpecializedServiceClassificationCnes()
            .setSystem("urn:oid:" + Utils.oids.get("cnes"))
            .setCode("THE SPECIALIZED SERVICE CLASSIFICATION CHARACTERISTIC CNES")
            .setDisplay(
                new String(
                    "Número no CNES".getBytes("ISO-8859-1"),
                    "UTF-8"
                )
            );
        
        specializedServiceClassifications.add(specializedServiceClassification);
        specializedServices.add(specializedService);
        
        retVal.setSpecializedServices(specializedServices);
        
        return retVal;
    }
}
