package generadorCarga;

/**
 * Created by sebastian on 5/1/15.
 */
public class Medida {

    private long tiempoTotal;
    private long tiempoAutenticacion;
    private String idCliente;

    public Medida(long tiempoAutenticacion, long tiempoTotal, String idCliente) {

        this.tiempoAutenticacion = tiempoAutenticacion;
        this.tiempoTotal = tiempoTotal;
        this.idCliente = idCliente;

    }

    public long getTiempoTotal() {
        return tiempoTotal;
    }

    public long getTiempoAutenticacion() {
        return tiempoAutenticacion;
    }

    public String getIdCliente() {
        return idCliente;
    }
}
