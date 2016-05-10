/* Insert Corvus corpus - image & Pica Pica - image with some tags ....  
 Don't forget to copy the images ( path+uuid-name ) to the correct path
*/;
INSERT INTO MEDIA VALUES 
('863ec044-17cf-4c87-81cc-783ab13230ae','Image',NULL,'image/jpeg','ingimar','public','view:sitting&country:sweden&photo:diginatour&source:wiki','http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae?format=image/jpeg',NULL,NULL),
('e4a3cf7d-add4-4949-a6ce-0f5594e61970','Image',NULL,'image/jpeg','ingimar','public','view:sitting&country:sweden&photo:xxberraschungsbilder&source:wiki','http://localhost:8080/MediaServerResteasy/media/e4a3cf7d-add4-4949-a6ce-0f5594e61970?format=image/jpeg',NULL,NULL), 
('c41bd445-8796-4421-9b77-fd1e65b14974','Image',NULL,'image/jpeg','ingimar','private','view:flying&country:sweden','http://localhost:8080/MediaServerResteasy/media/c41bd445-8796-4421-9b77-fd1e65b14974?format=image/jpeg',NULL,NULL);

/*'0' för postgres , 0 för mysql*/
INSERT INTO IMAGE VALUES 
('863ec044-17cf-4c87-81cc-783ab13230ae','0',null),
('e4a3cf7d-add4-4949-a6ce-0f5594e61970','0',null),
('c41bd445-8796-4421-9b77-fd1e65b14974','0',null);

INSERT INTO TAGS VALUES 
(1,'country','sweden','e4a3cf7d-add4-4949-a6ce-0f5594e61970','2014-01-27 11:58:58'),
(2,'source','wiki','e4a3cf7d-add4-4949-a6ce-0f5594e61970','2014-01-27 11:58:58'),
(3,'photo','uberraschungsbilder','e4a3cf7d-add4-4949-a6ce-0f5594e61970','2014-01-27 11:58:58'),
(4,'view','sitting','e4a3cf7d-add4-4949-a6ce-0f5594e61970','2014-01-27 11:58:58'),
(5,'country','sweden','863ec044-17cf-4c87-81cc-783ab13230ae','2014-01-27 11:58:58'),
(6,'source','wiki','863ec044-17cf-4c87-81cc-783ab13230ae','2014-01-27 11:58:58'),
(7,'photo','diginatour','863ec044-17cf-4c87-81cc-783ab13230ae','2014-01-27 11:58:58'),
(8,'view','sitting','863ec044-17cf-4c87-81cc-783ab13230ae','2014-01-27 11:58:58'),
(9,'country','sweden','c41bd445-8796-4421-9b77-fd1e65b14974','2014-01-27 11:58:58'),
(10,'view','flying','c41bd445-8796-4421-9b77-fd1e65b14974','2014-01-27 11:58:58');

INSERT INTO DETERMINATION VALUES 
(1,'taxon','02d2ef271-02ca-4934-860c-6c6a4ed043f9','mock-system','http','863ec044-17cf-4c87-81cc-783ab13230ae',null,1),
(2,'taxon','1089d789a-2df5-4882-abce-76d4f22e0a7a','mock-system','http','e4a3cf7d-add4-4949-a6ce-0f5594e61970',null,2),
(3,'taxon','02d2ef271-02ca-4934-860c-6c6a4ed043f9','mock-system','http','c41bd445-8796-4421-9b77-fd1e65b14974',null,3);

INSERT INTO MEDIA_TEXT VALUES (1,'Sittande skata','sv_SE','863ec044-17cf-4c87-81cc-783ab13230ae','-');
INSERT INTO MEDIA_TEXT VALUES (2,'Flygande skata','sv_SE','c41bd445-8796-4421-9b77-fd1e65b14974','-');
INSERT INTO MEDIA_TEXT VALUES (3,'korp','sv_SE','e4a3cf7d-add4-4949-a6ce-0f5594e61970','-');
