package br.com.rotatoria.api.domain.service;

import br.com.rotatoria.api.domain.dto.video.*;
import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import br.com.rotatoria.api.domain.model.Video;
import br.com.rotatoria.api.domain.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VideoService {

    private final AmazonS3Service amazonS3Service;
    private final SqsService sqsService;
    private final VideoRepository videoRepository;

    @Value("${amazonProperties.processedBucketName}")
    private String processedBucketName;

    @Value("${amazonProperties.videoBucketName}")
    private String videoBucketName;

    public VideoService(AmazonS3Service amazonS3Service, SqsService sqsService, VideoRepository videoRepository) {
        this.amazonS3Service = amazonS3Service;
        this.sqsService = sqsService;
        this.videoRepository = videoRepository;
    }

    @Transactional
    public ResponseEntity<Video> create(VideoRequestDTO videoRequestDTO) {
        var video = new Video();

        video.setTitle(videoRequestDTO.getTitle());
        video.setFileSize(videoRequestDTO.getFileSize());
        video.setExtension(videoRequestDTO.getExtension());
        video.setFileSubmit(LocalDateTime.now());
        video.setStatus(ProcessingStatus.IN_PROGRESS);
        video.setFileName(videoRequestDTO.getFileName());
        videoRepository.save(video);
        sqsService.sendVideoMessage(video);

        return ResponseEntity.ok().body(video);
    }

    public ResponseEntity<Page<ListVideosDTO>> findAll(Pageable pageable) {
        var page = videoRepository.findAll(pageable)
                .map(video -> {
                    var dto = new ListVideosDTO();
                    dto.setId(video.getId());
                    dto.setTitle(video.getTitle());
                    dto.setProcessingDate(video.getProcessingDate());
                    dto.setStatus(video.getStatus());
                    dto.setReportId(video.getReport() != null ? video.getReport().getId() : null);
                    return dto;
                });

        return ResponseEntity.ok(page);
    }

    public ResponseEntity<VideoResponseDTO> findById(Long id) {
        var video = videoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Não foi encontrado na base de dados video com id = " + id));
        var videoResponseDTO = new VideoResponseDTO();

        videoResponseDTO.setId(video.getId());
        videoResponseDTO.setTitle(video.getTitle());
        videoResponseDTO.setProcessingDate(video.getProcessingDate());
        videoResponseDTO.setExtension(video.getExtension());
        videoResponseDTO.setFileSize(video.getFileSize());
        videoResponseDTO.setStatus(video.getStatus());

        if(video.getStatus().equals(ProcessingStatus.IN_PROGRESS) || video.getStatus().equals(ProcessingStatus.FAILED)) {
            videoResponseDTO.setVideoUrl(amazonS3Service.createPresignedGetUrl(video.getFileName(), videoBucketName));
        } else {
            videoResponseDTO.setVideoUrl(amazonS3Service.createPresignedGetUrl(video.getFileName(), processedBucketName));
        }

        return ResponseEntity.ok().body(videoResponseDTO);
    }

    @Transactional
    public ResponseEntity<VideoResponseDTO> updateProcessingStatus(Long id, VideoStatusDTO videoStatusDTO) {
        var video = videoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Não foi encontrado na base de dados video com id = " + id));
        video.setStatus(videoStatusDTO.getStatus());
        videoRepository.save(video);

        var videoResponseDTO = new VideoResponseDTO();

        videoResponseDTO.setId(video.getId());
        videoResponseDTO.setTitle(video.getTitle());
        videoResponseDTO.setVideoUrl(amazonS3Service.createPresignedGetUrl(video.getFileName(), processedBucketName));
        videoResponseDTO.setProcessingDate(video.getProcessingDate());
        videoResponseDTO.setExtension(video.getExtension());
        videoResponseDTO.setFileSize(video.getFileSize());
        videoResponseDTO.setStatus(video.getStatus());

        return ResponseEntity.ok().body(videoResponseDTO);
    }

    public PresignedPutUrlDTO getPresignedPutUrl(String fileName, boolean isProcessed) {
        if(isProcessed) {
            return amazonS3Service.createPresignedPutUrl(fileName, processedBucketName, ".mp4");
        }
        return amazonS3Service.createPresignedPutUrl(fileName, videoBucketName, ".mp4");
    }
}
