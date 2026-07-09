public class Main {

    public static void main(String[] args) {
        System.out.println("===== INICIO g_agenda ====");

        // *** Paso 01: Leer configuración (José Antonio) ***
        Configuracion config = GestionDatosTiempo.leerConfig("config.txt");
        if (config == null) {
            System.out.println("ERROR: no se pudo leer config.txt. Se detiene el programa.");
            return;
        } 
        System.out.println("Procesando: " + config.getMes() + "/" + config.getAnio());
        System.out.println("Idioma entrada: " + config.getIdiomaEntrada()
                        + " | Idioma salida: " + config.getIdiomaSalida());

        // *** Paso 02: Cargar diccionario de idioma (José Antonio) ***
        GestionDatosTiempo gestor = new GestionDatosTiempo();
        gestor.cargarDiccionario(config.getIdiomaEntrada()); // ESP (para leer la entrada)
        gestor.cargarDiccionario(config.getIdiomaSalida());  // ENG (para escribir la salida)
        System.out.println("Diccionarios cargados. Titulo de salida: " + gestor.getTraduccion("001"));

        // *** Paso 03: Leer y procesar peticiones (Robert)***
        // ProcesadorAgenda procesador = new ProcesadorAgenda();
        // procesador.leerPeticiones("peticiones.txt");
        // procesador.ordenarYProcesar(config, gestor);
        System.out.println("[PASO 3 pendiente: motor de Robert]");

        // *** Paso 04: Generar los HTML de cada sala (Simon)***
        // GeneradorHTML.generarTodasLasSalas(procesador.getMatrices(), config, gestor);
        System.out.println("[PASO 4 pendiente: HTML de Simon]");

        System.out.println("==== Fin g_agenda ====");
    }
}
