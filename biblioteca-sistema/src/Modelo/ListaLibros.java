package Modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListaLibros {
    private NodoLibro cabeza;
    private int tamaño;

    private static class NodoLibro {
        Libro libro;
        NodoLibro siguiente;

        NodoLibro(Libro libro) {
            this.libro = libro;
            this.siguiente = null;
        }
    }

    public ListaLibros() {
        cabeza = null;
        tamaño = 0;
    }

    public void insertar(Libro libro) {
        NodoLibro nuevoNodo = new NodoLibro(libro);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoLibro actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamaño++;
    }

    public boolean eliminar(String isbn) {
        if (cabeza == null) return false;

        if (cabeza.libro.getIsbn().equals(isbn)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return true;
        }

        NodoLibro actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.libro.getIsbn().equals(isbn)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public Libro buscarPorIsbn(String isbn) {
        NodoLibro actual = cabeza;
        while (actual != null) {
            if (actual.libro.getIsbn().equals(isbn)) {
                return actual.libro;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> resultados = new ArrayList<>();
        NodoLibro actual = cabeza;
        while (actual != null) {
            if (actual.libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados.add(actual.libro);
            }
            actual = actual.siguiente;
        }
        return resultados;
    }

    public List<Libro> buscarPorAutor(String autor) {
        List<Libro> resultados = new ArrayList<>();
        NodoLibro actual = cabeza;
        while (actual != null) {
            if (actual.libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                resultados.add(actual.libro);
            }
            actual = actual.siguiente;
        }
        return resultados;
    }

    public List<Libro> buscarPorCategoria(String categoria) {
        List<Libro> resultados = new ArrayList<>();
        NodoLibro actual = cabeza;
        while (actual != null) {
            if (actual.libro.getCategoria().equalsIgnoreCase(categoria)) {
                resultados.add(actual.libro);
            }
            actual = actual.siguiente;
        }
        return resultados;
    }

    public void ordenarPorTitulo() {
        List<Libro> libros = toList();
        libros.sort(Comparator.comparing(Libro::getTitulo));
        actualizarLista(libros);
    }

    public void ordenarPorAutor() {
        List<Libro> libros = toList();
        libros.sort(Comparator.comparing(Libro::getAutor));
        actualizarLista(libros);
    }

    public void ordenarPorAño() {
        List<Libro> libros = toList();
        libros.sort(Comparator.comparingInt(Libro::getAñoPublicacion));
        actualizarLista(libros);
    }

    public List<Libro> toList() {
        List<Libro> libros = new ArrayList<>();
        NodoLibro actual = cabeza;
        while (actual != null) {
            libros.add(actual.libro);
            actual = actual.siguiente;
        }
        return libros;
    }

    private void actualizarLista(List<Libro> libros) {
        cabeza = null;
        tamaño = 0;
        for (Libro libro : libros) {
            insertar(libro);
        }
    }

    public int getTamaño() {
        return tamaño;
    }

    public List<Libro> getLibrosDisponibles() {
        List<Libro> disponibles = new ArrayList<>();
        NodoLibro actual = cabeza;
        while (actual != null) {
            if (actual.libro.isDisponible()) {
                disponibles.add(actual.libro);
            }
            actual = actual.siguiente;
        }
        return disponibles;
    }

    public List<Libro> getLibrosPrestados() {
        List<Libro> prestados = new ArrayList<>();
        NodoLibro actual = cabeza;
        while (actual != null) {
            if (!actual.libro.isDisponible()) {
                prestados.add(actual.libro);
            }
            actual = actual.siguiente;
        }
        return prestados;
    }
} 