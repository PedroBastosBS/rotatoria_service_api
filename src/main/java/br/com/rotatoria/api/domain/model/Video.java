package br.com.rotatoria.api.domain.model;

import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "videos")
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String extension;
    private LocalDateTime fileSubmit;
    private String fileSize;
    private String fileName;

    @Enumerated(value = EnumType.STRING)
    private ProcessingStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private Report report;

    public String getProcessingDate() {
        if (fileSubmit == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fileSubmit.format(formatter);
    }

}
