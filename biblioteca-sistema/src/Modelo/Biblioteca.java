package Modelo;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Biblioteca implements Serializable {
    private static final String ARCHIVO_LIBROS = "libros.dat";
    private static final String ARCHIVO_PRESTAMOS = "prestamos.dat";
    private static final String ARCHIVO_HISTORIAL = "historial.dat";
    
    private ListaLibros listaLibros;
    private Queue<Prestamo> colaPrestamos;
    private HashMap<String, Libro> mapLibros;
    private ArrayList<Prestamo> historialPrestamos;

    public Biblioteca() {
        listaLibros = new ListaLibros();
        colaPrestamos = new LinkedList<>();
        mapLibros = new HashMap<>();
        historialPrestamos = new ArrayList<>();
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            // Cargar libros
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_LIBROS))) {
                List<Libro> libros = (List<Libro>) ois.readObject();
                for (Libro libro : libros) {
                    listaLibros.insertar(libro);
                    mapLibros.put(libro.getIsbn(), libro);
                }
            } catch (FileNotFoundException e) {
                // El archivo no existe, se creará cuando se guarde
            }

            // Cargar préstamos actuales
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_PRESTAMOS))) {
                colaPrestamos = (LinkedList<Prestamo>) ois.readObject();
            } catch (FileNotFoundException e) {
                // El archivo no existe, se creará cuando se guarde
            }

            // Cargar historial
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_HISTORIAL))) {
                historialPrestamos = (ArrayList<Prestamo>) ois.readObject();
            } catch (FileNotFoundException e) {
                // El archivo no existe, se creará cuando se guarde
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    public void guardarDatos() {
        try {
            // Guardar libros
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_LIBROS))) {
                oos.writeObject(listaLibros.toList());
            }

            // Guardar préstamos actuales
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PRESTAMOS))) {
                oos.writeObject(colaPrestamos);
            }

            // Guardar historial
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_HISTORIAL))) {
                oos.writeObject(historialPrestamos);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public void agregarLibro(Libro libro) {
        if (mapLibros.containsKey(libro.getIsbn())) {
            throw new IllegalArgumentException("Ya existe un libro con ese ISBN");
        }
        listaLibros.insertar(libro);
        mapLibros.put(libro.getIsbn(), libro);
        guardarDatos();
    }

    public boolean eliminarLibro(String isbn) {
        Libro libro = mapLibros.get(isbn);
        if (libro != null && libro.isDisponible()) {
            listaLibros.eliminar(isbn);
            mapLibros.remove(isbn);
            guardarDatos();
            return true;
        }
        return false;
    }

    public Libro buscarLibroPorIsbn(String isbn) {
        return listaLibros.buscarPorIsbn(isbn);
    }

    public List<Libro> buscarLibrosPorTitulo(String titulo) {
        return listaLibros.buscarPorTitulo(titulo);
    }

    public List<Libro> buscarLibrosPorAutor(String autor) {
        return listaLibros.buscarPorAutor(autor);
    }

    public List<Libro> buscarLibrosPorCategoria(String categoria) {
        return listaLibros.buscarPorCategoria(categoria);
    }

    public void ordenarLibrosPorTitulo() {
        listaLibros.ordenarPorTitulo();
    }

    public void ordenarLibrosPorAutor() {
        listaLibros.ordenarPorAutor();
    }

    public void ordenarLibrosPorAño() {
        listaLibros.ordenarPorAño();
    }

    public Prestamo realizarPrestamo(String isbn, String nombreUsuario) {
        Libro libro = mapLibros.get(isbn);
        if (libro != null && libro.isDisponible()) {
            Prestamo prestamo = new Prestamo(libro, nombreUsuario);
            colaPrestamos.add(prestamo);
            historialPrestamos.add(prestamo);
            guardarDatos();
            return prestamo;
        }
        return null;
    }

    public boolean realizarDevolucion(String isbn) {
        for (Prestamo prestamo : colaPrestamos) {
            if (prestamo.getLibro().getIsbn().equals(isbn) && !prestamo.isDevuelto()) {
                prestamo.realizarDevolucion();
                colaPrestamos.remove(prestamo);
                guardarDatos();
                return true;
            }
        }
        return false;
    }

    public List<Prestamo> getPrestamosVencidos() {
        List<Prestamo> vencidos = new ArrayList<>();
        for (Prestamo prestamo : colaPrestamos) {
            if (prestamo.estaVencido()) {
                vencidos.add(prestamo);
            }
        }
        return vencidos;
    }

    public double getTotalMultasPendientes() {
        double total = 0.0;
        for (Prestamo prestamo : colaPrestamos) {
            if (prestamo.estaVencido()) {
                total += prestamo.getMulta();
            }
        }
        return total;
    }

    public void generarReporteLibrosDisponibles(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("Reporte de Libros Disponibles - " + LocalDate.now() + "\n\n");
            for (Libro libro : listaLibros.getLibrosDisponibles()) {
                writer.write(libro.toString() + "\n");
            }
        }
    }

    public void generarReporteLibrosPrestados(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("Reporte de Libros Prestados - " + LocalDate.now() + "\n\n");
            for (Prestamo prestamo : colaPrestamos) {
                writer.write(prestamo.toString() + "\n");
            }
        }
    }

    public void generarReportePrestamosVencidos(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("Reporte de Préstamos Vencidos - " + LocalDate.now() + "\n\n");
            for (Prestamo prestamo : getPrestamosVencidos()) {
                writer.write(prestamo.toString() + "\n");
            }
            writer.write("\nTotal de multas pendientes: $" + getTotalMultasPendientes());
        }
    }

    // Getters para las colecciones
    public List<Libro> getListaLibros() {
        return listaLibros.toList();
    }

    public Queue<Prestamo> getColaPrestamos() {
        return new LinkedList<>(colaPrestamos);
    }

    public List<Prestamo> getHistorialPrestamos() {
        return new ArrayList<>(historialPrestamos);
    }
} 
