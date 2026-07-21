import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeneradorHTML {

    public static void generarArchivoSala(String nomEspai, String[][] matrizMes, Configuracion config, GestionDatosTiempo gestor) {
        String nombreArchivo = nomEspai + ".html";
        StringBuilder html = new StringBuilder();

        String textoAgenda = gestor.getTraduccion("001");
        String textoCerrado = gestor.getTraduccion("007");
        String[] etiquetas = gestor.getTraduccion("005").split(",");
        String labelSemana = etiquetas[2].trim();
        String[] meses = gestor.getTraduccion("004").split(",");
        String nombreMes = meses[config.getMes() - 1].trim();
        String[] dias = gestor.getTraduccion("002").split(",");

        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>").append(textoAgenda).append(" - ").append(nomEspai).append("</title>\n");
        html.append("</head>\n<body style='font-family: Arial, sans-serif; padding: 20px;'>\n");
        html.append("    <h1>").append(textoAgenda).append(": ").append(nomEspai).append("</h1>\n");
        html.append("    <h2>").append(nombreMes).append(" ").append(config.getAnio()).append("</h2>\n");
        html.append("    <hr>\n");

        int diasTotales = GestionDatosTiempo.getDiasDelMes(config.getAnio(), config.getMes());
        int totalSemanas = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), diasTotales) + 1;

        for (int s = 0; s < totalSemanas; s++) {
            html.append("    <h3>").append(labelSemana).append(" ").append(s + 1).append("</h3>\n");
            html.append("    <table border='1' style='border-collapse: collapse; text-align: center; width: 100%; margin-bottom: 30px;'>\n");
            html.append("        <tr style='background-color: #f2f2f2; font-weight: bold;'>\n");
            html.append("            <th style='width: 12%;'>").append(labelSemana).append(" / ").append(etiquetas[3].trim()).append("</th>\n");
            for (int d = 0; d < dias.length; d++) {
                html.append("            <th style='width: 12%;'>").append(dias[d].trim()).append("</th>\n");
            }
            html.append("        </tr>\n");

            for (int hora = 0; hora < 24; hora++) {
                html.append("        <tr>\n");
                String rango = String.format("%02d-%02d", hora, hora + 1);
                html.append("            <td style='font-weight: bold; background-color: #fafafa;'>").append(rango).append("</td>\n");

                for (int col = 1; col <= 7; col++) {
                    int diaReal = 0;
                    for (int d = 1; d <= diasTotales; d++) {
                        int fila = GestionDatosTiempo.getFilaSemanaMes(config.getAnio(), config.getMes(), d);
                        int columna = GestionDatosTiempo.getDiaDeLaSemana(config.getAnio(), config.getMes(), d);
                        if (fila == s && columna == col) { diaReal = d; break; }
                    }

                    if (diaReal == 0) {
                        html.append("            <td style='background-color: #fdfdfd;'>&nbsp;</td>\n");
                    } else {
                        String actividad = matrizMes[hora][diaReal];
                        if (actividad == null || actividad.trim().isEmpty()) {
                            html.append("            <td>&nbsp;</td>\n");
                        } else if (actividad.equalsIgnoreCase("Cerrado") || actividad.equalsIgnoreCase("Tancat") || actividad.equalsIgnoreCase("Closed")) {
                            html.append("            <td style='background-color: #b2aaaa; color: black; font-weight: bold;'>").append(textoCerrado).append("</td>\n");
                        } else {
                            html.append("            <td style='background-color: #e6f7ff; color: #0050b3;'>").append(actividad.trim()).append("</td>\n");
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
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
