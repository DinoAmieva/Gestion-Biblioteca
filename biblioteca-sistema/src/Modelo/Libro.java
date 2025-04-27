package Modelo;

import java.io.Serializable;
import java.util.Objects;

public class Libro implements Serializable, Comparable<Libro> {
    private String titulo;
    private String autor;
    private int añoPublicacion;
    private String isbn;
    private boolean disponible;
    private String editorial;
    private String categoria;
    private int cantidadEjemplares;
    private int ejemplaresDisponibles;

    public Libro(String titulo, String autor, int añoPublicacion, String isbn, 
                String editorial, String categoria, int cantidadEjemplares) {
        setTitulo(titulo);
        setAutor(autor);
        setAñoPublicacion(añoPublicacion);
        setIsbn(isbn);
        setEditorial(editorial);
        setCategoria(categoria);
        setCantidadEjemplares(cantidadEjemplares);
        this.ejemplaresDisponibles = cantidadEjemplares;
        this.disponible = cantidadEjemplares > 0;
    }

    // Getters y Setters con validaciones
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        this.autor = autor.trim();
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAñoPublicacion(int añoPublicacion) {
        if (añoPublicacion < 0 || añoPublicacion > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("El año de publicación no es válido");
        }
        this.añoPublicacion = añoPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || !isbn.matches("\\d{13}")) {
            throw new IllegalArgumentException("El ISBN debe tener 13 dígitos");
        }
        this.isbn = isbn;
    }

    public boolean isDisponible() {
        return disponible && ejemplaresDisponibles > 0;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        if (editorial == null || editorial.trim().isEmpty()) {
            throw new IllegalArgumentException("La editorial no puede estar vacía");
        }
        this.editorial = editorial.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
        this.categoria = categoria.trim();
    }

    public int getCantidadEjemplares() {
        return cantidadEjemplares;
    }

    public void setCantidadEjemplares(int cantidadEjemplares) {
        if (cantidadEjemplares < 0) {
            throw new IllegalArgumentException("La cantidad de ejemplares no puede ser negativa");
        }
        this.cantidadEjemplares = cantidadEjemplares;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void prestarEjemplar() {
        if (ejemplaresDisponibles <= 0) {
            throw new IllegalStateException("No hay ejemplares disponibles para prestar");
        }
        ejemplaresDisponibles--;
        disponible = ejemplaresDisponibles > 0;
    }

    public void devolverEjemplar() {
        if (ejemplaresDisponibles >= cantidadEjemplares) {
            throw new IllegalStateException("No se pueden devolver más ejemplares de los prestados");
        }
        ejemplaresDisponibles++;
        disponible = true;
    }

    // Método para obtener el año (compatibilidad)
    public int getAño() {
        return añoPublicacion;
    }

    @Override
    public String toString() {
        return String.format("Libro{ISBN=%s, Título='%s', Autor='%s', Año=%d, Editorial='%s', " +
                           "Categoría='%s', Ejemplares=%d/%d}",
                           isbn, titulo, autor, añoPublicacion, editorial, categoria,
                           ejemplaresDisponibles, cantidadEjemplares);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return isbn.equals(libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public int compareTo(Libro otro) {
        return this.titulo.compareToIgnoreCase(otro.titulo);
    }
}