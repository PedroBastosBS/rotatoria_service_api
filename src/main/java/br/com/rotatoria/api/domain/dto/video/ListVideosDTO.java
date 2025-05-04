package br.com.rotatoria.api.domain.dto.video;

import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import lombok.Data;

@Data
public class ListVideosDTO {

    private Long id;
    private String title;
    private String processingDate;
    private ProcessingStatus status;
    private Long reportId;

}
