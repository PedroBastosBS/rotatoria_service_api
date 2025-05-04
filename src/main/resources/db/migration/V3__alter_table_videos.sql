ALTER TABLE videos
ADD COLUMN relatorio_id BIGINT,
ADD CONSTRAINT fk_videos_relatorio
    FOREIGN KEY (relatorio_id)
    REFERENCES reports(id)
    ON DELETE SET NULL;

