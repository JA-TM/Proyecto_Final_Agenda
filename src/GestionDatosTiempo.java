import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class GestionDatosTiempo {

    // *** B) Leer config.txt ***
    public static Configuracion leerConfig(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea1 = br.readLine();
            if (linea1 == null) return null;
            String[] partes1 = linea1.trim().split("\\s+");
            int anio = Integer.parseInt(partes1[0]);
            int mes = Integer.parseInt(partes1[1]);

            String linea2 = br.readLine();
            if (linea2 == null) return null;
            String[] partes2 = linea2.trim().split("\\s+");
            String idiomaEntrada = partes2[0];
            String idiomaSalida = partes2[1];

            return new Configuracion(anio, mes, idiomaEntrada, idiomaSalida);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer config.txt: " + e.getMessage());
            return null;
        }
    }

    // *** C) Diccionario de Idiomas ***
    private HashMap<String, String> diccionario = new HashMap<>();

    public void cargarDiccionario(String idioma) {
        String rutaArchivo = "internacional." + idioma.toUpperCase();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.trim().startsWith("#")) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    diccionario.put(partes[0].trim(), partes[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar " + rutaArchivo + ": " + e.getMessage());
        }
    }

    public String getTraduccion(String codigo) {
        return diccionario.getOrDefault(codigo, "¡Codigo " + codigo + " no encontrado!");
    }

    // *** D) Calculador de Fechas ***
    public static int getDiasDelMes(int anio, int mes) {
        return LocalDate.of(anio, mes, 1).lengthOfMonth();
    }

    public static int getDiaDeLaSemana(int anio, int mes, int dia) {
        return LocalDate.of(anio, mes, dia).getDayOfWeek().getValue(); // 1=Lun...
    }

    public static int getFilaSemanaMes(int anio, int mes, int dia) {
        LocalDate primerDiaMes = LocalDate.of(anio, mes, 1);
        int desfase = primerDiaMes.getDayOfWeek().getValue() - 1;
        int posicionEnCuadricula = dia - 1 + desfase;
        return posicionEnCuadricula / 7;
    }

    // *** D2) Mapeo de letras de dia (para Robert) ***
    // Las letras se leen del diccionario (codigo 003), no van fijas en el codigo.
    // Ojo: ya NO son static, porque necesitan el diccionario del gestor.
    public String getLetraDia(int numeroDia) {
        String letras = getTraduccion("003");   // ESP -> "LMXJVSD" | CAT -> "LMCJVSG"
        return String.valueOf(letras.charAt(numeroDia - 1));
    }

    public boolean diaEstaEnMascara(String mascara, int numeroDia) {
        String letra = getLetraDia(numeroDia);
        return mascara.contains(letra);
    }

    // *** Prueba ***
    public static void main(String[] args) {
        Configuracion config = leerConfig("config.txt");
        System.out.println("Anio: " + config.getAnio());
        System.out.println("Mes: " + config.getMes());
        System.out.println("Idioma entrada: " + config.getIdiomaEntrada());
        System.out.println("Idioma salida: " + config.getIdiomaSalida());

        System.out.println("\n--- Prueba del diccionario ---");
        GestionDatosTiempo gestor = new GestionDatosTiempo();

        gestor.cargarDiccionario("ESP");
        System.out.println("007 en ESP = " + gestor.getTraduccion("007")); // Cerrado
        System.out.println("001 en ESP = " + gestor.getTraduccion("001")); // Agenda

        gestor.cargarDiccionario("ENG");
        System.out.println("007 en ENG = " + gestor.getTraduccion("007")); // Closed
        System.out.println("001 en ENG = " + gestor.getTraduccion("001")); // Schedule

        System.out.println("\n--- Prueba de fechas (Noviembre 2025) ---");
        int anio = 2025, mes = 11;
        System.out.println("Dias del mes: " + getDiasDelMes(anio, mes));
        System.out.println("Dia semana del 1: " + getDiaDeLaSemana(anio, mes, 1));
        System.out.println("Fila del dia 1: " + getFilaSemanaMes(anio, mes, 1));
        System.out.println("Fila del dia 3: " + getFilaSemanaMes(anio, mes, 3));
        System.out.println("Fila del dia 30: " + getFilaSemanaMes(anio, mes, 30));

        System.out.println("\n--- Prueba de mascara de dias ---");
        gestor.cargarDiccionario(config.getIdiomaEntrada());   // ESP: recargamos el idioma de ENTRADA
        System.out.println("Letras del diccionario (003): " + gestor.getTraduccion("003"));
        System.out.println("Letra del dia 3 (miercoles): " + gestor.getLetraDia(3));
        System.out.println("Letra del dia 1 (lunes): " + gestor.getLetraDia(1));
        System.out.println("Mascara LXV incluye miercoles (3)? " + gestor.diaEstaEnMascara("LXV", 3));
        System.out.println("Mascara LXV incluye martes (2)? " + gestor.diaEstaEnMascara("LXV", 2));
        System.out.println("Mascara LMXJVSD incluye domingo (7)? " + gestor.diaEstaEnMascara("LMXJVSD", 7));
    }
}
