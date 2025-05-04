ALTER TABLE videos DROP CONSTRAINT fk_videos_relatorio;
ALTER TABLE videos RENAME COLUMN relatorio_id TO report_id;
ALTER TABLE videos
ADD CONSTRAINT fk_videos_report
FOREIGN KEY (report_id)
REFERENCES reports(id)
ON DELETE SET NULL;