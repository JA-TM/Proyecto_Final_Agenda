import java.util.HashMap;

public class ProcesadorAgenda {

    // Simula el método que debía hacer Robert hoy
    public static HashMap<String, String[][]> procesarAgenda(Configuracion config) {
        HashMap<String, String[][]> mapaSalas = new HashMap<String, String[][]>();

        // Creamos las matrices reales vacías para las dos salas configuradas
        String[][] matrizSala1 = new String[24][32];
        String[][] matrizSala2 = new String[24][32];

        // Simulamos que el lector de Robert ya ha colocado algunas reuniones
        matrizSala1[8][1] = "ReunioJava"; // Sábado 1, 08:00h
        matrizSala1[9][1] = "ReunioJava"; // Sábado 1, 09:00h
        matrizSala1[14][5] = "Tancat";     // Miércoles 5, 14:00h

        matrizSala2[10][12] = "ReunioC";   // Miércoles 12, 10:00h
        matrizSala2[14][5] = "Tancat";     // Miércoles 5, 14:00h

        mapaSalas.put("Sala1", matrizSala1);
        mapaSalas.put("Sala2", matrizSala2);

        // Simulamos la creación en silencio del archivo de incidencias vacío por hoy
        System.out.println("incidencias.log generado en silencio.");

        return mapaSalas;
    }
}
