import java.time.LocalDate;


//declaración de la clase Peticion and atributos de la clase Peticion
public class Peticion{
    private String actividad;
    private String espacio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String mascaraDias;
    private String mascaraHoras;
    
    //constructor de la clase Peticion
    public Peticion(String actividad, String espacio, LocalDate fechaInicio, LocalDate fechaFin, 
            String mascaraDias, String mascaraHoras){
            this.actividad = actividad;
            this.espacio = espacio;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.mascaraDias = mascaraDias;
            this.mascaraHoras = mascaraHoras;
    }
    
    //métodos getter de la clase Peticion para cada atributo de la clase Peticion
    public String getActividad(){
        return actividad;
    }

    public String getEspacio(){
        return espacio;
    }

    public LocalDate getFechaInicio(){
        return fechaInicio;
    }

    public LocalDate getFechaFin(){
        return fechaFin;
    }

    public String getMascaraDias(){
        return mascaraDias;
    }

    public String getMascaraHoras(){
        return mascaraHoras;
    }
}

