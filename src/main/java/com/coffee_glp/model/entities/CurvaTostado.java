package com.coffee_glp.model.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "curvas_tostado")
public class CurvaTostado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ciclo_id", nullable = false)
    private Long cicloId;

    @Column(nullable = false)
    private Double temperatura;

    private Double presion;

    // Getter para cicloId
    public Long getCicloId() {
        return cicloId;
    }

    // Setter para cicloId
    public void setCicloId(Long cicloId) {
        this.cicloId = cicloId;
    }
}