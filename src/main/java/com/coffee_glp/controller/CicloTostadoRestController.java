package com.coffee_glp.controller;

import com.coffee_glp.model.dao.ICicloTostadoDao;
import com.coffee_glp.model.entities.CicloTostado;
import com.coffee_glp.model.entities.CicloTostado.CicloStatus;
import com.coffee_glp.model.entities.Usuario;
import com.coffee_glp.repository.RoastProfileRepository;
import com.coffee_glp.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ciclos")
public class CicloTostadoRestController {

    @Autowired
    private ICicloTostadoDao cicloTostadoDao;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoastProfileRepository roastProfileRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registrarCiclo(@Valid @RequestBody CicloTostado cicloTostado) {
        // Validate coffee amount
        if (cicloTostado.getCoffeeAmount() == null || cicloTostado.getCoffeeAmount() <= 0 || cicloTostado.getCoffeeAmount() > 2500) {
            return ResponseEntity.badRequest().body("La cantidad de café debe estar entre 1 y 2500 gramos");
        }

        // Set start time to now if not provided
        if (cicloTostado.getFechaInicio() == null) {
            cicloTostado.setFechaInicio(LocalDateTime.now());
        }

        // Set initial status
        cicloTostado.setStatus(CicloStatus.PENDING);

        // Save the cycle
        CicloTostado savedCiclo = cicloTostadoDao.save(cicloTostado);
        return ResponseEntity.ok(savedCiclo);
    }

    @GetMapping
    public List<CicloTostado> listarCiclos(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long profileId,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username);

        List<CicloTostado> ciclos;

        // If admin, get all cycles, otherwise get only cycles for this user
        if (usuario != null && "ADMIN".equals(usuario.getRole())) {
            ciclos = cicloTostadoDao.findAll();
        } else {
            ciclos = cicloTostadoDao.findByCliente(usuario);
        }

        // Apply filters
        if (ciclos != null && !ciclos.isEmpty()) {
            // Filter by status
            if (status != null && !status.isEmpty()) {
                try {
                    CicloTostado.CicloStatus cicloStatus = CicloTostado.CicloStatus.valueOf(status.toUpperCase());
                    ciclos = ciclos.stream()
                            .filter(c -> c.getStatus() == cicloStatus)
                            .collect(java.util.stream.Collectors.toList());
                } catch (IllegalArgumentException e) {
                    // Invalid status, ignore filter
                }
            }

            // Filter by profile
            if (profileId != null) {
                ciclos = ciclos.stream()
                        .filter(c -> c.getProfile() != null && c.getProfile().getId().equals(profileId))
                        .collect(java.util.stream.Collectors.toList());
            }

            // Filter by start date
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                try {
                    java.time.LocalDate startDate = java.time.LocalDate.parse(fechaInicio);
                    ciclos = ciclos.stream()
                            .filter(c -> c.getFechaInicio() != null && 
                                    c.getFechaInicio().toLocalDate().isEqual(startDate) || 
                                    c.getFechaInicio().toLocalDate().isAfter(startDate))
                            .collect(java.util.stream.Collectors.toList());
                } catch (Exception e) {
                    // Invalid date format, ignore filter
                }
            }

            // Filter by end date
            if (fechaFin != null && !fechaFin.isEmpty()) {
                try {
                    java.time.LocalDate endDate = java.time.LocalDate.parse(fechaFin);
                    ciclos = ciclos.stream()
                            .filter(c -> c.getFechaFin() != null && 
                                    c.getFechaFin().toLocalDate().isEqual(endDate) || 
                                    c.getFechaFin().toLocalDate().isBefore(endDate))
                            .collect(java.util.stream.Collectors.toList());
                } catch (Exception e) {
                    // Invalid date format, ignore filter
                }
            }
        }

        return ciclos;
    }

    @GetMapping("/search")
    public ResponseEntity<?> buscarCiclos(@RequestParam String query) {
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username);

        List<CicloTostado> ciclos;

        // If admin, search all cycles, otherwise search only cycles for this user
        if (usuario != null && "ADMIN".equals(usuario.getRole())) {
            ciclos = cicloTostadoDao.findAll();
        } else {
            ciclos = cicloTostadoDao.findByCliente(usuario);
        }

        // Apply search filter
        if (query != null && !query.isEmpty()) {
            String lowerQuery = query.toLowerCase();
            ciclos = ciclos.stream()
                    .filter(c -> 
                        // Search in description
                        (c.getDescripcion() != null && c.getDescripcion().toLowerCase().contains(lowerQuery)) ||
                        // Search in profile name
                        (c.getProfile() != null && c.getProfile().getName().toLowerCase().contains(lowerQuery)) ||
                        // Search in status
                        c.getStatus().toString().toLowerCase().contains(lowerQuery) ||
                        // Search in client username
                        (c.getCliente() != null && c.getCliente().getUsername().toLowerCase().contains(lowerQuery)) ||
                        // Search in tostador username
                        (c.getUsuarioTostador() != null && c.getUsuarioTostador().getUsername().toLowerCase().contains(lowerQuery))
                    )
                    .collect(java.util.stream.Collectors.toList());
        }

        return ResponseEntity.ok(ciclos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCicloById(@PathVariable Long id) {
        Optional<CicloTostado> ciclo = cicloTostadoDao.findById(id);
        if (ciclo.isPresent()) {
            return ResponseEntity.ok(ciclo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> startCiclo(@PathVariable Long id) {
        Optional<CicloTostado> optionalCiclo = cicloTostadoDao.findById(id);
        if (!optionalCiclo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CicloTostado ciclo = optionalCiclo.get();
        if (ciclo.getStatus() != CicloStatus.PENDING) {
            return ResponseEntity.badRequest().body("El ciclo no está en estado pendiente");
        }

        ciclo.setStatus(CicloStatus.IN_PROGRESS);
        ciclo.setFechaInicio(LocalDateTime.now());
        cicloTostadoDao.save(ciclo);

        return ResponseEntity.ok(ciclo);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> completeCiclo(@PathVariable Long id) {
        Optional<CicloTostado> optionalCiclo = cicloTostadoDao.findById(id);
        if (!optionalCiclo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CicloTostado ciclo = optionalCiclo.get();
        if (ciclo.getStatus() != CicloStatus.IN_PROGRESS) {
            return ResponseEntity.badRequest().body("El ciclo no está en progreso");
        }

        ciclo.setStatus(CicloStatus.COMPLETED);
        ciclo.setFechaFin(LocalDateTime.now());
        cicloTostadoDao.save(ciclo);

        return ResponseEntity.ok(ciclo);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cancelCiclo(@PathVariable Long id) {
        Optional<CicloTostado> optionalCiclo = cicloTostadoDao.findById(id);
        if (!optionalCiclo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CicloTostado ciclo = optionalCiclo.get();
        if (ciclo.getStatus() == CicloStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("No se puede cancelar un ciclo completado");
        }

        ciclo.setStatus(CicloStatus.CANCELLED);
        ciclo.setFechaFin(LocalDateTime.now());
        cicloTostadoDao.save(ciclo);

        return ResponseEntity.ok(ciclo);
    }
}
