package com.biblioteca.gui;

import com.biblioteca.modelo.Biblioteca;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private final Biblioteca biblioteca;
    private JTabbedPane tabbedPane;
    private JTable tablaLibros;
    private DefaultTableModel modeloTablaLibros;
    private JTextField txtTitulo, txtAutor, txtIsbn, txtAño;
    private JTextField txtBusqueda;
    private JComboBox<String> comboCriterioBusqueda;

    public BibliotecaGUI() {
        biblioteca = new Biblioteca();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        
        // Panel de Gestión de Libros
        tabbedPane.addTab("Gestión de Libros", crearPanelGestionLibros());
        
        // Panel de Préstamos
        tabbedPane.addTab("Préstamos y Devoluciones", crearPanelPrestamos());
        
        // Panel de Reportes
        tabbedPane.addTab("Reportes", crearPanelReportes());
        
        add(tabbedPane);
    }

    private JPanel crearPanelGestionLibros() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de entrada de datos
        JPanel panelEntrada = new JPanel(new GridLayout(5, 2, 5, 5));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtAño = new JTextField();
        
        panelEntrada.add(new JLabel("Título:"));
        panelEntrada.add(txtTitulo);
        panelEntrada.add(new JLabel("Autor:"));
        panelEntrada.add(txtAutor);
        panelEntrada.add(new JLabel("ISBN:"));
        panelEntrada.add(txtIsbn);
        panelEntrada.add(new JLabel("Año:"));
        panelEntrada.add(txtAño);
        
        JButton btnAgregar = new JButton("Agregar Libro");
        btnAgregar.addActionListener(e -> agregarLibro());
        panelEntrada.add(btnAgregar);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        txtBusqueda = new JTextField(20);
        comboCriterioBusqueda = new JComboBox<>(new String[]{"Título", "Autor", "ISBN"});
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarLibros());
        
        panelBusqueda.add(new JLabel("Buscar por:"));
        panelBusqueda.add(comboCriterioBusqueda);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        
        // Tabla de libros
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Disponible"};
        modeloTablaLibros = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTablaLibros);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        
        // Agregar componentes al panel principal
        panel.add(panelEntrada, BorderLayout.NORTH);
        panel.add(panelBusqueda, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de préstamo
        JPanel panelPrestamo = new JPanel(new FlowLayout());
        JTextField txtIsbnPrestamo = new JTextField(15);
        JTextField txtUsuario = new JTextField(15);
        JButton btnPrestar = new JButton("Realizar Préstamo");
        
        panelPrestamo.add(new JLabel("ISBN:"));
        panelPrestamo.add(txtIsbnPrestamo);
        panelPrestamo.add(new JLabel("Usuario:"));
        panelPrestamo.add(txtUsuario);
        panelPrestamo.add(btnPrestar);
        
        btnPrestar.addActionListener(e -> {
            String isbn = txtIsbnPrestamo.getText();
            String usuario = txtUsuario.getText();
            if (!isbn.isEmpty() && !usuario.isEmpty()) {
                Prestamo prestamo = biblioteca.realizarPrestamo(isbn, usuario);
                if (prestamo != null) {
                    JOptionPane.showMessageDialog(this, "Préstamo realizado con éxito");
                    actualizarTablaLibros();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el préstamo");
                }
            }
        });
        
        // Panel de devolución
        JPanel panelDevolucion = new JPanel(new FlowLayout());
        JTextField txtIsbnDevolucion = new JTextField(15);
        JButton btnDevolver = new JButton("Realizar Devolución");
        
        panelDevolucion.add(new JLabel("ISBN:"));
        panelDevolucion.add(txtIsbnDevolucion);
        panelDevolucion.add(btnDevolver);
        
        btnDevolver.addActionListener(e -> {
            String isbn = txtIsbnDevolucion.getText();
            if (!isbn.isEmpty()) {
                boolean devuelto = biblioteca.realizarDevolucion(isbn);
                if (devuelto) {
                    JOptionPane.showMessageDialog(this, "Devolución realizada con éxito");
                    actualizarTablaLibros();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar la devolución");
                }
            }
        });
        
        panel.add(panelPrestamo, BorderLayout.NORTH);
        panel.add(panelDevolucion, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton btnReporteDisponibles = new JButton("Generar Reporte de Libros Disponibles");
        JButton btnReportePrestados = new JButton("Generar Reporte de Libros Prestados");
        
        btnReporteDisponibles.addActionListener(e -> {
            try {
                biblioteca.generarReporteLibrosDisponibles("reporte_disponibles.txt");
                JOptionPane.showMessageDialog(this, "Reporte generado con éxito");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
            }
        });
        
        btnReportePrestados.addActionListener(e -> {
            try {
                biblioteca.generarReporteLibrosPrestados("reporte_prestados.txt");
                JOptionPane.showMessageDialog(this, "Reporte generado con éxito");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
            }
        });
        
        panel.add(btnReporteDisponibles);
        panel.add(btnReportePrestados);
        
        return panel;
    }

    private void agregarLibro() {
        try {
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();
            String isbn = txtIsbn.getText();
            int año = Integer.parseInt(txtAño.getText());
            
            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
                return;
            }
            
            Libro libro = new Libro(titulo, autor, año, isbn);
            biblioteca.agregarLibro(libro);
            actualizarTablaLibros();
            limpiarCampos();
            
            JOptionPane.showMessageDialog(this, "Libro agregado con éxito");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número válido");
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
            Object[] fila = {
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAñoPublicacion(),
                libro.isDisponible() ? "Sí" : "No"
            };
            modeloTablaLibros.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtAño.setText("");
    }

    public static void main(String[] args) {
        try {
            // Usar el Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Error al inicializar el Look and Feel");
        }
        
        SwingUtilities.invokeLater(() -> {
            BibliotecaGUI gui = new BibliotecaGUI();
            gui.setVisible(true);
        });
    }
} 