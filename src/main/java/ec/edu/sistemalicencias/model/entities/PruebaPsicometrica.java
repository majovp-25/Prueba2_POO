package ec.edu.sistemalicencias.model.entities;

import java.time.LocalDateTime;

public class PruebaPsicometrica {
    private Long id;
    private Long conductorId;
    private LocalDateTime fechaRealizacion;

    private Double notaPercepcion;
    private Double notaAtencion;
    private Double notaCoordinacion;
    private Double notaPsicologica;
    private Double notaReaccion;

    private String observaciones;

    public PruebaPsicometrica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConductorId() { return conductorId; }
    public void setConductorId(Long conductorId) { this.conductorId = conductorId; }

    public LocalDateTime getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDateTime fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }

    public Double getNotaPercepcion() { return notaPercepcion; }
    public void setNotaPercepcion(Double notaPercepcion) { this.notaPercepcion = notaPercepcion; }

    public Double getNotaAtencion() { return notaAtencion; }
    public void setNotaAtencion(Double notaAtencion) { this.notaAtencion = notaAtencion; }

    public Double getNotaCoordinacion() { return notaCoordinacion; }
    public void setNotaCoordinacion(Double notaCoordinacion) { this.notaCoordinacion = notaCoordinacion; }

    public Double getNotaPsicologica() { return notaPsicologica; }
    public void setNotaPsicologica(Double notaPsicologica) { this.notaPsicologica = notaPsicologica; }

    public Double getNotaReaccion() { return notaReaccion; }
    public void setNotaReaccion(Double notaReaccion) { this.notaReaccion = notaReaccion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public boolean estaAprobado() {
        return  validarNota(notaPercepcion) &&
                validarNota(notaAtencion) &&
                validarNota(notaCoordinacion) &&
                validarNota(notaPsicologica) &&
                validarNota(notaReaccion);
    }

    private boolean validarNota(Double nota) {
        return nota != null && nota >= 70.0;
    }
}