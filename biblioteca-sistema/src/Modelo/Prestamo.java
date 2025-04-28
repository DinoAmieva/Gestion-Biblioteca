package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo implements Serializable {
    private static final int DIAS_PRESTAMO = 3;
    private static final double MULTA_POR_DIA = 100.0;

    private static final String ARCHIVO_LIBROS = "libros.dat";
    private static final String ARCHIVO_PRESTAMOS = "prestamos.dat";
    private static final String ARCHIVO_HISTORIAL = "historial.dat";
    private Libro libro;
    private String nombreUsuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private LocalDate fechaVencimiento;
    private double multa;
    private boolean devuelto;

    public Prestamo(Libro libro, String nombreUsuario) {
        if (libro == null || !libro.isDisponible()) {
            throw new IllegalStateException("El libro no está disponible para préstamo");
        }
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        this.libro = libro;
        this.nombreUsuario = nombreUsuario.trim();
        this.fechaPrestamo = LocalDate.now();
        this.fechaVencimiento = fechaPrestamo.plusDays(DIAS_PRESTAMO);
        this.multa = 0.0;
        this.devuelto = false;
        this.libro.prestarEjemplar();
    }

    public void realizarDevolucion() {
        if (devuelto) {
            throw new IllegalStateException("El préstamo ya fue devuelto");
        }
        
        this.fechaDevolucion = LocalDate.now();
        this.devuelto = true;
        this.libro.devolverEjemplar();
        
        // Calcular multa si hay retraso
        if (fechaDevolucion.isAfter(fechaVencimiento)) {
            long diasRetraso = ChronoUnit.DAYS.between(fechaVencimiento, fechaDevolucion);
            this.multa = diasRetraso * MULTA_POR_DIA;
        }
    }

    public boolean estaVencido() {
        return !devuelto && LocalDate.now().isAfter(fechaVencimiento);
    }

    public long getDiasRetraso() {
        if (!devuelto) {
            return Math.max(0, ChronoUnit.DAYS.between(fechaVencimiento, LocalDate.now()));
        }
        return Math.max(0, ChronoUnit.DAYS.between(fechaVencimiento, fechaDevolucion));
    }

    public double getMulta() {
        if (!devuelto && estaVencido()) {
            return getDiasRetraso() * MULTA_POR_DIA;
        }
        return multa;
    }

    // Getters
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

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    @Override
    public String toString() {
        return String.format("Prestamo{ISBN=%s, Usuario='%s', FechaPrestamo=%s, " +
                           "FechaVencimiento=%s, FechaDevolucion=%s, Multa=%.2f}",
                           libro.getIsbn(), nombreUsuario, fechaPrestamo,
                           fechaVencimiento, fechaDevolucion, getMulta());
    }
} 