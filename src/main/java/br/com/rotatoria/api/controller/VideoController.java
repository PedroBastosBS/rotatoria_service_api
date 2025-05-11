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
    public ResponseEntity<Video> create(@RequestBody VideoRequestDTO videoRequestDTO) {
        return videoService.create(videoRequestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponseDTO> findById(@PathVariable Long id) {
        return videoService.findById(id);
    }

    @GetMapping
    public ResponseEntity<Page<ListVideosDTO>> findAll(Pageable pageable) {
        return videoService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VideoResponseDTO> updateProcessingStatus(@PathVariable Long id, @RequestBody VideoStatusDTO videoStatusDTO) {
        System.out.println("atualizando status de processamento...");
        return videoService.updateProcessingStatus(id, videoStatusDTO);
    }

    @GetMapping("submit_file")
    public ResponseEntity<PresignedPutUrlDTO> getPresignedPutUrl(@RequestParam String fileName, @RequestParam(required = false) boolean isProcessed) {
        return ResponseEntity.ok(videoService.getPresignedPutUrl(fileName, isProcessed));
    }

}
