CREATE TABLE videos (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    extension VARCHAR(20),
    file_submit TIMESTAMP,
    file_size VARCHAR(50),
    file_name VARCHAR(255),
    status VARCHAR(50)
);