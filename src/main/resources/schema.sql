-- Erstelle die Sequenz users_id_seq, falls sie nicht existiert
CREATE SEQUENCE IF NOT EXISTS users_id_seq;

-- Erstelle die users-Tabelle, falls sie nicht existiert
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT DEFAULT nextval('users_id_seq') PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(50),
                                     enabled BOOLEAN
);

-- Verknüpfe die users_id_seq mit der id-Spalte in users
ALTER SEQUENCE users_id_seq OWNED BY users.id;

-- Erstelle die Sequenz termine_id_seq, falls sie nicht existiert
CREATE SEQUENCE IF NOT EXISTS termine_id_seq;

-- Erstelle die termine-Tabelle, falls sie nicht existiert
CREATE TABLE IF NOT EXISTS termine (
                                       id BIGINT DEFAULT nextval('termine_id_seq') PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
                                       description VARCHAR(255),
                                       start_time TIMESTAMP NOT NULL,
                                       end_time TIMESTAMP NOT NULL,
                                       organizer_id BIGINT,
                                       CONSTRAINT fk_organizer FOREIGN KEY (organizer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Verknüpfe die termine_id_seq mit der id-Spalte in termine
ALTER SEQUENCE termine_id_seq OWNED BY termine.id;


-- Erstelle die einladungen-Tabelle, falls sie nicht existiert
CREATE SEQUENCE IF NOT EXISTS einladungen_id_seq;
CREATE TABLE IF NOT EXISTS einladungen (
                                           id BIGINT DEFAULT nextval('einladungen_id_seq') PRIMARY KEY,
                                           termin_id BIGINT NOT NULL,
                                           empfänger_id BIGINT NOT NULL,
                                           status VARCHAR(50) DEFAULT 'PENDING',
                                           accepted BOOLEAN DEFAULT FALSE,  -- ✅ Hier wird die Spalte direkt ins Schema aufgenommen
                                           CONSTRAINT fk_termin FOREIGN KEY (termin_id) REFERENCES termine(id) ON DELETE CASCADE,
                                           CONSTRAINT fk_empfänger FOREIGN KEY (empfänger_id) REFERENCES users(id) ON DELETE CASCADE
);
-- Verknüpfe die einladungen_id_seq mit der id-Spalte in einladungen
ALTER SEQUENCE einladungen_id_seq OWNED BY einladungen.id;

CREATE TABLE IF NOT EXISTS system_logs (
                                           id SERIAL PRIMARY KEY,
                                           timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           admin_username VARCHAR(50),
                                           action TEXT,
                                           details TEXT
);
DELETE FROM system_logs
WHERE id NOT IN (SELECT id FROM system_logs ORDER BY timestamp DESC LIMIT 500);
