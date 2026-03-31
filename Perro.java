import java.util.ArrayList;

public class Perro {
    private String nombre;
    private String raza;
    private int edad;
    private String dueño;
    private String codigoPostal;
    private ArrayList<Servicio> servicios;

    // Constructor
    public Perro(String nombre, String raza, int edad, String dueño, String codigoPostal) {
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.dueño = dueño;
        this.codigoPostal = codigoPostal;
        this.servicios = new ArrayList<Servicio>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public int getEdad() {
        return edad;
    }

    public String getDueño() {
        return dueño;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setDueño(String dueño) {
        this.dueño = dueño;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    // Métodos específicos
    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
    }

    public String toArchivo() {
        String resultado = nombre + "::" + raza + "::" + edad + "::" + dueño + "::" + codigoPostal;
        
        if (servicios.size() > 0) {
            resultado += "::";
            for (int i = 0; i < servicios.size(); i++) {
                if (i > 0) {
                    resultado += "|";
                }
                resultado += servicios.get(i).toArchivo();
            }
        }
        
        return resultado;
    }
}