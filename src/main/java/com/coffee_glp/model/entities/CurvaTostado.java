package com.coffee_glp.model.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "curvas_tostado")
public class CurvaTostado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ciclo_id", nullable = false)
    private Long cicloId;

    @NotNull
    @Column(nullable = false)
    private Double temperatura;

    private Double presion;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCicloId() {
        return cicloId;
    }

    public void setCicloId(Long cicloId) {
        this.cicloId = cicloId;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getPresion() {
        return presion;
    }

    public void setPresion(Double presion) {
        this.presion = presion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
