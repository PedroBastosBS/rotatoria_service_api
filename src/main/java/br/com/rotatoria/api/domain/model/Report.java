package br.com.rotatoria.api.domain.model;

import br.com.rotatoria.api.domain.model.ENUMS.ProcessingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reports")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String fileName;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

}
