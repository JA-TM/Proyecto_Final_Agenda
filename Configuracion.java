public class Configuracion {
    private int anio;
    private int mes;
    private String idiomaEntrada;
    private String idiomaSalida;

    // Constructor: Soporte de los elementos a rellenar
    public Configuracion(int anio, int mes, String idiomaEntrada, String idiomaSalida) {
        this.anio = anio;
        this.mes = mes;
        this.idiomaEntrada = idiomaEntrada;
        this.idiomaSalida = idiomaSalida;
    }

    // Getters: Puerta pra que puedan verificarse los datos
    public int getAnio() { return anio; }
    public int getMes () { return mes; }
    public String getIdiomaEntrada() { return idiomaEntrada; }
    public String getIdiomaSalida() { return idiomaSalida; }
}
