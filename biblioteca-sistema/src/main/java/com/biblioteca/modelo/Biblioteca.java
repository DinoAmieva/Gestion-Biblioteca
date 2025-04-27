package com.biblioteca.modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Biblioteca {
    private LinkedList<Libro> listaLibros;
    private Queue<Prestamo> colaPrestamos;
    private HashMap<String, Libro> mapLibros;
    private ArrayList<Prestamo> historialPrestamos;

    public Biblioteca() {
        this.listaLibros = new LinkedList<>();
        this.colaPrestamos = new LinkedList<>();
        this.mapLibros = new HashMap<>();
        this.historialPrestamos = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        listaLibros.add(libro);
        mapLibros.put(libro.getIsbn(), libro);
    }

    public boolean eliminarLibro(String isbn) {
        Libro libro = mapLibros.get(isbn);
        if (libro != null && libro.isDisponible()) {
            listaLibros.remove(libro);
            mapLibros.remove(isbn);
            return true;
        }
        return false;
    }

    public Libro buscarLibroPorIsbn(String isbn) {
        return mapLibros.get(isbn);
    }

    public List<Libro> buscarLibrosPorTitulo(String titulo) {
        List<Libro> resultados = new ArrayList<>();
        for (Libro libro : listaLibros) {
            if (libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    public List<Libro> buscarLibrosPorAutor(String autor) {
        List<Libro> resultados = new ArrayList<>();
        for (Libro libro : listaLibros) {
            if (libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    public Prestamo realizarPrestamo(String isbn, String nombreUsuario) {
        Libro libro = mapLibros.get(isbn);
        if (libro != null && libro.isDisponible()) {
            Prestamo prestamo = new Prestamo(libro, nombreUsuario);
            colaPrestamos.add(prestamo);
            historialPrestamos.add(prestamo);
            return prestamo;
        }
        return null;
    }

    public boolean realizarDevolucion(String isbn) {
        for (Prestamo prestamo : colaPrestamos) {
            if (prestamo.getLibro().getIsbn().equals(isbn) && 
                prestamo.getFechaDevolucion() == null) {
                prestamo.realizarDevolucion();
                colaPrestamos.remove(prestamo);
                return true;
            }
        }
        return false;
    }

    public void generarReporteLibrosDisponibles(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("Reporte de Libros Disponibles - " + LocalDate.now() + "\n\n");
            for (Libro libro : listaLibros) {
                if (libro.isDisponible()) {
                    writer.write(libro.toString() + "\n");
                }
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

    // Getters para las colecciones
    public List<Libro> getListaLibros() {
        return new ArrayList<>(listaLibros);
    }

    public Queue<Prestamo> getColaPrestamos() {
        return new LinkedList<>(colaPrestamos);
    }

    public List<Prestamo> getHistorialPrestamos() {
        return new ArrayList<>(historialPrestamos);
    }
} 