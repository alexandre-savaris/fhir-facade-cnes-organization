package org.alexandresavaris.fhir.facade.cnes.organization.model;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.Coding;

import java.util.ArrayList;
import java.util.List;

/**
 * This "block definition" defines an extension type with multiple child
 * extensions.
 */
@Block
public class SpecializedService extends BackboneElement {

    // Specialized Service.
    @Description(
        shortDefinition
            = "The Specialized Service provided by the Organization."
    )
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicoEspecializado",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedService")
    private Coding specializedService;

    // Specialized Service Specification.
    @Description(
        shortDefinition
            = "The Specialized Service Specification provided by the Organization."
    )
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/EspecificacaoServicoEspecializado",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedServiceSpecification")
    private Coding specializedServiceSpecification;

    // Specialized Service Classifications.
    @Description(
        shortDefinition
            = "Classifications of the Specialized Service offered by the Organization.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacoesServicoEspecializado",
        isModifier = false,
        definedLocally = true)
    @Child(name = "SpecializedServiceClassifications")
    private List<SpecializedServiceClassification>
        specializedServiceClassifications;

    // Getters and Setters.
    public Coding getSpecializedService() {

        if (this.specializedService == null) {
            this.specializedService = new Coding();
        }

        return this.specializedService;
    }

    public SpecializedService setSpecializedService(
        Coding specializedService) {

        this.specializedService = specializedService;

        return this;
    }

    public Coding getSpecializedServiceSpecification() {

        if (this.specializedServiceSpecification == null) {
            this.specializedServiceSpecification = new Coding();
        }

        return this.specializedServiceSpecification;
    }

    public SpecializedService setSpecializedServiceSpecification(
        Coding specializedServiceSpecification) {

        this.specializedServiceSpecification = specializedServiceSpecification;

        return this;
    }

    public List<SpecializedServiceClassification> getSpecializedServiceClassifications() {

        if (this.specializedServiceClassifications == null) {
            this.specializedServiceClassifications = new ArrayList<>();
        }

        return this.specializedServiceClassifications;
    }

    public void setSpecializedServiceClassifications(
        List<SpecializedServiceClassification> specializedServiceClassifications) {

        this.specializedServiceClassifications
            = specializedServiceClassifications;
    }

    public void addSpecializedServiceClassification(
        SpecializedServiceClassification specializedServiceClassification) {

        if (this.specializedServiceClassifications == null) {
            this.specializedServiceClassifications = new ArrayList<>();
        }

        this.specializedServiceClassifications.add(
            specializedServiceClassification
        );
    }

    /* *****************************
     * Boilerplate methods- Hopefully these will be removed or made optional
     * in a future version of HAPI but for now they need to be added to all
     * block types. These two methods follow a simple pattern where a
     * utility method from ElementUtil is called and all fields are passed
     * in.
     * *****************************/
    @Override
    public BackboneElement copy() {

        SpecializedService localSpecializedService
            = new SpecializedService();
        localSpecializedService.setSpecializedService(
            this.specializedService
        );
        localSpecializedService.setSpecializedServiceSpecification(
            this.specializedServiceSpecification
        );
        localSpecializedService.setSpecializedServiceClassifications(
            this.specializedServiceClassifications
        );

        return localSpecializedService;
    }

    @Override
    public boolean isEmpty() {

        return ElementUtil.isEmpty(this.specializedService,
                                   this.specializedServiceSpecification,
                                   this.specializedServiceClassifications
        );
    }
}
