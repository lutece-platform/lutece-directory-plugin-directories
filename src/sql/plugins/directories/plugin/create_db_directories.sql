--
-- Structure for table directories_directory
--
DROP TABLE IF EXISTS directories_directory;
CREATE TABLE directories_directory (
id_reference int AUTO_INCREMENT,
name long varchar NOT NULL,
description long varchar NOT NULL,
PRIMARY KEY (id_reference)
);
--
-- Structure for table directories_directory_response
--
DROP TABLE IF EXISTS directories_directory_response;
CREATE TABLE directories_directory_response (
id_directory_response int AUTO_INCREMENT,
id_directory int NOT NULL,
id_response int NOT NULL,
id_entity int NOT NULL,
PRIMARY KEY (id_directory_response)
);
--
-- Structure for table directories_directory_entity
--
DROP TABLE IF EXISTS directories_directory_entity;
CREATE TABLE directories_directory_entity (
id_reference int AUTO_INCREMENT,
id_directory int NOT NULL,
id_creator int NOT NULL,
date_creation timestamp NULL,
id_modificator int DEFAULT NULL,
date_update timestamp NULL,
title long varchar NOT NULL,
PRIMARY KEY (id_reference)
);
