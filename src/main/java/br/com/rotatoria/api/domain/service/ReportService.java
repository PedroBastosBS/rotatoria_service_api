package br.com.rotatoria.api.domain.service;

import br.com.rotatoria.api.domain.dto.report.ReportRequestDTO;
import br.com.rotatoria.api.domain.dto.report.ReportResponseDTO;
import br.com.rotatoria.api.domain.dto.report.ReportStatusDTO;
import br.com.rotatoria.api.domain.model.Report;
import br.com.rotatoria.api.domain.repository.ReportRepository;
import br.com.rotatoria.api.domain.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final AmazonS3Service amazonS3Service;
    private final VideoRepository videoRepository;

    @Value("${amazonProperties.reportBucketName}")
    private String bucketName;

    public ReportService(ReportRepository reportRepository, AmazonS3Service amazonS3Service, VideoRepository videoRepository) {
        this.reportRepository = reportRepository;
        this.amazonS3Service = amazonS3Service;
        this.videoRepository = videoRepository;
    }


    @Transactional
    public ResponseEntity<Report> create(ReportRequestDTO reportRequestDTO) {
        var video = videoRepository.findById(reportRequestDTO.getVideoId())
                .orElseThrow(() -> new EntityNotFoundException("Vídeo não encontrado"));

        System.out.println(video);
        var report = new Report();
        report.setName(reportRequestDTO.getName());
        report.setStatus(reportRequestDTO.getStatus());

        reportRepository.save(report);

        video.setReport(report);
        videoRepository.save(video);

        return ResponseEntity.ok(report);
    }

    public ResponseEntity<ReportResponseDTO> findById(Long id) {
        var report = reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Não foi encontrado um relatório com id = " + id));
        var reportResponseDTO = new ReportResponseDTO();

        reportResponseDTO.setName(report.getName());
        reportResponseDTO.setReportUrl(amazonS3Service.createPresignedGetUrl(report.getName(), bucketName));
        reportResponseDTO.setStatus(report.getStatus());

        return ResponseEntity.ok().body(reportResponseDTO);
    }

    @Transactional
    public ResponseEntity<ReportResponseDTO> updateProcessingStatus(Long id, ReportStatusDTO reportStatusDTO) {
        var report = reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Não foi encontrado na base de dados report com id = " + id));
        var reportResponseDTO = new ReportResponseDTO();

        reportResponseDTO.setName(report.getName());
        reportResponseDTO.setReportUrl(amazonS3Service.createPresignedGetUrl(report.getName(), bucketName));
        reportResponseDTO.setStatus(reportStatusDTO.getStatus());

        return ResponseEntity.ok().body(reportResponseDTO);
    }
    
}
