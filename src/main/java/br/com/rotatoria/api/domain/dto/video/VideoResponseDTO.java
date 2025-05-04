package br.com.rotatoria.api.domain.dto.video;

import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import lombok.Data;

@Data
public class VideoResponseDTO {

    private Long id;
    private String title;
    private String videoUrl;
    private String processingDate;
    private String extension;
    private String fileSize;
    private ProcessingStatus status;
}




