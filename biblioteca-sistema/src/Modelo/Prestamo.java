package Modelo;


import java.time.LocalDate;

public class Prestamo {
    private Libro libro;
    private String nombreUsuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public Prestamo(Libro libro, String nombreUsuario) {
        this.libro = libro;
        this.nombreUsuario = nombreUsuario;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = null;
        this.libro.setDisponible(false);
    }

    public void realizarDevolucion() {
        this.fechaDevolucion = LocalDate.now();
        this.libro.setDisponible(true);
    }

    // Getters y Setters
    public Libro getLibro() {
        return libro;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "libro=" + libro.getTitulo() +
                ", usuario='" + nombreUsuario + '\'' +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                '}';
    }
} 