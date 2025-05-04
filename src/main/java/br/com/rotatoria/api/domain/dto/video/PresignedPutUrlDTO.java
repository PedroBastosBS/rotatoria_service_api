package br.com.rotatoria.api.domain.dto.video;

import lombok.Data;

@Data
public class PresignedPutUrlDTO {

    private String fileName;
    private String url;

}
