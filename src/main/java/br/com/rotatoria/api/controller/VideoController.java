package br.com.rotatoria.api.controller;

import br.com.rotatoria.api.domain.dto.video.*;
import br.com.rotatoria.api.domain.model.Video;
import br.com.rotatoria.api.domain.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<Video> uploadVideoToS3(@RequestBody VideoRequestDTO videoRequestDTO) {
        return videoService.uploadVideoToS3(videoRequestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponseDTO> downloadProcessedVideo(@PathVariable Long id) {
        return videoService.downloadProcessedVideo(id);
    }

    @GetMapping
    public ResponseEntity<Page<ListVideosDTO>> findAll(Pageable pageable) {
        return videoService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VideoResponseDTO> updateProcessingStatus(@PathVariable Long id, @RequestBody VideoStatusDTO videoStatusDTO) {
        return videoService.updateProcessingStatus(id, videoStatusDTO);
    }

    @GetMapping("submit_file")
    public ResponseEntity<PresignedPutUrlDTO> getPresignedPutUrl(@RequestParam String fileName) {
        return ResponseEntity.ok(videoService.getPresignedPutUrl(fileName));
    }
}
