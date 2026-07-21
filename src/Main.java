public class Main {
    public static void main(String[] args) {
        // 1. Leer configuracion (modulo Jose Antonio)
        Configuracion config = GestionDatosTiempo.leerConfig("config.txt");
        if (config == null) return;
        int anio = config.getAnio();
        int mes = config.getMes();

        // 2. Gestor con diccionario de ENTRADA (para leer mascaras y "Cerrado")
        GestionDatosTiempo gestorEntrada = new GestionDatosTiempo();
        gestorEntrada.cargarDiccionario(config.getIdiomaEntrada()); // ESP

        // 3. Motor de Robert: leer, validar, ordenar, agrupar
        ProcesadorAgenda procesador = new ProcesadorAgenda();
        try {
            procesador.leerPeticiones("peticions.txt");
        } catch (java.io.FileNotFoundException e) {
            return;
        }
        java.util.ArrayList<Peticion> validas = procesador.validarPeticiones();
        java.util.ArrayList<Peticion> ordenadas = procesador.ordenarTancatPrimero(validas);
        java.util.ArrayList<Peticion> agrupadas = procesador.agruparPorActividad(ordenadas);

        // 4. Crear y rellenar la matriz de cada sala
        java.util.HashMap<String, String[][]> matrices = procesador.crearMatricesPorSala(agrupadas);
        for (String sala : matrices.keySet()) {
            java.util.ArrayList<Peticion> peticionesSala = procesador.filtrarPorSala(agrupadas, sala);
            procesador.rellenarMatriz(matrices.get(sala), peticionesSala, anio, mes, gestorEntrada, sala);
        }

        // 5. Escribir incidencias
        try {
            procesador.escribirIncidencias("incidencias.log");
        } catch (java.io.IOException e) { }

        // 6. Generar HTML de cada sala (modulo Simon), con diccionario de SALIDA
        GestionDatosTiempo gestorSalida = new GestionDatosTiempo();
        gestorSalida.cargarDiccionario(config.getIdiomaSalida()); // ENG
        for (String sala : matrices.keySet()) {
            GeneradorHTML.generarArchivoSala(sala, matrices.get(sala), config, gestorSalida);
        }
    }
}
