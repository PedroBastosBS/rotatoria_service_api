package br.com.rotatoria.api.domain.dto.report;

import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import lombok.Data;

@Data
public class ReportStatusDTO {

    private ProcessingStatus status;

}
