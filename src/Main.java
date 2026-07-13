import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        
        // 1. CONEXIÓN ANTONIO: Leer configuración del archivo físico
        Configuracion config = GestionDatosTiempo.leerConfig("config.txt");
        if (config == null) return; 

        // Cargar el diccionario de salida según el config (ej: ENG)
        GestionDatosTiempo gestorTiempo = new GestionDatosTiempo();
        gestorTiempo.cargarDiccionario(config.getIdiomaSalida());

        // 2. CONEXIÓN ROBERT: Llamada al método que procesa las agendas
        // (Hoy lee nuestra simulación limpia, el lunes leerá el motor real de Robert)
        HashMap<String, String[][]> mapasSalasReales = ProcesadorAgenda.procesarAgenda(config);

        // 3. CONEXIÓN SIMON (Tú): Tu motor dinámico genera los HTML de todas las salas
        for (String nombreSala : mapasSalasReales.keySet()) {
            String[][] matrizMesSala = mapasSalasReales.get(nombreSala);
            GeneradorHTML.generarArchivoSala(nombreSala, matrizMesSala, config, gestorTiempo);
        }
    }
}
