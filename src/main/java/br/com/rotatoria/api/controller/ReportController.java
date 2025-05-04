package br.com.rotatoria.api.controller;

import br.com.rotatoria.api.domain.dto.report.ReportRequestDTO;
import br.com.rotatoria.api.domain.dto.report.ReportResponseDTO;
import br.com.rotatoria.api.domain.dto.report.ReportStatusDTO;
import br.com.rotatoria.api.domain.model.Report;
import br.com.rotatoria.api.domain.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Report> create(@RequestBody ReportRequestDTO reportRequestDTO) {
        return reportService.create(reportRequestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> findById(@PathVariable Long id) {
        return reportService.findById(id);
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<ReportResponseDTO> updateProcessingStatus(@PathVariable Long id, @RequestBody ReportStatusDTO reportStatusDTO) {
//        return reportService.updateProcessingStatus(id, reportStatusDTO);
//    }
}
