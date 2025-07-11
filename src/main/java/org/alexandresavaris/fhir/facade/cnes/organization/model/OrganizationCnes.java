package org.alexandresavaris.fhir.facade.cnes.organization.model;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.util.ElementUtil;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.r4.model.*;

@ResourceDef(name = "Organization")
public class OrganizationCnes extends Organization {

    // Update date.
    @Description(
        shortDefinition
            = "The date when the resource instance's data were updated.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/DataAtualizacao",
        isModifier = false,
        definedLocally = true)
    @Child(name = "updateDate")
    private DateType updateDate;

    // Director's CPF.
    @Description(
        shortDefinition
            = "The CPF number (Cadastro de Pessoa Física) of the Organization's Director.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CpfDiretor",
        isModifier = false,
        definedLocally = true)
    @Child(name = "directorCpf")
    private Coding directorCpf;

    // Director's name.
    @Description(
        shortDefinition
            = "The name of the Organization's Director.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/NomeDiretor",
        isModifier = false,
        definedLocally = true)
    @Child(name = "directorName")
    private HumanName directorName;

    // Is the Organization part of SUS?
    @Description(shortDefinition = "Is the Organization part of SUS?")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://rnds-fhir.saude.gov.br/StructureDefinition/BRAtendeSUS-1.0",
        isModifier = false,
        definedLocally = false)
    @Child(name = "isSus")
    private BooleanType isSus;

    // Client flow.
    @Description(
        shortDefinition = "The client flow expected for the Organization."
    )
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/FluxoClientela",
        isModifier = false,
        definedLocally = true)
    @Child(name = "clientFlow")
    private Coding clientFlow;

    // Specialized Services.
    @Description(
        shortDefinition
            = "Specialized Services offered by the Organization."
    )
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicosEspecializados",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedServices")
    private List<SpecializedService> specializedServices;

    public DateType getUpdateDate() {

        if (this.updateDate == null) {
            this.updateDate = new DateType();
        }

        return this.updateDate;
    }

    public void setUpdateDate(DateType updateDate) {

        this.updateDate = updateDate;
    }

    public Coding getDirectorCpf() {

        if (this.directorCpf == null) {
            this.directorCpf = new Coding();
        }

        return this.directorCpf;
    }

    public void setDirectorCpf(Coding directorCpf) {

        this.directorCpf = directorCpf;
    }

    public HumanName getDirectorName() {

        if (this.directorName == null) {
            this.directorName = new HumanName();
        }

        return this.directorName;
    }

    public void setDirectorName(HumanName directorName) {

        this.directorName = directorName;
    }

    public BooleanType getIsSus() {

        if (this.isSus == null) {
            this.isSus = new BooleanType();
        }

        return this.isSus;
    }

    public void setIsSus(BooleanType isSus) {

        this.isSus = isSus;
    }

    public Coding getClientFlow() {

        if (this.clientFlow == null) {
            this.clientFlow = new Coding();
        }

        return this.clientFlow;
    }

    public void setClientFlow(Coding clientFlow) {

        this.clientFlow = clientFlow;
    }

    public List<SpecializedService> getSpecializedServices() {

        if (this.specializedServices == null) {
            this.specializedServices = new ArrayList<>();
        }

        return this.specializedServices;
    }

    public void setSpecializedServices(
        List<SpecializedService> specializedServices) {

        this.specializedServices = specializedServices;
    }

    public void addSpecializedService(SpecializedService specializedService) {

        if (this.specializedServices == null) {
            this.specializedServices = new ArrayList<>();
        }

        this.specializedServices.add(specializedService);
    }

    // Are all elements of the resource instance null?
    @Override
    public boolean isEmpty() {

      return super.isEmpty() && ElementUtil.isEmpty(
          this.updateDate,
          this.directorCpf,
          this.directorName,
          this.isSus,
          this.clientFlow,
          this.specializedServices
      );
    }

    /**
     * This "block definition" defines an extension type with multiple child
     * extensions.
     */
    @Block
    public static class SpecializedService extends BackboneElement {

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

            return localSpecializedService;
        }

        @Override
        public boolean isEmpty() {

            return ElementUtil.isEmpty(this.specializedService);
        }

        /**
         * This "block definition" defines an extension type with multiple child
         * extensions.
         */
        @Block
        public static class SpecializedServiceClassification
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
    }
}
