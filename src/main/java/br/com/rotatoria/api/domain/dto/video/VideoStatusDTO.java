package br.com.rotatoria.api.domain.dto.video;

import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import lombok.Data;

@Data
public class VideoStatusDTO {

    private ProcessingStatus status;

}
