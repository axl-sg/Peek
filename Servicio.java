import java.time.LocalDate;
import java.time.LocalTime;

public class Servicio {
    private String tipo;
    private int numero;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    // Constructor para Paseo (con fecha y hora)
    public Servicio(String tipo, int numero, LocalDate fecha, LocalTime hora) {
        this.tipo = tipo;
        this.numero = numero;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = "Pendiente";
    }

    // Constructor para Entrenamiento (sin fecha ni hora)
    public Servicio(String tipo, int numero) {
        this.tipo = tipo;
        this.numero = numero;
        this.fecha = null;
        this.hora = null;
        this.estado = "Pendiente";
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    // Setters
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Métodos específicos
    public String toString() {
        if (tipo.equals("Paseo")) {
            return tipo + " #" + numero + " - " + fecha + " a las " + hora + " (" + estado + ")";
        } else {
            return tipo + " #" + numero + " (" + estado + ")";
        }
    }

    public String toArchivo() {
        if (tipo.equals("Paseo")) {
            return tipo + "-" + numero + "-" + fecha + "-" + hora + "-" + estado;
        } else {
            return tipo + "-" + numero + "-" + estado;
        }
    }
}