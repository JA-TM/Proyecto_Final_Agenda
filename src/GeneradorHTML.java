import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * PROYECTO g_agenda - GRUPO ROSIJO
 * MÓDULO: GeneradorHTML (Simon)
 * 
 * Clase encargada de transformar las matrices de ocupación en múltiples
 * ficheros HTML semanales independientes de forma dinámica y adaptada al idioma.
 */
public class GeneradorHTML {

    /**
     * MÉTODO OFICIAL DE EXPORTACIÓN VISUAL
     * Procesa la matriz de una sala y genera su correspondiente reporte web.
     */
    public static void generarArchivoSala(String nomEspai, String[][] matrizMes, Configuracion config, GestionDatosTiempo gestor) {
        String nombreArchivo = nomEspai + ".html";
        
        // TAREA A: Uso obligatorio de StringBuilder para optimizar Entrada/Salida en memoria RAM
        StringBuilder html = new StringBuilder(); 

        // Extracción dinámica de etiquetas usando los códigos oficiales del diccionario de Antonio
        String textoAgenda = gestor.getTraduccion("001"); // Título principal ("Schedule" / "Agenda")
        String textoCerrado = gestor.getTraduccion("007"); // Estado de bloqueo ("Closed" / "Cerrado")
        
        // Desglosar etiquetas globales del código 005 (Semana, Hora, Día) para los subtítulos
        String[] partesEtiquetas = gestor.getTraduccion("005").split(",");
        String labelSemana = partesEtiquetas[2].trim(); // Extrae la posición de "Week / Semana" de forma segura
        String labelHora = partesEtiquetas[4].trim();   // Extrae la posición de "Hour / Hora"
        String labelDia = partesEtiquetas[3].trim();    // Extrae la posición de "Day / Día"
        String labelHoraDia = labelHora + " / " + labelDia; // Construye "Hour / Day"

        // Desglosar nombres de meses (Código 004) y nombres de los días (Código 002)
        String[] mesesTraducidos = gestor.getTraduccion("004").split(",");
        String nombreMesActual = mesesTraducidos[config.getMes() - 1].trim();
        String[] diasTraducidos = gestor.getTraduccion("002").split(",");

        // ESTRUCTURA BASE HTML5: Forzamos UTF-8 para evitar que se rompan acentos o eñes en Windows/Mac
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>").append(textoAgenda).append(" - ").append(nomEspai).append("</title>\n");
        html.append("</head>\n<body style='font-family: Arial, sans-serif; padding: 20px;'>\n");
        html.append("    <h1>").append(textoAgenda).append(": ").append(nomEspai).append("</h1>\n");
        html.append("    <h2>").append(nombreMesActual).append(" ").append(config.getAnio()).append("</h2>\n");
        html.append("    <hr>\n");

        // Conexión nativa con las funciones de fecha reales de Antonio para calcular las dimensiones del mes
        int diasTotalesMes = GestionDatosTiempo.getDiasDelMes(config.getAnio(), config.getMes());
        int totalSemanas = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), diasTotalesMes) + 1;

        // BUCLE PRINCIPAL: Renderiza una tabla HTML independiente por cada semana del mes
        for (int s = 0; s < totalSemanas; s++) {
            html.append("    <h3>").append(labelSemana).append(" ").append(s + 1).append("</h3>\n");
            html.append("    <table border='1' style='border-collapse: collapse; text-align: center; width: 100%; margin-bottom: 30px;'>\n");
            
            // Fila de encabezado con los días traducidos dinámicamente
            html.append("        <tr style='background-color: #f2f2f2; font-weight: bold;'>\n");
            // PASO 2: Anchura fija de columnas al 12% para garantizar simetría visual y evitar deformaciones
            html.append("            <th style='width: 12%;'>").append(labelHoraDia).append("</th>\n");
            for (int d = 0; d < diasTraducidos.length; d++) {
                html.append("            <th style='width: 12%;'>").append(diasTraducidos[d].trim()).append("</th>\n");
            }
            html.append("        </tr>\n");

            // AUDITORÍA: BUCLE VERTICAL (Recorre las 24 horas del día - Filas)
            for (int hora = 0; hora < 24; hora++) {
                html.append("        <tr>\n");
                String rangoHora = String.format("%02d-%02d", hora, hora + 1);
                html.append("            <td style='font-weight: bold; background-color: #fafafa;'>").append(rangoHora).append("</td>\n");
                
                // AUDITORÍA: BUCLE HORIZONTAL (Recorre los 7 días de la semana - Columnas del 1 al 7)
                for (int diaSemana = 1; diaSemana <= 7; diaSemana++) {
                    
                    // Buscador matemático real integrado (Año, Mes, Día) utilizando las funciones de Antonio
                    int diaRealEncontrado = 0;
                    for (int d = 1; d <= diasTotalesMes; d++) {
                        int filaSemana = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), d);
                        int colSemana = GestionDatosTiempo.getDiaDeLaSemana(config.getAnio(), config.getMes(), d);
                        
                        if (filaSemana == s && colSemana == diaSemana) {
                            diaRealEncontrado = d;
                            break;
                        }
                    }

                    // PASO 2: Protección extrema contra desajustes del calendario o días fuera de rango
                    if (diaRealEncontrado == 0 || diaRealEncontrado > diasTotalesMes) {
                        html.append("            <td style='background-color: #fdfdfd;'>&nbsp;</td>\n");
                    } else {
                        String actividad = matrizMes[hora][diaRealEncontrado];

                        // PASO 2: Filtro de seguridad para celdas nulas o textos vacíos de Robert (.trim())
                        if (actividad == null || actividad.trim().isEmpty()) {
                            html.append("            <td>&nbsp;</td>\n");
                        } 
                        // AUDITORÍA CSS: Bloqueo Gris para actividad especial "Tancat" (#b2aaaa)
                        else if (actividad.equalsIgnoreCase("Tancat")) {
                            html.append("            <td style='background-color: #b2aaaa; color: black; font-weight: bold;'>")
                                .append(textoCerrado).append("</td>");
                        } 
                        // AUDITORÍA CSS: Destacado azul para reuniones normales (textos cortos, largos o números)
                        else {
                            html.append("            <td style='background-color: #e6f7ff; color: #0050b3; font-weight: normal;'>")
                                .append(actividad.trim()).append("</td>\n");
                        }
                    }
                }
                html.append("        </tr>\n");
            }
            html.append("    </table>\n");
        }

        html.append("</body>\n</html>\n");

        // Escritura física directa del documento web unificado desde el StringBuilder en RAM
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(html.toString());
            System.out.println("¡Éxito! Archivo real generado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error crítico de escritura en el archivo HTML: " + e.getMessage());
        }
    }
}
