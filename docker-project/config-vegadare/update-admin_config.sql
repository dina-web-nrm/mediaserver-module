update media.ADMIN_CONFIG set admin_value = "/opt/data/media/" 		where admin_key="path_to_files";
update media.ADMIN_CONFIG set admin_value = "https://api.nrm.se" 	where admin_key="mediaserver_host";
update media.ADMIN_CONFIG set admin_value = "media" 			where admin_key="relative_new_stream_url";
