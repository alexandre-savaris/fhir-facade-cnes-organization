package org.alexandresavaris.fhir.facade.cnes.organization.model;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.Coding;

/**
 * This "block definition" defines an extension type with multiple child
 * extensions.
 */
@Block
public class SpecializedServiceClassification
    extends BackboneElement {

    // Specialized Service Classification.
    @Description(
        shortDefinition
            = "The classification for the Specialized Service provided by the Organization.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ClassificacaoServicoEspecializado",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedServiceClassification")
    private Coding specializedServiceClassification;

    // Specialized Service Classification Characteristic.
    @Description(
        shortDefinition
            = "The Characteristic from the Classification for the Specialized Service provided by the Organization.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CaracteristicaClassificacaoServicoEspecializado",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedServiceClassificationCharacteristic")
    private Coding specializedServiceClassificationCharacteristic;

    // Specialized Service Classification Cnes.
    @Description(shortDefinition = "The CNES from the Classification for the Specialized Service provided by the Organization.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CnesClassificacaoServicoEspecializado",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedServiceClassificationCnes")
    private Coding specializedServiceClassificationCnes;

    // Getters and Setters.
    public Coding getSpecializedServiceClassification() {

        if (this.specializedServiceClassification == null) {
            this.specializedServiceClassification = new Coding();
        }

        return this.specializedServiceClassification;
    }

    public SpecializedServiceClassification setSpecializedServiceClassification(
        Coding specializedServiceClassification) {

        this.specializedServiceClassification
            = specializedServiceClassification;

        return this;
    }

    public Coding getSpecializedServiceClassificationCharacteristic() {

        if (this.specializedServiceClassificationCharacteristic == null) {
            this.specializedServiceClassificationCharacteristic
                = new Coding();
        }

        return this.specializedServiceClassificationCharacteristic;
    }

    public SpecializedServiceClassification setSpecializedServiceClassificationCharacteristic(
        Coding specializedServiceClassificationCharacteristic) {

        this.specializedServiceClassificationCharacteristic
            = specializedServiceClassificationCharacteristic;

        return this;
    }

    public Coding getSpecializedServiceClassificationCnes() {

        if (this.specializedServiceClassificationCnes == null) {
            this.specializedServiceClassificationCnes = new Coding();
        }

        return this.specializedServiceClassificationCnes;
    }

    public SpecializedServiceClassification setSpecializedServiceClassificationCnes(
        Coding specializedServiceClassificationCnes) {

        this.specializedServiceClassificationCnes
            = specializedServiceClassificationCnes;

        return this;
    }

    /* *****************************
     * Boilerplate methods- Hopefully these will be removed or made
     * optional in a future version of HAPI but for now they need to be
     * added to all block types. These two methods follow a simple
     * pattern where a utility method from ElementUtil is called and all
     * fields are passed in.
     * *****************************/
    @Override
    public BackboneElement copy() {

        SpecializedServiceClassification localSpecializedServiceClassification
            = new SpecializedServiceClassification();
        localSpecializedServiceClassification.setSpecializedServiceClassification(
            this.specializedServiceClassification
        );
        localSpecializedServiceClassification.setSpecializedServiceClassificationCharacteristic(
            this.specializedServiceClassificationCharacteristic
        );
        localSpecializedServiceClassification.setSpecializedServiceClassificationCnes(
            this.specializedServiceClassificationCnes
        );

        return localSpecializedServiceClassification;
    }

    @Override
    public boolean isEmpty() {

        return ElementUtil.isEmpty(
            this.specializedServiceClassification,
            this.specializedServiceClassificationCharacteristic,
            this.specializedServiceClassificationCnes);
    }
}
