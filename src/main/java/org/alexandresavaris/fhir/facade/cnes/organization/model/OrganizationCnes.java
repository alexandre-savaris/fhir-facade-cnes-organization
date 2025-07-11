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
}
