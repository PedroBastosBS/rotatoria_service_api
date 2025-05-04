package br.com.rotatoria.api.domain.repository;

import br.com.rotatoria.api.domain.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface VideoRepository extends JpaRepository<Video, Long> {
}
