import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorHTML {

    public static void generarArchivoHTML(String nomEspai, String[][][] matrizSala, int totalSemanas, String idiomaSortida) {
        String nombreArchivo = nomEspai + ".html";
        boolean esIngles = idiomaSortida.equalsIgnoreCase("ENG");
        
        
        String textoBloqueo = esIngles ? "Closed" : "Cerrado";
        String tituloSemana = esIngles ? "Week " : "Semana ";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            bw.write("<html>\n<head>\n<title>" + nomEspai + "</title>\n");
            bw.write("<style>\n");
            bw.write("  table { border-collapse: collapse; width: 100%; margin-bottom: 30px; font-family: sans-serif; }\n");
            bw.write("  th, td { border: 1px solid #ccc; padding: 6px; text-align: center; height: 25px; }\n");
            bw.write("  th { background-color: #f2f2f2; }\n");
            bw.write("</style>\n</head>\n<body>\n");
            bw.write("<h1>" + nomEspai.toUpperCase() + "</h1>\n");

            
            for (int s = 0; s < totalSemanas; s++) {
                bw.write("<h2>" + tituloSemana + (s + 1) + "</h2>\n");
                bw.write("<table>\n");
                
                
                bw.write("  <tr>\n    <th>" + (esIngles ? "Hour / Day" : "Hora / Dia") + "</th>\n");
                String[] dias = esIngles ? 
                    new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"} :
                    new String[]{"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
                
                for (String dia : dias) {
                    bw.write("    <th>" + dia + "</th>\n");
                }
                bw.write("  </tr>\n");

                
                for (int h = 0; h < 24; h++) {
                    String horaInicio = String.format("%02d", h);
                    String horaFin = String.format("%02d", h + 1);
                    bw.write("  <tr>\n    <td>" + horaInicio + "-" + horaFin + "</td>\n");

                    
                    for (int d = 0; d < 7; d++) {
                        String actividad = matrizSala[s][d][h];

                        
                        if (actividad != null && (actividad.equalsIgnoreCase("Tancat") || actividad.equalsIgnoreCase("Cerrado") || actividad.equalsIgnoreCase("Closed"))) {
                            bw.write("    <td style='background-color: #808080; color: #ffffff; font-weight: bold;'>" + textoBloqueo + "</td>\n");
                        } 
                        
                        else if (actividad != null && !actividad.isEmpty()) {
                            bw.write("    <td>" + actividad + "</td>\n");
                        } 
                       
                        else {
                            bw.write("    <td></td>\n");
                        }
                    }
                    bw.write("  </tr>\n");
                }
                bw.write("</table>\n");
            }

            bw.write("</body>\n</html>");

        } catch (IOException e) {
           
        }
    }
}
