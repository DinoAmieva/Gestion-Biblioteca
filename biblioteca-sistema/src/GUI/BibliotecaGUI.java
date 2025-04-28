package GUI;

import Modelo.Biblioteca;
import Modelo.Libro;
import Modelo.Prestamo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BibliotecaGUI extends JFrame {
    // Definición de colores corporativos
    private static final Color COLOR_PRIMARY = new Color(47, 54, 64);    // Azul oscuro
    private static final Color COLOR_SECONDARY = new Color(70, 80, 95);  // Azul grisáceo
    private static final Color COLOR_ACCENT = new Color(86, 101, 115);   // Gris azulado
    private static final Color COLOR_BACKGROUND = new Color(245, 246, 250); // Gris muy claro
    private static final Color COLOR_TEXT = new Color(47, 54, 64);       // Texto oscuro
    private static final Font FONT_MAIN = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    private static final String DIRECTORIO_REPORTES = "reportes/";

    private final Biblioteca biblioteca;
    private JTabbedPane tabbedPane;
    private JTable tablaLibros;
    private DefaultTableModel modeloTablaLibros;
    private JTextField txtTitulo, txtAutor, txtIsbn, txtAño;
    private JTextField txtBusqueda;
    private JComboBox<String> comboCriterioBusqueda;
    private JTextField txtEditorial;
    private JComboBox<String> comboCategory;

    public BibliotecaGUI() {
        biblioteca = new Biblioteca();
        configurarVentana();
        inicializarComponentes();
        aplicarEstilos();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BACKGROUND);
    }

    private void aplicarEstilos() {
        // Aplicar estilo al TabbedPane
        tabbedPane.setFont(FONT_TITLE);
        tabbedPane.setBackground(COLOR_BACKGROUND);
        tabbedPane.setForeground(COLOR_TEXT);
        
        // Estilo para todos los componentes
        UIManager.put("Button.background", COLOR_PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", FONT_MAIN);
        UIManager.put("Label.font", FONT_MAIN);
        UIManager.put("TextField.font", FONT_MAIN);
        UIManager.put("ComboBox.font", FONT_MAIN);
        UIManager.put("Table.font", FONT_MAIN);
        UIManager.put("TableHeader.font", FONT_TITLE);
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(COLOR_BACKGROUND);
        
        // Panel de Gestión de Libros
        tabbedPane.addTab("Gestión de Libros", crearPanelGestionLibros());
        
        // Panel de Préstamos
        tabbedPane.addTab("Préstamos y Devoluciones", crearPanelPrestamos());
        
        // Panel de Reportes
        tabbedPane.addTab("Reportes", crearPanelReportes());
        
        add(tabbedPane);
    }

    private JPanel crearPanelGestionLibros() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de entrada de datos
        JPanel panelEntrada = new JPanel(new GridLayout(7, 2, 10, 10));
        panelEntrada.setBackground(COLOR_BACKGROUND);
        panelEntrada.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "Datos del Libro", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                FONT_TITLE, COLOR_TEXT),
            new EmptyBorder(10, 10, 10, 10)));
        
        txtTitulo = crearTextField();
        txtAutor = crearTextField();
        txtIsbn = crearTextField();
        txtAño = crearTextField();
        txtEditorial = crearTextField();
        comboCategory = new JComboBox<>(new String[]{"Ficción", "No Ficción", "Ciencia", "Historia", "Biografía", "Infantil"});
        comboCategory.setPreferredSize(new Dimension(200, 30));
        
        panelEntrada.add(crearLabel("Título:"));
        panelEntrada.add(txtTitulo);
        panelEntrada.add(crearLabel("Autor:"));
        panelEntrada.add(txtAutor);
        panelEntrada.add(crearLabel("ISBN:"));
        panelEntrada.add(txtIsbn);
        panelEntrada.add(crearLabel("Año:"));
        panelEntrada.add(txtAño);
        panelEntrada.add(crearLabel("Editorial:"));
        panelEntrada.add(txtEditorial);
        panelEntrada.add(crearLabel("Categoría:"));
        panelEntrada.add(comboCategory);
        
        JButton btnAgregar = crearBoton("Agregar Libro");
        btnAgregar.addActionListener(e -> agregarLibro());
        panelEntrada.add(btnAgregar);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(COLOR_BACKGROUND);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "Búsqueda", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                FONT_TITLE, COLOR_TEXT),
            new EmptyBorder(10, 10, 10, 10)));
        
        txtBusqueda = crearTextField();
        txtBusqueda.setPreferredSize(new Dimension(200, 30));
        comboCriterioBusqueda = new JComboBox<>(new String[]{"Título", "Autor", "ISBN"});
        comboCriterioBusqueda.setPreferredSize(new Dimension(120, 30));
        JButton btnBuscar = crearBoton("Buscar");
        btnBuscar.addActionListener(e -> buscarLibros());
        
        panelBusqueda.add(crearLabel("Buscar por:"));
        panelBusqueda.add(comboCriterioBusqueda);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        
        // Tabla de libros
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Editorial", "Categoría", "Disponible"};
        modeloTablaLibros = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTablaLibros);
        estilizarTabla(tablaLibros);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Panel superior que contiene entrada y búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 15));
        panelSuperior.setBackground(COLOR_BACKGROUND);
        panelSuperior.add(panelEntrada, BorderLayout.CENTER);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);
        
        // Agregar componentes al panel principal
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de préstamo
        JPanel panelPrestamo = new JPanel(new GridBagLayout());
        panelPrestamo.setBackground(COLOR_BACKGROUND);
        panelPrestamo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "Realizar Préstamo", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                FONT_TITLE, COLOR_TEXT),
            new EmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtIsbnPrestamo = crearTextField();
        JTextField txtUsuario = crearTextField();
        JButton btnPrestar = crearBoton("Realizar Préstamo");
        
        panelPrestamo.add(crearLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        panelPrestamo.add(txtIsbnPrestamo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrestamo.add(crearLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panelPrestamo.add(txtUsuario, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panelPrestamo.add(btnPrestar, gbc);
        
        btnPrestar.addActionListener(e -> {
            String isbn = txtIsbnPrestamo.getText();
            String usuario = txtUsuario.getText();
            if (!isbn.isEmpty() && !usuario.isEmpty()) {
                Prestamo prestamo = biblioteca.realizarPrestamo(isbn, usuario);
                if (prestamo != null) {
                    mostrarMensaje("Préstamo realizado con éxito", "Éxito");
                    actualizarTablaLibros();
                } else {
                    mostrarError("No se pudo realizar el préstamo");
                }
            }
        });
        
        // Panel de devolución
        JPanel panelDevolucion = new JPanel(new GridBagLayout());
        panelDevolucion.setBackground(COLOR_BACKGROUND);
        panelDevolucion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "Realizar Devolución", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                FONT_TITLE, COLOR_TEXT),
            new EmptyBorder(10, 10, 10, 10)));

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtIsbnDevolucion = crearTextField();
        JButton btnDevolver = crearBoton("Realizar Devolución");
        
        panelDevolucion.add(crearLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        panelDevolucion.add(txtIsbnDevolucion, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelDevolucion.add(btnDevolver, gbc);
        
        btnDevolver.addActionListener(e -> {
            String isbn = txtIsbnDevolucion.getText();
            if (!isbn.isEmpty()) {
                boolean devuelto = biblioteca.realizarDevolucion(isbn);
                if (devuelto) {
                    mostrarMensaje("Devolución realizada con éxito", "Éxito");
                    actualizarTablaLibros();
                } else {
                    mostrarError("No se pudo realizar la devolución");
                }
            }
        });
        
        // Panel contenedor
        JPanel contenedor = new JPanel(new GridLayout(2, 1, 15, 15));
        contenedor.setBackground(COLOR_BACKGROUND);
        contenedor.add(panelPrestamo);
        contenedor.add(panelDevolucion);
        
        panel.add(contenedor, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JButton btnReporteDisponibles = crearBoton("Generar Reporte de Libros Disponibles");
        JButton btnReportePrestados = crearBoton("Generar Reporte de Libros Prestados");
        JButton btnReporteVencidos = crearBoton("Generar Reporte de Préstamos Vencidos");
        
        panel.add(btnReporteDisponibles, gbc);
        gbc.gridy = 1;
        panel.add(btnReportePrestados, gbc);
        gbc.gridy = 2;
        panel.add(btnReporteVencidos, gbc);
        
        // Crear directorio de reportes si no existe
        File directorioReportes = new File(DIRECTORIO_REPORTES);
        if (!directorioReportes.exists()) {
            directorioReportes.mkdir();
        }
        
        btnReporteDisponibles.addActionListener(e -> {
            try {
                String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String nombreArchivo = DIRECTORIO_REPORTES + "reporte_disponibles_" + fecha + ".txt";
                biblioteca.generarReporteLibrosDisponibles(nombreArchivo);
                mostrarMensaje("Reporte generado con éxito en: " + nombreArchivo, "Éxito");
            } catch (IOException ex) {
                mostrarError("Error al generar el reporte: " + ex.getMessage());
            }
        });
        
        btnReportePrestados.addActionListener(e -> {
            try {
                String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String nombreArchivo = DIRECTORIO_REPORTES + "reporte_prestados_" + fecha + ".txt";
                biblioteca.generarReporteLibrosPrestados(nombreArchivo);
                mostrarMensaje("Reporte generado con éxito en: " + nombreArchivo, "Éxito");
            } catch (IOException ex) {
                mostrarError("Error al generar el reporte: " + ex.getMessage());
            }
        });

        btnReporteVencidos.addActionListener(e -> {
            try {
                String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String nombreArchivo = DIRECTORIO_REPORTES + "reporte_vencidos_" + fecha + ".txt";
                biblioteca.generarReportePrestamosVencidos(nombreArchivo);
                mostrarMensaje("Reporte generado con éxito en: " + nombreArchivo, "Éxito");
            } catch (IOException ex) {
                mostrarError("Error al generar el reporte: " + ex.getMessage());
            }
        });
        
        return panel;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_MAIN);
        label.setForeground(COLOR_TEXT);
        return label;
    }

    private JTextField crearTextField() {
        JTextField textField = new JTextField();
        textField.setFont(FONT_MAIN);
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FONT_MAIN);
        boton.setBackground(COLOR_PRIMARY);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(200, 35));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(FONT_MAIN);
        tabla.setRowHeight(30);
        tabla.setShowGrid(true);
        tabla.setGridColor(COLOR_ACCENT);
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(COLOR_SECONDARY);
        tabla.setSelectionForeground(Color.WHITE);
        
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(COLOR_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(FONT_TITLE);
    }

    private void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void agregarLibro() {
        try {
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();
            String isbn = txtIsbn.getText();
            int año = Integer.parseInt(txtAño.getText());
            
            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }
            
            Libro libro = new Libro(titulo, autor, año, isbn, txtEditorial.getText(), (String) comboCategory.getSelectedItem(), 1);
            biblioteca.agregarLibro(libro);
            actualizarTablaLibros();
            limpiarCampos();
            
            mostrarMensaje("Libro agregado con éxito", "Éxito");
        } catch (NumberFormatException e) {
            mostrarError("El año debe ser un número válido");
        }
    }

    private void buscarLibros() {
        String criterio = (String) comboCriterioBusqueda.getSelectedItem();
        String busqueda = txtBusqueda.getText();
        
        if (busqueda.isEmpty()) {
            actualizarTablaLibros();
            return;
        }
        
        List<Libro> resultados;
        switch (criterio) {
            case "Título":
                resultados = biblioteca.buscarLibrosPorTitulo(busqueda);
                break;
            case "Autor":
                resultados = biblioteca.buscarLibrosPorAutor(busqueda);
                break;
            case "ISBN":
                Libro libro = biblioteca.buscarLibroPorIsbn(busqueda);
                resultados = libro != null ? Arrays.asList(libro) : new ArrayList<>();
                break;
            default:
                resultados = new ArrayList<>();
                break;
        }
        
        actualizarTablaLibros(resultados);
    }

    private void actualizarTablaLibros() {
        actualizarTablaLibros(biblioteca.getListaLibros());
    }

    private void actualizarTablaLibros(List<Libro> libros) {
        modeloTablaLibros.setRowCount(0);
        for (Libro libro : libros) {
            modeloTablaLibros.addRow(new Object[]{
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAño(),
                libro.getEditorial(),
                libro.getCategoria(),
                libro.isDisponible() ? "Sí" : "No"
            });
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtAño.setText("");
        txtEditorial.setText("");
        comboCategory.setSelectedIndex(0);
        txtTitulo.requestFocus();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            BibliotecaGUI gui = new BibliotecaGUI();
            gui.setVisible(true);
        });
    }
}