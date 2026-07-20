import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeneradorHTML {

    public static void generarArchivoSala(String nomEspai, String[][] matrizMes, Configuracion config, GestionDatosTiempo gestor) {
        String nombreArchivo = nomEspai + ".html";
        StringBuilder html = new StringBuilder(); 

        String textoAgenda = gestor.getTraduccion("001"); 
        String textoCerrado = gestor.getTraduccion("007"); 
        
        // CORRECCIÓN PUNTO 1: Índices del 0 al 3 alineados con las 4 palabras de Antonio (Año,Mes,Semana,Día)
        String[] partesEtiquetas = gestor.getTraduccion("005").split(",");
        String labelSemana = partesEtiquetas[2].trim(); // Posición 2 = Semana / Week
        String labelDia = partesEtiquetas[3].trim();    // Posición 3 = Día / Day
        
        // Usamos la traducción oficial del código 007 ("Cerrado"/"Closed") para la cabecera de la hora
        String labelHora = textoCerrado.equalsIgnoreCase("Closed") ? "Hour" : "Hora"; 
        String labelHoraDia = labelHora + " / " + labelDia;

        String[] mesesTraducidos = gestor.getTraduccion("004").split(",");
        String nombreMesActual = mesesTraducidos[config.getMes() - 1].trim();
        String[] diasTraducidos = gestor.getTraduccion("002").split(",");

        // PUNTO 3: Forzamos codificación UTF-8 nativa en el documento web
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>").append(textoAgenda).append(" - ").append(nomEspai).append("</title>\n");
        html.append("</head>\n<body style='font-family: Arial, sans-serif; padding: 20px;'>\n");
        html.append("    <h1>").append(textoAgenda).append(": ").append(nomEspai).append("</h1>\n");
        html.append("    <h2>").append(nombreMesActual).append(" ").append(config.getAnio()).append("</h2>\n");
        html.append("    <hr>\n");

        int diasTotalesMes = GestionDatosTiempo.getDiasDelMes(config.getAnio(), config.getMes());
        int totalSemanas = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), diasTotalesMes) + 1;

        for (int s = 0; s < totalSemanas; s++) {
            html.append("    <h3>").append(labelSemana).append(" ").append(s + 1).append("</h3>\n");
            html.append("    <table border='1' style='border-collapse: collapse; text-align: center; width: 100%; margin-bottom: 30px;'>\n");
            
            html.append("        <tr style='background-color: #f2f2f2; font-weight: bold;'>\n");
            html.append("            <th style='width: 12%;'>").append(labelHoraDia).append("</th>\n");
            for (int d = 0; d < diasTraducidos.length; d++) {
                html.append("            <th style='width: 12%;'>").append(diasTraducidos[d].trim()).append("</th>\n");
            }
            html.append("        </tr>\n");

            for (int hora = 0; hora < 24; hora++) {
                html.append("        <tr>\n");
                String rangoHora = String.format("%02d-%02d", hora, hora + 1);
                html.append("            <td style='font-weight: bold; background-color: #fafafa;'>").append(rangoHora).append("</td>\n");
                
                for (int diaSemana = 1; diaSemana <= 7; diaSemana++) {
                    int diaRealEncontrado = 0;
                    for (int d = 1; d <= diasTotalesMes; d++) {
                        int filaSemana = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), d);
                        int colSemana = GestionDatosTiempo.getDiaDeLaSemana(config.getAnio(), config.getMes(), d);
                        
                        if (filaSemana == s && colSemana == diaSemana) {
                            diaRealEncontrado = d;
                            break;
                        }
                    }

                    if (diaRealEncontrado == 0 || diaRealEncontrado > diasTotalesMes) {
                        html.append("            <td style='background-color: #fdfdfd;'>&nbsp;</td>\n");
                    } else {
                        String actividad = matrizMes[hora][diaRealEncontrado];

                        if (actividad == null || actividad.trim().isEmpty()) {
                            html.append("            <td>&nbsp;</td>\n");
                        } 
                        // CORRECCIÓN PUNTO 2: Sincronización estricta con la palabra del motor de Robert ("Cerrado")
                        else if (actividad.equalsIgnoreCase("Cerrado")) {
                            html.append("            <td style='background-color: #b2aaaa; color: black; font-weight: bold;'>")
                                .append(textoCerrado).append("</td>");
                        } else {
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

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(html.toString());
            System.out.println("¡Éxito! Archivo real generado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
