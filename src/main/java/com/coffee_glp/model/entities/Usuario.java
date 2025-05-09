package com.coffee_glp.model.entities;

    import jakarta.persistence.*;

    @Entity
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;
        private String role;
        private String correo;
        private String password; // Campo para la contraseña

        private String resetToken;
        private java.time.LocalDateTime resetTokenExpiry;

        // Getters y setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getResetToken() {
            return resetToken;
        }

        public void setResetToken(String resetToken) {
            this.resetToken = resetToken;
        }

        public java.time.LocalDateTime getResetTokenExpiry() {
            return resetTokenExpiry;
        }

        public void setResetTokenExpiry(java.time.LocalDateTime resetTokenExpiry) {
            this.resetTokenExpiry = resetTokenExpiry;
        }
    }
