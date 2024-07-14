package org.alexandresavaris.model;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.util.ElementUtil;
import java.util.ArrayList;
import java.util.List;
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
    // Unity's/Organization's type.  TODO: rever para "type".
    @Description(shortDefinition = "The Organization's type.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/TipoUnidade",
        isModifier = false,
        definedLocally = true)
    @Child(name = "unityType")
    private Coding unityType;
    // Geolocation.
    @Description(shortDefinition = "The Organization's geolocation.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "http://hl7.org/fhir/StructureDefinition/geolocation",
        isModifier = false,
        definedLocally = false)
    @Child(name = "geolocation")
    private Geolocation geolocation;
    // Is the Organization part of SUS?
    @Description(shortDefinition = "Is the Organization part of SUS?")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "http://www.saude.gov.br/fhir/r4/StructureDefinition/BRAtendeSUS-1.0",
        isModifier = false,
        definedLocally = false)
    @Child(name = "isSus")
    private BooleanType isSus;
    // Specialized Services.
    @Description(shortDefinition = "Specialized Services offered by the Organization.")
    @ca.uhn.fhir.model.api.annotation.Extension(
        url = "SpecializedServices",
        isModifier = false,
        definedLocally = true)
    @Child(name = "specializedServices")
    private List<SpecializedService> specializedServices;
    
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

    public Coding getUnityType() {
        
        if (this.unityType == null) {
            this.unityType = new Coding();
        }
        
        return this.unityType;
    }
    
    public void setUnityType(Coding unityType) {
        
        this.unityType = unityType;
    }

    public Geolocation getGeolocation() {
        
        if (this.geolocation == null) {
            this.geolocation = new Geolocation();
        }
        
        return this.geolocation;
    }
    
    public void setGeolocation(Geolocation geolocation) {
        
        this.geolocation = geolocation;
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

    public List<SpecializedService> getSpecializedServices() {
        
        if (this.specializedServices == null) {
            this.specializedServices = new ArrayList<>();
        }
        
        return this.specializedServices;
    }
    
    public void setSpecializedServices(List<SpecializedService> specializedServices) {
        
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
          this.cityCodeIbge,
          this.stateCodeIbge,
          this.updateDate,
          this.cpfDirector,
          this.nameDirector,
          this.unityType,
          this.geolocation,
          this.isSus,
          this.specializedServices);
   }

   /**
    * This "block definition" defines an extension type with multiple child extensions.
    */
   @Block
   public static class Geolocation extends BackboneElement {
       // Latitude.
       @Description(shortDefinition = "The latitude for the Organization's location.")
       @ca.uhn.fhir.model.api.annotation.Extension(
           url = "latitude",
           isModifier = false,
           definedLocally = false)
       @Child(name = "latitude")
       private DecimalType latitude;
       // Longitude.
       @Description(shortDefinition = "The longitude for the Organization's location.")
       @ca.uhn.fhir.model.api.annotation.Extension(
           url = "longitude",
           isModifier = false,
           definedLocally = false)
       @Child(name = "longitude")
       private DecimalType longitude;
       
       // Getters and Setters.
      public DecimalType getLatitude() {
          
         if (this.latitude == null) {
            this.latitude = new DecimalType();
         }
         
         return this.latitude;
      }

      public void setLatitude(DecimalType latitude) {
          
         this.latitude = latitude;
      }

      public DecimalType getLongitude() {
          
         if (this.longitude == null) {
            this.longitude = new DecimalType();
         }
         
         return this.longitude;
      }

      public void setLongitude(DecimalType longitude) {
          
         this.longitude = longitude;
      }

      /* *****************************
       * Boilerplate methods- Hopefully these will be removed or made optional
       * in a future version of HAPI but for now they need to be added to all block
       * types. These two methods follow a simple pattern where a utility method from
       * ElementUtil is called and all fields are passed in.
       * *****************************/
      @Override
      public BackboneElement copy() {
          
         Geolocation geolocation = new Geolocation();
         geolocation.setLatitude(this.latitude);
         geolocation.setLongitude(this.longitude);
         
         return geolocation;
      }

      @Override
      public boolean isEmpty() {
          
         return ElementUtil.isEmpty(this.latitude, this.longitude);
      }
   }

   /**
    * This "block definition" defines an extension type with multiple child extensions.
    */
   @Block
   public static class SpecializedService extends BackboneElement {
       // Specialized Service.
       @Description(shortDefinition = "The Specialized Service provided by the Organization.")
       @ca.uhn.fhir.model.api.annotation.Extension(
           url = "https://alexandresavaris.org/fhir/r4/Extension/cnes/ServicoEspecializado",
           isModifier = false,
           definedLocally = true)
       @Child(name = "specializedService")
       private Coding specializedService;
       
       // Getters and Setters.
      public Coding getSpecializedService() {
          
         if (this.specializedService == null) {
            this.specializedService = new Coding();
         }
         
         return this.specializedService;
      }

      public SpecializedService setSpecializedService(Coding specializedService) {
          
         this.specializedService = specializedService;
         
         return this;
      }

      /* *****************************
       * Boilerplate methods- Hopefully these will be removed or made optional
       * in a future version of HAPI but for now they need to be added to all block
       * types. These two methods follow a simple pattern where a utility method from
       * ElementUtil is called and all fields are passed in.
       * *****************************/
      @Override
      public BackboneElement copy() {
          
         SpecializedService specializedService = new SpecializedService();
         specializedService.setSpecializedService(this.specializedService);
         
         return specializedService;
      }

      @Override
      public boolean isEmpty() {
          
         return ElementUtil.isEmpty(this.specializedService);
      }
   }
}
