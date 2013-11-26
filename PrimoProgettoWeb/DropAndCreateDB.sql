DROP TABLE file;
DROP TABLE gruppo_utente;
DROP TABLE post;
DROP TABLE invito;
DROP TABLE gruppo;
DROP TABLE utente;

CREATE TABLE utente(
 id_utente INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
username VARCHAR(100),
password VARCHAR(100),
nome_avatar VARCHAR(100),
utente_abilitato BOOLEAN DEFAULT true,
PRIMARY KEY (id_utente)
);

CREATE TABLE gruppo(
 id_gruppo INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
nome VARCHAR(100),
id_proprietario INT,
data_creazione VARCHAR(30),
descrizione VARCHAR(1000),
gruppo_abilitato BOOLEAN  DEFAULT true,
PRIMARY KEY (id_gruppo),
FOREIGN KEY (id_proprietario) REFERENCES utente(id_utente)
);

CREATE TABLE gruppo_utente(
id_gruppo INT,
id_utente INT,
gruppo_utente_abilitato BOOLEAN DEFAULT true,
PRIMARY KEY (id_gruppo,id_utente),
FOREIGN KEY (id_gruppo) REFERENCES gruppo(id_gruppo),
FOREIGN KEY (id_utente) REFERENCES utente(id_utente)
);

CREATE TABLE post(
id_post INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
testo LONG VARCHAR,
id_scrivente INT,
data_creazione VARCHAR(30),
id_gruppo INT,
PRIMARY KEY (id_post),
FOREIGN KEY (id_gruppo) REFERENCES gruppo(id_gruppo),
FOREIGN KEY (id_scrivente) REFERENCES utente(id_utente)
);



CREATE TABLE file(
id_file INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
nome_file VARCHAR(100),
id_post INT,
PRIMARY KEY (id_file),
FOREIGN KEY (id_post) REFERENCES post(id_post)
);

CREATE TABLE invito(
id_invito INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
id_invitato INT,
id_invitante INT,
id_gruppo INT,
invito_abilitato BOOLEAN DEFAULT true, 
PRIMARY KEY (id_invito),
FOREIGN KEY (id_invitato) REFERENCES utente(id_utente),
FOREIGN KEY (id_invitante) REFERENCES utente(id_utente),
FOREIGN KEY (id_gruppo) REFERENCES gruppo(id_gruppo)
);

INSERT INTO utente (username,password)
VALUES ('pippo','pippa');
INSERT INTO utente (username,password)
VALUES ('gino','perna');
INSERT INTO utente (username,password)
VALUES ('giovanni','rossi');
INSERT INTO utente (username,password)
VALUES ('susanna','panna');
INSERT INTO utente (username,password)
VALUES ('giovanna','panna');
