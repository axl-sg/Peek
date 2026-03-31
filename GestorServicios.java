import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class GestorServicios {

    public void registrarServicio(ArrayList<Perro> perros, RegistroPerros registro, ManejadorArchivos manejador) {
        boolean continuar;
        
        do {
            Perro perro = registro.seleccionarPerro(perros);
            if (perro == null) {
                return;
            }

            String[] tipos = {"Paseo", "Entrenamiento"};
            String tipo = (String) JOptionPane.showInputDialog(null, "Selecciona el tipo de servicio:",
                    "Tipo de Servicio", JOptionPane.PLAIN_MESSAGE, null, tipos, tipos[0]);

            if (tipo == null) {
                return;
            }

            if (tipo.equals("Paseo")) {
                registrarPaseos(perro, perros);
            } else {
                registrarEntrenamientos(perro);
            }

            manejador.guardarPerros(perros);
            continuar = JOptionPane.showConfirmDialog(null, "¿Desea registrar otro servicio?",
                    "Continuar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    
        } while (continuar);
    }

    private void registrarPaseos(Perro perro, ArrayList<Perro> todosLosPerros) {
        String fechaStr = solicitarDato("Fecha de inicio (AAAA-MM-DD):");
        String horaStr = solicitarDato("Hora de inicio (HH:MM, entre 15:00 y 20:00):");
        
        if (fechaStr != null && horaStr != null) {
            try {
                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalTime hora = LocalTime.parse(horaStr);
                
                // Validar horario entre 15:00 y 20:00
                if (hora.isBefore(LocalTime.of(15, 0)) || hora.isAfter(LocalTime.of(20, 0))) {
                    JOptionPane.showMessageDialog(null, "La hora debe estar entre 15:00 y 20:00.");
                    return;
                }
                
                // Validar conflictos de horario
                if (tieneConflictoHorario(todosLosPerros, fecha, hora)) {
                    JOptionPane.showMessageDialog(null, 
                        "CONFLICTO DE HORARIO \n\n" +
                        "Ya existe un paseo programado a las " + hora + " durante el período solicitado.\n" +
                        "Por favor, seleccione una hora diferente.\n\n" +
                        "Horarios disponibles: cualquier hora entre 15:00 y 20:00 que no esté ocupada.",
                        "Error - Horario Ocupado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Si no hay conflictos, registrar los paseos
                for (int i = 0; i < 7; i++) {
                    perro.agregarServicio(new Servicio("Paseo", i + 1, fecha.plusDays(i), hora));
                }
                JOptionPane.showMessageDialog(null, 
                    " Servicios de paseo registrados exitosamente.\n\n" +
                    "Período: " + fecha + " al " + fecha.plusDays(6) + "\n" +
                    "Hora: " + hora + "\n" +
                    "Perro: " + perro.getNombre());
                    
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de fecha u hora inválido.");
            }
        }
    }
    
    /**
     * Verifica si existe un conflicto de horario para los paseos en las fechas propuestas
     */
    private boolean tieneConflictoHorario(ArrayList<Perro> todosLosPerros, LocalDate fechaInicio, LocalTime horaInicio) {
        // Crear lista de fechas del paquete propuesto (7 días)
        ArrayList<LocalDate> fechasPropuestas = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fechasPropuestas.add(fechaInicio.plusDays(i));
        }
        
        // Verificar cada perro y sus servicios
        for (Perro perro : todosLosPerros) {
            for (Servicio servicio : perro.getServicios()) {
                // Solo verificar paseos (los entrenamientos no tienen hora específica)
                if (servicio.getTipo().equals("Paseo") && servicio.getFecha() != null && servicio.getHora() != null) {
                    // Si la hora coincide y la fecha está en el rango propuesto
                    if (servicio.getHora().equals(horaInicio) && fechasPropuestas.contains(servicio.getFecha())) {
                        return true; // Hay conflicto
                    }
                }
            }
        }
        return false; // No hay conflicto
    }
    
    /**
     * Muestra los horarios ocupados para una fecha específica (método auxiliar para debugging)
     */
    private void mostrarHorariosOcupados(ArrayList<Perro> todosLosPerros, LocalDate fecha) {
        ArrayList<LocalTime> horariosOcupados = new ArrayList<>();
        
        for (Perro perro : todosLosPerros) {
            for (Servicio servicio : perro.getServicios()) {
                if (servicio.getTipo().equals("Paseo") && 
                    servicio.getFecha() != null && 
                    servicio.getFecha().equals(fecha) && 
                    servicio.getHora() != null) {
                    horariosOcupados.add(servicio.getHora());
                }
            }
        }
        
        if (!horariosOcupados.isEmpty()) {
            String mensaje = "Horarios ocupados para " + fecha + ":\n";
            for (LocalTime hora : horariosOcupados) {
                mensaje += "- " + hora + "\n";
            }
            JOptionPane.showMessageDialog(null, mensaje, "Información de Horarios", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void registrarEntrenamientos(Perro perro) {
        for (int i = 0; i < 6; i++) {
            perro.agregarServicio(new Servicio("Entrenamiento", i + 1));
        }
        JOptionPane.showMessageDialog(null, "Servicios de entrenamiento registrados exitosamente.");
    }

    public void actualizarServicios(ArrayList<Perro> perros, RegistroPerros registro, ManejadorArchivos manejador) {
        boolean continuar;
        
        do {
            Perro perro = registro.seleccionarPerro(perros);
            if (perro == null) {
                return;
            }

            ArrayList<Servicio> pendientes = obtenerServiciosPendientes(perro);

            if (pendientes.size() == 0) {
                JOptionPane.showMessageDialog(null, "No hay servicios pendientes.");
                continuar = JOptionPane.showConfirmDialog(null, "¿Desea actualizar servicios de otro perro?",
                        "Continuar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                continue;
            }

            Servicio servicioActualizado = seleccionarServicioPendiente(pendientes);
            
            if (servicioActualizado != null) {
                servicioActualizado.setEstado("Completado");
                JOptionPane.showMessageDialog(null, "Perro: " + perro.getNombre() + 
                        " (" + perro.getRaza() + ")\n- " + servicioActualizado.getTipo() + ": Completado");

                // Verificar si todos están completados
                if (todosServiciosCompletados(perro)) {
                    JOptionPane.showMessageDialog(null, " ¡FELICIDADES! \n\n" +
                            "Todos los servicios de " + perro.getNombre() + " han sido completados.\n" +
                            "El registro ha sido eliminado exitosamente.\n\n¡Excelente trabajo!");
                    perros.remove(perro);
                }
                manejador.guardarPerros(perros);
            }

            continuar = JOptionPane.showConfirmDialog(null, "¿Desea actualizar otro servicio?",
                    "Continuar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    
        } while (continuar);
    }

    private ArrayList<Servicio> obtenerServiciosPendientes(Perro perro) {
        ArrayList<Servicio> pendientes = new ArrayList<Servicio>();
        
        for (int i = 0; i < perro.getServicios().size(); i++) {
            Servicio s = perro.getServicios().get(i);
            if (s.getEstado().equals("Pendiente")) {
                pendientes.add(s);
            }
        }
        return pendientes;
    }

    private Servicio seleccionarServicioPendiente(ArrayList<Servicio> pendientes) {
        String[] lista = new String[pendientes.size()];
        for (int i = 0; i < pendientes.size(); i++) {
            lista[i] = pendientes.get(i).toString();
        }

        String servicioSeleccionado = (String) JOptionPane.showInputDialog(null,
                "Selecciona el servicio a completar:", "Actualizar Servicio",
                JOptionPane.PLAIN_MESSAGE, null, lista, lista[0]);

        if (servicioSeleccionado != null) {
            // Confirmación antes de actualizar
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de marcar este servicio como completado?\n" + servicioSeleccionado,
                    "Confirmar Actualización", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                for (int i = 0; i < lista.length; i++) {
                    if (lista[i].equals(servicioSeleccionado)) {
                        return pendientes.get(i);
                    }
                }
            }
        }
        return null;
    }

    private boolean todosServiciosCompletados(Perro perro) {
        for (int i = 0; i < perro.getServicios().size(); i++) {
            Servicio s = perro.getServicios().get(i);
            if (s.getEstado().equals("Pendiente")) {
                return false;
            }
        }
        return true;
    }

    public void consultarServicios(ArrayList<Perro> perros, RegistroPerros registro) {
        Perro perro = registro.seleccionarPerro(perros);
        if (perro == null) {
            return;
        }

        String mensaje = "";
        mensaje += "Servicios de ";
        mensaje += perro.getNombre();
        mensaje += ":\n";
        mensaje += "Dueño: ";
        mensaje += perro.getDueño();
        mensaje += "\n";
        mensaje += "Código Postal: ";
        mensaje += perro.getCodigoPostal();
        mensaje += "\n\n";

        if (perro.getServicios().size() == 0) {
            mensaje += "No hay servicios registrados.";
        } else {
            int[] contadores = {0, 0, 0, 0}; // totalPaseos, completadosPaseos, totalEntrenamientos, completadosEntrenamientos
            
            for (int i = 0; i < perro.getServicios().size(); i++) {
                Servicio s = perro.getServicios().get(i);
                if (s.getTipo().equals("Paseo")) {
                    contadores[0]++;
                    if (s.getEstado().equals("Completado")) {
                        contadores[1]++;
                    }
                } else {
                    contadores[2]++;
                    if (s.getEstado().equals("Completado")) {
                        contadores[3]++;
                    }
                }
                mensaje += "- ";
                mensaje += s.toString();
                mensaje += "\n";
            }
            
            mensaje += "\n--- PROGRESO ---\n";
            if (contadores[0] > 0) {
                mensaje += String.format("Paseos: %d/%d completados (%.1f%%)\n", 
                         contadores[1], contadores[0], (contadores[1] * 100.0) / contadores[0]);
            }
            if (contadores[2] > 0) {
                mensaje += String.format("Entrenamientos: %d/%d completados (%.1f%%)\n", 
                         contadores[3], contadores[2], (contadores[3] * 100.0) / contadores[2]);
            }
        }
        JOptionPane.showMessageDialog(null, mensaje);
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