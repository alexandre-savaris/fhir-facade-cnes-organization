package org.alexandresavaris.model;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.*;

@ResourceDef(name = "Organization")
public class CnesOrganization extends Organization {
    // City code.
    @Description(shortDefinition = "The city code defined and maintained by the Brazilian Institute of Geography and Statistics (IBGE).")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CodigoMunicipioIbge",
        isModifier = false,
        definedLocally = true)
    @Child(name = "cityCodeIbge")
    private CodeType cityCodeIbge;
    // State code.
    @Description(shortDefinition = "The state code defined and maintained by the Brazilian Institute of Geography and Statistics (IBGE).")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CodigoUfIbge",
        isModifier = false,
        definedLocally = true)
    @Child(name = "stateCodeIbge")
    private CodeType stateCodeIbge;
    // Update date.
    @Description(shortDefinition = "The date when the resource instance's data were updated.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/DataAtualizacao",
        isModifier = false,
        definedLocally = true)
    @Child(name = "updateDate")
    private DateType updateDate;
    // Director's CPF.
    @Description(shortDefinition = "The CPF number (Cadastro de Pessoa Física) of the Organization's Director.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/CpfDiretor",
        isModifier = false,
        definedLocally = true)
    @Child(name = "cpfDirector")
    private CodeType cpfDirector;
    // Director's name.
    @Description(shortDefinition = "The name of the Organization's Director.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/NomeDiretor",
        isModifier = false,
        definedLocally = true)
    @Child(name = "nameDirector")
    private HumanName nameDirector;

    // Getters and Setters.
    public CodeType getCityCodeIbge() {
        
        if (this.cityCodeIbge == null) {
            this.cityCodeIbge = new CodeType();
        }
        
        return this.cityCodeIbge;
    }
    
    public void setCityCodeIbge(CodeType cityCodeIbge) {
        
        this.cityCodeIbge = cityCodeIbge;
    }

    public CodeType getStateCodeIbge() {
        
        if (this.stateCodeIbge == null) {
            this.stateCodeIbge = new CodeType();
        }
        
        return this.stateCodeIbge;
    }
    
    public void setStateCodeIbge(CodeType stateCodeIbge) {
        
        this.stateCodeIbge = stateCodeIbge;
    }

    public DateType getUpdateDate() {
        
        if (this.updateDate == null) {
            this.updateDate = new DateType();
        }
        
        return this.updateDate;
    }
    
    public void setUpdateDate(DateType updateDate) {
        
        this.updateDate = updateDate;
    }

    public CodeType getCpfDirector() {
        
        if (this.cpfDirector == null) {
            this.cpfDirector = new CodeType();
        }
        
        return this.cpfDirector;
    }
    
    public void setCpfDirector(CodeType cpfDirector) {
        
        this.cpfDirector = cpfDirector;
    }

    public HumanName getNameDirector() {
        
        if (this.nameDirector == null) {
            this.nameDirector = new HumanName();
        }
        
        return this.nameDirector;
    }
    
    public void setNameDirector(HumanName nameDirector) {
        
        this.nameDirector = nameDirector;
    }
    
    // Are all elements of the resource instance null?
    @Override
    public boolean isEmpty() {
        
      return super.isEmpty() && ElementUtil.isEmpty(
          this.cityCodeIbge,
          this.stateCodeIbge,
          this.updateDate,
          this.cpfDirector,
          this.nameDirector);
   }
}
