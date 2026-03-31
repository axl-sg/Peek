import javax.swing.JOptionPane;
import java.util.ArrayList;

public class RegistroPerros {

    public void registrarPerro(ArrayList<Perro> perros, ManejadorArchivos manejador) {
        String nombre = solicitarDato("Nombre del perro:");
        if (nombre == null) {
            return;
        }
        
        String raza = solicitarDato("Raza:");
        if (raza == null) {
            return;
        }
        
        String dueño = solicitarDato("Nombre del dueño:");
        if (dueño == null) {
            return;
        }
        
        String codigoPostal = solicitarDato("Código postal:");
        if (codigoPostal == null) {
            return;
        }
        
        String edadStr = solicitarDato("Edad de : "  + nombre);
        if (edadStr == null) {
            return;
        }
        
        try {
            int edad = Integer.parseInt(edadStr);
            perros.add(new Perro(nombre, raza, edad, dueño, codigoPostal));
            manejador.guardarPerros(perros);
            JOptionPane.showMessageDialog(null, "Perro registrado exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La edad debe ser un número válido.");
        }
    }

    public Perro seleccionarPerro(ArrayList<Perro> perros) {
        if (perros.size() == 0) {
            JOptionPane.showMessageDialog(null, "No hay perros registrados.");
            return null;
        }

        String[] nombres = new String[perros.size()];
        for (int i = 0; i < perros.size(); i++) {
            nombres[i] = perros.get(i).getNombre();
        }
        
        String nombreSeleccionado = (String) JOptionPane.showInputDialog(null, "Selecciona un perro:",
                "Lista de Perros", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

        if (nombreSeleccionado == null) {
            return null;
        }

        // Buscar el perro por nombre
        for (int i = 0; i < perros.size(); i++) {
            Perro p = perros.get(i);
            if (p.getNombre().equals(nombreSeleccionado)) {
                return p;
            }
        }
        
        return null;
    }

    private String solicitarDato(String mensaje) {
        String dato = JOptionPane.showInputDialog(mensaje);
        if (dato != null && !dato.trim().isEmpty()) {
            return dato;
        } else {
            return null;
        }
    }
}