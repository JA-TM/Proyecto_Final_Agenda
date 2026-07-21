import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String archivoConfig = "config.txt";
        String archivoPeticions = "peticions.txt";
        String archivoLog = "incidencies.log";

        String anyMes = "";
        String idiomas = "";
        String idiomaSortida = "ESP"; 

        List<String> peticionesCerrado = new ArrayList<>();
        List<String> peticionesEstandar = new ArrayList<>();

        
        try (BufferedReader br = new BufferedReader(new FileReader(archivoConfig))) {
            anyMes = br.readLine(); 
            idiomas = br.readLine(); 
            if (idiomas != null && idiomas.split(" ").length == 2) {
                idiomaSortida = idiomas.split(" ")[1]; 
            }
        } catch (IOException e) {
            
        }

        
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPeticions))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                
                if (linea.toLowerCase().startsWith("cerrado") || linea.toLowerCase().startsWith("tancat")) {
                    peticionesCerrado.add(linea);
                } else {
                    peticionesEstandar.add(linea);
                }
            }
        } catch (IOException e) {
            
        }

        
        int totalSemanas = 6; 
        String[][][] matrizSala1 = new String[totalSemanas][7][24];
        String[][][] matrizSala2 = new String[totalSemanas][7][24];

       
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(archivoLog))) {
            logWriter.write("#Resumen Actividades " + (anyMes != null ? anyMes.replace(" ", "/") : "") + "\n");

            
            for (String peticion : peticionesCerrado) {
                inyectarEnMatriz(peticion, matrizSala1, matrizSala2, logWriter);
            }

            for (String peticion : peticionesEstandar) {
                inyectarEnMatriz(peticion, matrizSala1, matrizSala2, logWriter);
            }
            
        } catch (IOException e) {
            
        }

       
        GeneradorHTML.generarArchivoHTML("Sala1", matrizSala1, totalSemanas, idiomaSortida);
        GeneradorHTML.generarArchivoHTML("Sala2", matrizSala2, totalSemanas, idiomaSortida);
    }

    private static void inyectarEnMatriz(String linea, String[][][] s1, String[][][] s2, BufferedWriter log) throws IOException {
        String[] partes = linea.split(" ");
        if (partes.length < 6) return;

        String actividad = partes[0];
        String sala = partes[1];
        String horasMascara = partes[5]; 

        
        try {
            String[] rangos = horasMascara.split("_");
            for (String rango : rangos) {
                String[] h = rango.split("-");
                int inicio = Integer.parseInt(h[0]);
                int fin = Integer.parseInt(h[1]);

                String[][][] matrizObjetivo = sala.equalsIgnoreCase("Sala1") ? s1 : s2;

                for (int sem = 0; sem < 6; sem++) {
                    for (int dia = 0; dia < 7; dia++) {
                        for (int hora = inicio; hora < fin; hora++) {
                            if (matrizObjetivo[sem][dia][hora] == null) {
                                matrizObjetivo[sem][dia][hora] = actividad;
                            } else {
                                log.write("Conflicto: " + actividad + " no pudo entrar en " + sala + " (Ocupado por " + matrizObjetivo[sem][dia][hora] + ")\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            
        }
    }
}
