import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * PROYECTO g_agenda - GRUPO ROSIJO
 * MÓDULO: GeneradorHTML (Simon)
 */
public class GeneradorHTML {

    public static void generarArchivoSala(String nomEspai, String[][] matrizMes, Configuracion config, GestionDatosTiempo gestor) {
        String nombreArchivo = nomEspai + ".html";
        StringBuilder html = new StringBuilder(); // TAREA A: Uso obligatorio de StringBuilder

        // Extracción de etiquetas dinámicas usando los códigos oficiales de Antonio
        String textoAgenda = gestor.getTraduccion("001"); // Título
        String textoCerrado = gestor.getTraduccion("007"); // Cerrado / Closed
        
        // Desglosar etiquetas globales del código 005 (Semana, Hora, Día)
        String labelSemana = gestor.getTraduccion("005").split(",")[2].trim(); 
        String labelHora = gestor.getTraduccion("005").split(",")[3].trim();   
        String labelDia = gestor.getTraduccion("005").split(",")[4].trim();    
        String labelHoraDia = labelHora + " / " + labelDia;

        // Desglosar nombres de meses y de los días
        String[] mesesTraducidos = gestor.getTraduccion("004").split(",");
        String nombreMesActual = mesesTraducidos[config.getMes() - 1].trim();
        String[] diasTraducidos = gestor.getTraduccion("002").split(",");

        // Construcción de la cabecera HTML5
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>").append(textoAgenda).append(" - ").append(nomEspai).append("</title>\n");
        html.append("</head>\n<body style='font-family: Arial, sans-serif; padding: 20px;'>\n");
        html.append("    <h1>").append(textoAgenda).append(": ").append(nomEspai).append("</h1>\n");
        html.append("    <h2>").append(nombreMesActual).append(" ").append(config.getAnio()).append("</h2>\n");
        html.append("    <hr>\n");

        // CORRECCIÓN: Nombres de métodos y orden de variables (año, mes) según documento oficial
        int diasTotalesMes = GestionDatosTiempo.getDiasDelMes(config.getAnio(), config.getMes());
        int totalSemanas = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), diasTotalesMes) + 1;

        // BUCLE PRINCIPAL: Tablas semanales independientes
        for (int s = 0; s < totalSemanas; s++) {
            html.append("    <h3>").append(labelSemana).append(" ").append(s + 1).append("</h3>\n");
            html.append("    <table border='1' style='border-collapse: collapse; text-align: center; width: 100%; margin-bottom: 30px;'>\n");
            
            // Fila de encabezado internacionalizado
            html.append("        <tr style='background-color: #f2f2f2; font-weight: bold;'>\n");
            html.append("            <th style='width: 12%;'>").append(labelHoraDia).append("</th>\n");
            for (int d = 0; d < diasTraducidos.length; d++) {
                html.append("            <th style='width: 12%;'>").append(diasTraducidos[d].trim()).append("</th>\n");
            }
            html.append("        </tr>\n");

            // BUCLE VERTICAL: Recorre las 24 horas (Filas)
            for (int hora = 0; hora < 24; hora++) {
                html.append("        <tr>\n");
                String rangoHora = String.format("%02d-%02d", hora, hora + 1);
                html.append("            <td style='font-weight: bold; background-color: #fafafa;'>").append(rangoHora).append("</td>\n");
                
                // BUCLE HORIZONTAL: Recorre los 7 días de la semana (Columnas del 1 al 7)
                for (int diaSemana = 1; diaSemana <= 7; diaSemana++) {
                    
                    // Buscador matemático real integrado (Año, Mes, Día)
                    int diaRealEncontrado = 0;
                    for (int d = 1; d <= diasTotalesMes; d++) {
                        // CORRECCIÓN: Métodos con orden de variables real (año, mes, día)
                        int filaSemana = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), d);
                        int colSemana = GestionDatosTiempo.getDiaDeLaSemana(config.getAnio(), config.getMes(), d); // S mayúscula
                        
                        if (filaSemana == s && colSemana == diaSemana) {
                            diaRealEncontrado = d;
                            break;
                        }
                    }

                    // Celda vacía para desajustes del mes
                    if (diaRealEncontrado == 0 || diaRealEncontrado > diasTotalesMes) {
                        html.append("            <td style='background-color: #fdfdfd;'>&nbsp;</td>\n");
                    } else {
                        String actividad = matrizMes[hora][diaRealEncontrado];

                        if (actividad == null || actividad.trim().isEmpty()) {
                            html.append("            <td>&nbsp;</td>\n");
                        } 
                        // Actividad especial "Tancat" se pinta en gris (#b2aaaa)
                        else if (actividad.equalsIgnoreCase("Tancat")) {
                            html.append("            <td style='background-color: #b2aaaa; color: black; font-weight: bold;'>")
                                .append(textoCerrado).append("</td>");
                        } 
                        // Reuniones estándar se destacan en azul (#e6f7ff)
                        else {
                            html.append("            <td style='background-color: #e6f7ff; color: #0050b3;'>").append(actividad).append("</td>\n");
                        }
                    }
                }
                html.append("        </tr>\n");
            }
            html.append("    </table>\n");
        }

        html.append("</body>\n</html>\n");

        // Escritura física directa del archivo web final
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(html.toString());
            System.out.println("¡Éxito! Archivo real generado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error crítico de escritura: " + e.getMessage());
        }
    }
}
