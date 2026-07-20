import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Configuracion config = GestionDatosTiempo.leerConfig("config.txt");
        if (config == null) {
            return; 
        }

        String idiomaSalida = config.getIdiomaSalida();
        GestionDatosTiempo gestorTiempo = new GestionDatosTiempo();
        gestorTiempo.cargarDiccionario(idiomaSalida);

        HashMap<String, String[][]> mapasSalasReales = ProcesadorAgenda.procesarAgenda(config);
        if (mapasSalasReales == null) {
            return;
        }

        for (String nombreSala : mapasSalasReales.keySet()) {
            String[][] matrizMesSala = mapasSalasReales.get(nombreSala);
            GeneradorHTML.generarArchivoSala(nombreSala, matrizMesSala, config, gestorTiempo);
        }
    }
}
