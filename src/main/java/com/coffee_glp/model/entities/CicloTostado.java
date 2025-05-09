package com.coffee_glp.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "roast_cycles")
public class CicloTostado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_tostador_id", nullable = false)
    private Usuario usuarioTostador;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private RoastProfile profile;

    @NotNull
    @Min(1)
    @Max(2500)
    @Column(name = "coffee_amount", nullable = false)
    private Integer coffeeAmount; // in grams

    private String descripcion;

    @Column(name = "start_time")
    private LocalDateTime fechaInicio;

    @Column(name = "end_time")
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CicloStatus status = CicloStatus.PENDING;

    public enum CicloStatus {
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuarioTostador() {
        return usuarioTostador;
    }

    public void setUsuarioTostador(Usuario usuarioTostador) {
        this.usuarioTostador = usuarioTostador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public RoastProfile getProfile() {
        return profile;
    }

    public void setProfile(RoastProfile profile) {
        this.profile = profile;
    }

    public Integer getCoffeeAmount() {
        return coffeeAmount;
    }

    public void setCoffeeAmount(Integer coffeeAmount) {
        this.coffeeAmount = coffeeAmount;
    }

    public CicloStatus getStatus() {
        return status;
    }

    public void setStatus(CicloStatus status) {
        this.status = status;
    }
}
