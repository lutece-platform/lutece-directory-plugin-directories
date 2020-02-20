--
-- Data for table genatt_entry_type
--
INSERT  INTO genatt_entry_type (id_type,title,is_group,is_comment,is_mylutece_user,class_name,icon_name,plugin) VALUES
(201,'Bouton radio',0,0,0,'directories.entryTypeRadioButton',NULL,'directories'),
(202,'Case à cocher',0,0,0,'directories.entryTypeCheckBox',NULL,'directories'),
(203,'Commentaire',0,1,0,'directories.entryTypeComment',NULL,'directories'),
(204,'Date',0,0,0,'directories.entryTypeDate',NULL,'directories'),
(205,'Liste déroulante',0,0,0,'directories.entryTypeSelect',NULL,'directories'),
(206,'Zone de texte court',0,0,0,'directories.entryTypeText',NULL,'directories'),
(207,'Zone de texte long',0,0,0,'directories.entryTypeTextArea',NULL,'directories'),
(208,'Numérotation',0,0,0,'directories.entryTypeNumbering',NULL,'directories'),
(209,'Regroupement',1,0,0,'directories.entryTypeGroup',NULL,'directories'),
(210,'Liste déroulante SQL',0,0,0,'directories.entryTypeSelectSQL',NULL,'directories'),
(211,'Géolocalisation',0,0,0,'directories.entryTypeGeolocation',NULL,'directories'),
(212,'Session',0,0,0,'directories.entryTypeSession',NULL,'directories'),
(214,'Image',0,0,0,'directories.entryTypeImage',NULL,'directories'),
(215,'Fichier',0,0,0,'directories.entryTypeFile',NULL,'directories'),
(216,'Numéro de téléphone',0,0,0,'directories.entryTypePhone',NULL,'directories');