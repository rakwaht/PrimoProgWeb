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
VALUES ('federico','federico');
INSERT INTO utente (username,password)
VALUES ('davide','davide');
INSERT INTO utente (username,password)
VALUES ('francesco','francesco');
INSERT INTO utente (username,password)
VALUES ('susanna','susanna');
INSERT INTO utente (username,password)
VALUES ('giovanna','giovanna');


INSERT INTO gruppo (nome,id_proprietario,data_creazione,descrizione)
VALUES ('[Programmazione per il Web]',1,'2013-01-16 alle 14:50:11','In questo gruppo potrete chiedere informazioni ed aiuto per questo corso.');
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (1,1);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (1,2);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (1,3);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (1,4);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (1,5);
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(2,'2013-11-15 alle 19:20:59',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(3,'2013-11-16 alle 19:20:50',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-17 alle 19:12:51',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-19 alle 19:11:10',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(2,'2013-11-15 alle 19:10:44',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(4,'2013-11-11 alle 19:00:60',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(3,'2013-11-27 alle 19:00:12',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-27 alle 18:20:56',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-17 alle 18:14:23',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(2,'2013-10-19 alle 18:10:52',1,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');






INSERT INTO gruppo (nome,id_proprietario,data_creazione,descrizione)
VALUES ('[Linguaggi formali e compilatori]',3,'2013-10-11 alle 19:20:59','In questo gruppo potrete chiedere informazioni ed aiuto per questo corso.');
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (2,1);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (2,3);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (2,5);
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-15 alle 19:20:59',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(3,'2013-11-16 alle 19:20:50',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-17 alle 19:12:51',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(3,'2013-11-19 alle 19:11:10',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-15 alle 19:10:44',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-11 alle 19:00:60',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-27 alle 19:00:12',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-27 alle 18:20:56',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(3,'2013-11-17 alle 18:14:23',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-10-19 alle 18:10:52',2,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');





INSERT INTO gruppo (nome,id_proprietario,data_creazione,descrizione)
VALUES ('[Human Computer Interaction]',5,'1969-08-09 alle 23:33:10','In questo gruppo potrete chiedere informazioni ed aiuto per questo corso.');
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (3,1);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (3,2);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (3,4);
INSERT INTO gruppo_utente (id_gruppo,id_utente)
VALUES (3,5);
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-15 alle 19:20:59',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(2,'2013-11-16 alle 19:20:50',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-17 alle 19:12:51',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(4,'2013-11-19 alle 19:11:10',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-11-15 alle 19:10:44',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-11 alle 19:00:60',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(4,'2013-11-27 alle 19:00:12',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(5,'2013-11-27 alle 18:20:56',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(2,'2013-11-17 alle 18:14:23',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');
INSERT INTO post(id_scrivente,data_creazione,id_gruppo,testo)
VALUES(1,'2013-10-19 alle 18:10:52',3,'Bacon ipsum dolor sit amet shankle kielbasa doner, short loin ut venison salami fatback fugiat nostrud sunt ad meatloaf. Corned beef pork brisket, kevin venison shankle non cillum et swine. Turducken aute cillum salami est incididunt shoulder sausage consectetur dolore corned beef. Biltong sirloin quis, veniam in short ribs flank fatback spare ribs. Chicken frankfurter dolore, irure rump labore tenderloin reprehenderit adipisicing.');


