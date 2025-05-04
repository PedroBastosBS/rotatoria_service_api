package br.com.rotatoria.api.domain.dto.video;

import lombok.Data;

@Data
public class VideoRequestDTO {

    private String title;
    private String fileSize;
    private String extension;
    private String fileName;

}
