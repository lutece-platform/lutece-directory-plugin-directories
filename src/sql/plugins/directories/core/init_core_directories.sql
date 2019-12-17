--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DIRECTORIES_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DIRECTORIES_MANAGEMENT','directories.adminFeature.DirectoriesManager.name',1,'jsp/admin/plugins/directories/ManageDirectories.jsp','directories.adminFeature.DirectoriesManager.description',0,'directories',NULL,NULL,NULL,4);
--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DIRECTORIES_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DIRECTORIES_MANAGEMENT',1);
--
-- Data for table genatt_entry_type
--
INSERT  INTO genatt_entry_type (id_type,title,is_group,is_comment,is_mylutece_user,class_name,icon_name,plugin) VALUES
(206,'Zone de texte court',0,0,0,'directories.entryTypeText',NULL,'directories');