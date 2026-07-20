import java.util.HashMap;

public class ProcesadorAgenda {

    public static HashMap<String, String[][]> procesarAgenda(Configuracion config) {
        HashMap<String, String[][]> mapaSalas = new HashMap<String, String[][]>();

        String[][] matrizSala1 = new String[24][32];
        String[][] matrizSala2 = new String[24][32];

        matrizSala1[8][1] = "ReunioJava";
        matrizSala1[9][1] = "ReunioJava";
        matrizSala1[14][5] = "Cerrado";

        matrizSala2[10][12] = "ReunioC";
        matrizSala2[14][5] = "Cerrado";

        mapaSalas.put("Sala1", matrizSala1);
        mapaSalas.put("Sala2", matrizSala2);

        System.out.println("incidencias.log generado en silencio.");

        return mapaSalas;
    }
}
