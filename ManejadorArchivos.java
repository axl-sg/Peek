import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ManejadorArchivos {
    private static final String ARCHIVO = "perros.txt";

    public ArrayList<Perro> cargarPerros() {
        ArrayList<Perro> perros = new ArrayList<Perro>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO));
            String linea;
            
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("::");
                if (partes.length < 5) {
                    continue; // nombre::raza::edad::dueño::codigoPostal::servicios
                }

                Perro p = new Perro(partes[0], partes[1], Integer.parseInt(partes[2]), partes[3], partes[4]);

                if (partes.length > 5 && !partes[5].trim().isEmpty()) {
                    String[] servicios = partes[5].split("\\|");
                    
                    for (int i = 0; i < servicios.length; i++) {
                        String s = servicios[i];
                        if (s.trim().isEmpty()) {
                            continue;
                        }
                        
                        String[] datos = s.split("-");
                        if (datos.length == 0) {
                            continue;
                        }

                        if (datos[0].equals("Paseo") && datos.length >= 5) {
                            LocalDate fecha = parsearFecha(datos[2]);
                            LocalTime hora = parsearHora(datos[3]);
                            Servicio ser = new Servicio("Paseo", Integer.parseInt(datos[1]), fecha, hora);
                            ser.setEstado(datos[4]);
                            p.agregarServicio(ser);
                        } else if (datos[0].equals("Entrenamiento") && datos.length >= 3) {
                            Servicio ser = new Servicio("Entrenamiento", Integer.parseInt(datos[1]));
                            ser.setEstado(datos[2]);
                            p.agregarServicio(ser);
                        }
                    }
                }
                perros.add(p);
            }
            br.close();
        } catch (IOException e) {
            // Archivo no existe, se creará al guardar
        }
        
        return perros;
    }

    public void guardarPerros(ArrayList<Perro> perros) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO));
            
            for (int i = 0; i < perros.size(); i++) {
                Perro p = perros.get(i);
                pw.println(p.toArchivo());
            }
            pw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar perros.");
        }
    }

    private LocalDate parsearFecha(String fechaTexto) {
        try {
            return LocalDate.parse(fechaTexto);
        } catch (DateTimeParseException e) {
            // Si es solo un año
            if (fechaTexto.matches("\\d{4}")) {
                return LocalDate.of(Integer.parseInt(fechaTexto), 1, 1);
            }
            return LocalDate.now();
        }
    }

    private LocalTime parsearHora(String horaTexto) {
        try {
            return LocalTime.parse(horaTexto);
        } catch (DateTimeParseException e) {
            // Si es solo una hora
            if (horaTexto.matches("\\d{1,2}")) {
                int h = Integer.parseInt(horaTexto);
                if (h >= 0 && h <= 23) {
                    return LocalTime.of(h, 0);
                } else {
                    return LocalTime.of(15, 0);
                }
            }
            return LocalTime.of(15, 0);
        }
    }
}