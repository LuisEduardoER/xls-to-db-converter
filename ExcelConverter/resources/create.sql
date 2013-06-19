SET character_set_client='utf8';
SET character_set_results='utf8';
SET collation_connection='utf8_general_ci';

DROP DATABASE IF EXISTS dbOrg;
CREATE DATABASE dbOrg;
USE dbOrg;

CREATE TABLE OrgType(
type BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
title VARCHAR(20) NOT NULL,
fullname VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Org(
id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
num VARCHAR(12),
type BIGINT NOT NULL ,
name VARCHAR(255),
address VARCHAR(255),
unp INT,
okpo BIGINT,
account BIGINT,
is_net BOOLEAN,
INDEX type_ind (type),
	FOREIGN KEY (type)
	REFERENCES OrgType(type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO OrgType (title, fullname) VALUE("ООО", "Общество с ограниченной ответственностью");
INSERT INTO OrgType (title, fullname) VALUE("ИП", "Индивидуальный предприниматель"); 
INSERT INTO OrgType (title, fullname) VALUE("ЧУП", "Частное унитарное предприятие");

DESCRIBE Org;
DESCRIBE OrgType;
SELECT * FROM OrgType;

