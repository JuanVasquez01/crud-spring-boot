package com.coffee_glp.controller;

import com.coffee_glp.model.dao.ICicloTostadoDao;
import com.coffee_glp.model.dao.ICurvaTostadoDao;
import com.coffee_glp.model.entities.CicloTostado;
import com.coffee_glp.model.entities.CurvaTostado;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/curvas")
public class CurvaTostadoRestController {

    @Autowired
    private ICurvaTostadoDao curvaTostadoDao;

    @Autowired
    private ICicloTostadoDao cicloTostadoDao;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> crearCurva(@Valid @RequestBody CurvaTostado curvaTostado) {
        // Validate cicloId exists
        if (curvaTostado.getCicloId() == null) {
            return ResponseEntity.badRequest().body("El ID del ciclo es requerido");
        }

        Optional<CicloTostado> ciclo = cicloTostadoDao.findById(curvaTostado.getCicloId());
        if (!ciclo.isPresent()) {
            return ResponseEntity.badRequest().body("El ciclo especificado no existe");
        }

        // Validate temperature
        if (curvaTostado.getTemperatura() == null) {
            return ResponseEntity.badRequest().body("La temperatura es requerida");
        }

        CurvaTostado savedCurva = curvaTostadoDao.save(curvaTostado);
        return ResponseEntity.ok(savedCurva);
    }

    @GetMapping("/ciclo/{cicloId}")
    public ResponseEntity<?> getCurvasByCiclo(@PathVariable Long cicloId) {
        // Validate ciclo exists
        Optional<CicloTostado> ciclo = cicloTostadoDao.findById(cicloId);
        if (!ciclo.isPresent()) {
            return ResponseEntity.badRequest().body("El ciclo especificado no existe");
        }

        List<CurvaTostado> curvas = curvaTostadoDao.findByCicloId(cicloId);
        return ResponseEntity.ok(curvas);
    }

    @GetMapping("/comparar")
    public ResponseEntity<?> compararCiclos(@RequestParam List<Long> cicloIds) {
        if (cicloIds == null || cicloIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Se requiere al menos un ID de ciclo");
        }

        // Validate all ciclos exist
        for (Long cicloId : cicloIds) {
            Optional<CicloTostado> ciclo = cicloTostadoDao.findById(cicloId);
            if (!ciclo.isPresent()) {
                return ResponseEntity.badRequest().body("El ciclo con ID " + cicloId + " no existe");
            }
        }

        // Create a map of cicloId -> list of curvas
        Map<Long, List<CurvaTostado>> curvasPorCiclo = new HashMap<>();
        for (Long cicloId : cicloIds) {
            List<CurvaTostado> curvas = curvaTostadoDao.findByCicloId(cicloId);
            curvasPorCiclo.put(cicloId, curvas);
        }

        // Create a response with the data for each ciclo
        Map<String, Object> response = new HashMap<>();
        for (Map.Entry<Long, List<CurvaTostado>> entry : curvasPorCiclo.entrySet()) {
            Long cicloId = entry.getKey();
            List<CurvaTostado> curvas = entry.getValue();

            // Get ciclo details
            Optional<CicloTostado> cicloOpt = cicloTostadoDao.findById(cicloId);
            CicloTostado ciclo = cicloOpt.get(); // Safe because we validated above

            // Create a map with ciclo details and curva data
            Map<String, Object> cicloData = new HashMap<>();
            cicloData.put("id", ciclo.getId());
            cicloData.put("profile", ciclo.getProfile().getName());
            cicloData.put("startTime", ciclo.getFechaInicio());
            cicloData.put("endTime", ciclo.getFechaFin());
            cicloData.put("status", ciclo.getStatus());

            // Extract temperature data points
            List<Map<String, Object>> dataPoints = new ArrayList<>();
            for (CurvaTostado curva : curvas) {
                Map<String, Object> point = new HashMap<>();
                point.put("timestamp", curva.getTimestamp());
                point.put("temperatura", curva.getTemperatura());
                point.put("presion", curva.getPresion());
                dataPoints.add(point);
            }
            cicloData.put("dataPoints", dataPoints);

            response.put("ciclo_" + cicloId, cicloData);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCurvaById(@PathVariable Long id) {
        Optional<CurvaTostado> curva = curvaTostadoDao.findById(id);
        if (curva.isPresent()) {
            return ResponseEntity.ok(curva.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ciclo/{cicloId}/export/csv")
    public void exportCurvasToCsv(@PathVariable Long cicloId, HttpServletResponse response) throws IOException {
        // Validate ciclo exists
        Optional<CicloTostado> ciclo = cicloTostadoDao.findById(cicloId);
        if (!ciclo.isPresent()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ciclo especificado no existe");
            return;
        }

        List<CurvaTostado> curvas = curvaTostadoDao.findByCicloIdOrderByTimestampDesc(cicloId);

        // Set response headers
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=curvas_ciclo_" + cicloId + ".csv");

        // Create CSV writer
        try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(response.getOutputStream()))) {
            // Write header
            csvWriter.writeNext(new String[]{"ID", "Ciclo ID", "Temperatura", "Presión", "Timestamp"});

            // Write data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (CurvaTostado curva : curvas) {
                csvWriter.writeNext(new String[]{
                    curva.getId().toString(),
                    curva.getCicloId().toString(),
                    curva.getTemperatura().toString(),
                    curva.getPresion() != null ? curva.getPresion().toString() : "",
                    curva.getTimestamp().format(formatter)
                });
            }
        }
    }

    @GetMapping("/ciclo/{cicloId}/export/pdf")
    public void exportCurvasToPdf(@PathVariable Long cicloId, HttpServletResponse response) throws IOException {
        // Validate ciclo exists
        Optional<CicloTostado> ciclo = cicloTostadoDao.findById(cicloId);
        if (!ciclo.isPresent()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ciclo especificado no existe");
            return;
        }

        List<CurvaTostado> curvas = curvaTostadoDao.findByCicloIdOrderByTimestampDesc(cicloId);

        // Set response headers
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=curvas_ciclo_" + cicloId + ".pdf");

        // Create PDF document
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Datos de Curva de Tostado - Ciclo " + cicloId, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Add table
            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);

            // Add table header
            String[] headers = {"ID", "Ciclo ID", "Temperatura", "Presión", "Timestamp"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell();
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setBorderWidth(2);
                cell.setPhrase(new Phrase(header));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Add data rows
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (CurvaTostado curva : curvas) {
                table.addCell(curva.getId().toString());
                table.addCell(curva.getCicloId().toString());
                table.addCell(curva.getTemperatura().toString());
                table.addCell(curva.getPresion() != null ? curva.getPresion().toString() : "");
                table.addCell(curva.getTimestamp().format(formatter));
            }

            document.add(table);

        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF", e);
        } finally {
            document.close();
        }
    }
}
