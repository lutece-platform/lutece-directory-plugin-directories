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
(209,'Géolocalisation',0,0,0,'directories.entryTypeGeolocation',NULL,'directories'),
(210,'Image',0,0,0,'directories.entryTypeImage',NULL,'directories'),
(211,'Fichier',0,0,0,'directories.entryTypeFile',NULL,'directories'),
(212,'Numéro de téléphone',0,0,0,'directories.entryTypePhone',NULL,'directories'),
(213,'Attribut MyLutece',0,0,0,'directories.entryTypeMyLuteceAttribute',NULL,'directories'),
(214,'Sélecteur d\'utilisateur lutece',0,0,0,'directories.entryTypeMyLuteceUserPicker',NULL,'directories');