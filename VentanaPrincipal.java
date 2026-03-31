import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {
    private ArrayList<Perro> perros;
    private ManejadorArchivos manejadorArchivos;
    private RegistroPerros registroPerros;
    private GestorServicios gestorServicios;

    public VentanaPrincipal() {
        // Inicialización
        manejadorArchivos = new ManejadorArchivos();
        registroPerros = new RegistroPerros();
        gestorServicios = new GestorServicios();
        perros = manejadorArchivos.cargarPerros();

        // Configuración de la ventana
        setTitle(" Gestión de Servicios para Perros - PEEK");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(47, 52, 73)); // Fondo azul
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Agregar título descriptivo
        JLabel titulo = new JLabel("<html><center>🐕 SISTEMA DE GESTIÓN<br>Control de Horarios Integrado</center></html>");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setHorizontalAlignment(SwingConstants.CENTER); // Centrado horizontal adicional
        add(Box.createVerticalStrut(20));
        add(titulo);
        add(Box.createVerticalStrut(15));

        // Crear botones amarillos
        JButton btnRegistrarPerro = crearBoton("Registrar Perro");
        JButton btnRegistrarServicio = crearBoton("Registrar Servicio");
        JButton btnActualizarServicios = crearBoton("Actualizar Servicios");
        JButton btnConsultarServicios = crearBoton("Consultar Servicios");
        JButton btnSalir = crearBoton("Salir");

        // Acciones
        btnRegistrarPerro.addActionListener(e -> {
            registroPerros.registrarPerro(perros, manejadorArchivos);
        });

        btnRegistrarServicio.addActionListener(e -> {
            gestorServicios.registrarServicio(perros, registroPerros, manejadorArchivos);
        });

        btnActualizarServicios.addActionListener(e -> {
            gestorServicios.actualizarServicios(perros, registroPerros, manejadorArchivos);
        });

        btnConsultarServicios.addActionListener(e -> {
            gestorServicios.consultarServicios(perros, registroPerros);
        });

        btnSalir.addActionListener(e -> {
            manejadorArchivos.guardarPerros(perros);
            JOptionPane.showMessageDialog(this, 
                "✅ Datos guardados exitosamente.\nCerrando sistema...", 
                "Salir del Sistema", 
                JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });

        // Agregar botones centrados y con tamaño definido
        add(crearPanelBoton(btnRegistrarPerro));
        add(crearPanelBoton(btnRegistrarServicio));
        add(crearPanelBoton(btnActualizarServicios));
        add(crearPanelBoton(btnConsultarServicios));
        add(Box.createVerticalStrut(10));
        add(crearPanelBoton(btnSalir));
    }

    // Crea botones con color y estilo mejorado
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(255, 255, 102)); // Amarillo claro
        boton.setForeground(Color.BLACK);              // Texto negro
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setPreferredSize(new Dimension(250, 40)); // Tamaño original
        
        // Efecto hover mejorado
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(255, 255, 150)); // Amarillo más claro al pasar mouse
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(255, 255, 102)); // Volver al amarillo original
            }
        });
        
        return boton;
    }

    // Crea un panel que centra el botón
    private JPanel crearPanelBoton(JButton boton) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false); // Para que se vea el fondo azul
        panel.add(boton);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Mejorar la ventana de login
            JPanel loginPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            JLabel userLabel = new JLabel("Usuario:");
            JTextField userField = new JTextField(15);
            JLabel passLabel = new JLabel("Contraseña:");
            JPasswordField passField = new JPasswordField(15);
            
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0; gbc.gridy = 0;
            loginPanel.add(userLabel, gbc);
            gbc.gridx = 1;
            loginPanel.add(userField, gbc);
            gbc.gridx = 0; gbc.gridy = 1;
            loginPanel.add(passLabel, gbc);
            gbc.gridx = 1;
            loginPanel.add(passField, gbc);
            
            int result = JOptionPane.showConfirmDialog(null, loginPanel, 
                "🔐 Acceso al Sistema - Control de Horarios", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String user = userField.getText();
                String pass = new String(passField.getPassword());
                
                if (user != null && pass != null && user.equals("PEEK") && pass.equals("adm")) {
                    JOptionPane.showMessageDialog(null, 
                        "✅ Acceso autorizado\n🐕 Bienvenido a Peek", 
                        "Acceso Concedido", 
                        JOptionPane.INFORMATION_MESSAGE);
                    VentanaPrincipal ventana = new VentanaPrincipal();
                    ventana.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "❌ Credenciales incorrectas\n🚫 Acceso denegado", 
                        "Error de Autenticación", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}