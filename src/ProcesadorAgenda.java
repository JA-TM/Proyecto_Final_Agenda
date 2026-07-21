import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;
import java.io.FileWriter;
import java.io.IOException;




public class ProcesadorAgenda {

    private ArrayList<Peticion> peticiones = new ArrayList<Peticion>(); // declararion de la lista
    private ArrayList<String> incidencias = new ArrayList<String>(); // declararion de la lista de incidencias
    private HashMap<String, Integer> horasAsignadas = new HashMap<>();
    private HashMap<String, Integer> horasNoAsignadas = new HashMap<>();
   
   
    // declarar el método leerPeticiones que recibe la ruta del fichero como parámetro y lanza una excepción de tipo FileNotFoundException
    public void leerPeticiones(String rutaFichero) throws FileNotFoundException { 
        File fichero = new File(rutaFichero); // crear un objeto File con la ruta del fichero
        Scanner sc = new Scanner(fichero); // crear un objeto Scanner para leer el fichero

        while (sc.hasNextLine()) {  // mientras haya líneas en el fichero
            String linea = sc.nextLine();
            String[] campos = linea.split(" ");
            // conectamos campos[2] y campos[3] que recibimos como texto 
            // lo convertimos a local date con el método convertirFecha
            LocalDate fechaInicio = convertirFecha(campos[2]);
            LocalDate fechaFin = convertirFecha(campos[3]);

            // creamos un objeto Peticion con los campos leídos del fichero
            Peticion p = new Peticion(campos[0], campos[1], fechaInicio, fechaFin, campos[4], campos[5]);
            // y guardarla en la lista
            peticiones.add(p);
        }
        sc.close();
    }  
    //Metodo para convertir la fecha de texto a LocalDate
    public LocalDate convertirFecha(String fechaTexto) {
        String[] partes = fechaTexto.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int anio = Integer.parseInt(partes[2]);
        return LocalDate.of(anio, mes, dia);
    }
    //metodo de validación de la fecha de inicio y fin de la petición

    public ArrayList<Peticion> validarPeticiones() {
        ArrayList<Peticion> validas = new ArrayList<Peticion>();

        for (Peticion p : peticiones) { //utilizamos un bucle for-each para recorrer la lista de peticiones
            if (p.getFechaFin().isBefore(p.getFechaInicio()) || !mascaraHorasValidas(p.getMascaraHoras())) { //si la fecha de fin es anterior a la fecha de inicio, la petición es inválida
                        System.out.println("Peticion invalida descartada: " + p.getActividad());
        } else {
                validas.add(p);
        }
    
        }
        return validas;
    
        

    }
    //método para ordenar las peticiones, primero las que tienen actividad "Tancat" y luego el resto
    public ArrayList<Peticion> ordenarTancatPrimero(ArrayList<Peticion> lista){
        ArrayList<Peticion> tancats = new ArrayList<Peticion>();
        ArrayList<Peticion> resto = new ArrayList<Peticion>();

        for(Peticion p: lista){
            if(p.getActividad().equals("Tancat")){
                tancats.add(p);
            }else{
                resto.add(p);
            }

        }

        ArrayList<Peticion> ordenada = new ArrayList<Peticion>();

        ordenada.addAll(tancats);
        ordenada.addAll(resto);

       
        return ordenada;

    }
    //método para validar el rango horario de la petición
    public boolean rangoHorarioValido(String rango){
        String[] horas = rango.split("-");
        int horaInicio = Integer.parseInt(horas[0]);//convertimos la hora de inicio a entero
        int horaFin = Integer.parseInt(horas[1]);//convertimos la hora de fin a entero

        if(horaFin <= horaInicio){//si la hora de fin es menor o igual a la hora de inicio, el rango horario no es válido
            return false;
        }
        return true;
    }
    //método para validar la máscara de horas de la petición
    public boolean mascaraHorasValidas(String mascaraHoras){
        String[] rangos = mascaraHoras.split("_");
        for(String rango: rangos){
            if(!rangoHorarioValido(rango)){
                return false;
            }
        }
        return true;
    }
    //método para agrupar las peticiones por actividad
    public ArrayList<Peticion> agruparPorActividad(ArrayList<Peticion> lista) {
        LinkedHashMap<String, ArrayList<Peticion>> grupos = new LinkedHashMap<>();

        for (Peticion p : lista) {
            String actividad = p.getActividad();

            if (!grupos.containsKey(actividad)) {
                grupos.put(actividad, new ArrayList<Peticion>());
            }

            grupos.get(actividad).add(p);
        }

        ArrayList<Peticion> resultado = new ArrayList<Peticion>();
        for (String clave : grupos.keySet()) {
            resultado.addAll(grupos.get(clave));
        }

        return resultado;
    }

    // getter para obtener la lista de peticiones
    public ArrayList<Peticion> getPeticiones() {
           return peticiones;
    }
    // getter para obtener la lista de incidencias
    
    public ArrayList<String> getIncidencias() {
        return incidencias;
    }
     // getter para obtener la lista de horas asignadas
    public HashMap<String, Integer> getHorasAsignadas() {
        return horasAsignadas;
    }
    // getter para obtener la lista de horas no asignadas
     public HashMap<String, Integer> getHorasNoAsignadas() {
        return horasNoAsignadas;
    }

    // método para crear matrices por sala
    public HashMap<String, String[][]> crearMatricesPorSala(ArrayList<Peticion> lista) {
        HashMap<String, String[][]> agendasPorSala = new HashMap<>();

        for (Peticion p : lista) {
            String espacio = p.getEspacio();
             if (!agendasPorSala.containsKey(espacio)) {
                 agendasPorSala.put(espacio, new String[24][32]);
            }
        }

        return agendasPorSala;
    }
    // método para obtener los días del mes que cumplen con la petición
    public ArrayList<Integer> diasDelMesParaPeticion(Peticion p, int anio, int mes, GestionDatosTiempo gestor) {
    ArrayList<Integer> diasValidos = new ArrayList<Integer>();
    int totalDias = GestionDatosTiempo.getDiasDelMes(anio, mes);

    for (int dia = 1; dia <= totalDias; dia++) {
        LocalDate fechaActual = LocalDate.of(anio, mes, dia);

        boolean dentroDelRango = 
            (fechaActual.isEqual(p.getFechaInicio()) || fechaActual.isAfter(p.getFechaInicio()))
            && (fechaActual.isEqual(p.getFechaFin()) || fechaActual.isBefore(p.getFechaFin()));

        if (dentroDelRango) {
            int numeroDiaSemana = GestionDatosTiempo.getDiaDeLaSemana(anio, mes, dia);
            if (gestor.diaEstaEnMascara(p.getMascaraDias(), numeroDiaSemana)) {
                diasValidos.add(dia);
            }
        }
    }

        return diasValidos;
    }
    // método para obtener las horas de la máscara de horas
    public ArrayList<Integer> horasDeMascara(String mascaraHoras) {
        ArrayList<Integer> horas = new ArrayList<Integer>();
        String[] rangos = mascaraHoras.split("_");

        for (String rango : rangos) {
            String[] partes = rango.split("-");
            int horaInicio = Integer.parseInt(partes[0]);
            int horaFin = Integer.parseInt(partes[1]);

            for (int hora = horaInicio; hora < horaFin; hora++) {
                horas.add(hora);
            }
        }
        return horas;
    }
    // método para rellenar la matriz de una sala con las peticiones válidas
    public void rellenarMatriz(String[][] matriz, ArrayList<Peticion> peticionesDeSala,
        int anio, int mes, GestionDatosTiempo gestor, String nombreSala) {
            for(Peticion p : peticionesDeSala){
                ArrayList<Integer> dias = diasDelMesParaPeticion(p, anio, mes, gestor);
                ArrayList<Integer> horas = horasDeMascara(p.getMascaraHoras());

                for (int dia : dias){
                    for (int hora : horas){
                        if (matriz[hora][dia] == null) {
                            matriz[hora][dia] = p.getActividad();
                        
                        String actividad = p.getActividad();
                        if(!horasAsignadas.containsKey(actividad)){
                            horasAsignadas.put(actividad, 0);
                        }
                        int valorActual = horasAsignadas.get(actividad);
                        horasAsignadas.put(actividad, valorActual + 1);

                        }else{
                            
                        String mensaje = "Conflicto: " + p.getActividad() + " no pudo entrar en " + nombreSala + " dia " + dia + " hora " + hora + " (ocupado por " + matriz[hora][dia] + ")";
                        incidencias.add(mensaje);

                    
                        String actividadPerdedora = p.getActividad();
                        if (!horasNoAsignadas.containsKey(actividadPerdedora)) {
                        horasNoAsignadas.put(actividadPerdedora, 0);
                        }
                        int valorActualNo = horasNoAsignadas.get(actividadPerdedora);
                        horasNoAsignadas.put(actividadPerdedora, valorActualNo + 1);
                    
                         
                        }

                    }
                }
            }
    }    
    // método para filtrar las peticiones por sala
    public ArrayList<Peticion> filtrarPorSala(ArrayList<Peticion> lista, String sala){
        ArrayList<Peticion>  resultado = new ArrayList<Peticion>();
        for(Peticion p : lista){
            if(p.getEspacio().equals(sala)){
                resultado.add(p);
            }
        }

        return resultado;
    }
    // método para escribir las incidencias en un fichero
    public void escribirIncidencias(String rutaFichero) throws IOException{
        FileWriter fw = new FileWriter(rutaFichero);
        PrintWriter pw = new PrintWriter(fw);
        for(String incidencia : incidencias){
            pw.println(incidencia);
        }
        pw.close();
    }

     // método main para probar la clase ProcesadorAgendas
    public static void main(String[] args) {
        ProcesadorAgenda procesador = new ProcesadorAgenda();
        try {
            procesador.leerPeticiones("peticions.txt");
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el fichero: " + e.getMessage());
        }
        ArrayList<Peticion> validas = procesador.validarPeticiones();
        ArrayList<Peticion> ordenadas = procesador.ordenarTancatPrimero(validas);
        ArrayList<Peticion> agrupadas = procesador.agruparPorActividad(ordenadas);
        
        GestionDatosTiempo gestor = new GestionDatosTiempo();
        gestor.cargarDiccionario("CAT");

        ArrayList<Peticion> peticionesSala1 = procesador.filtrarPorSala(agrupadas, "Sala1");
        ArrayList<Peticion> peticionesSala2 = procesador.filtrarPorSala(agrupadas, "Sala2");

        HashMap<String, String[][]> matrices = procesador.crearMatricesPorSala(agrupadas);
        String[][] matrizSala1 = matrices.get("Sala1");
        String[][] matrizSala2 = matrices.get("Sala2");
        
        procesador.rellenarMatriz(matrizSala1, peticionesSala1, 2025, 11, gestor, "Sala1");
        procesador.rellenarMatriz(matrizSala2, peticionesSala2, 2025, 11, gestor, "Sala2");

        

        try{
            procesador.escribirIncidencias("incidencias.log");
        } catch (IOException e) {
            System.out.println("Error al escribir incidencias: " + e.getMessage());
        }

        System.out.println("\n--- Horas asignadas por actividad ---");
        for (String actividad : procesador.getHorasAsignadas().keySet()) {
        System.out.println(actividad + ": " + procesador.getHorasAsignadas().get(actividad));
    
        }

        System.out.println("\n--- Horas NO asignadas por actividad ---");
        for (String actividad : procesador.getHorasNoAsignadas().keySet()) {
        System.out.println(actividad + ": " + procesador.getHorasNoAsignadas().get(actividad));
        }

    }

}

