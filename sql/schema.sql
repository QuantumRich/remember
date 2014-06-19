DROP TABLE event;
CREATE TABLE event
(
event_code bigint PRIMARY KEY,
event_name varchar,
event_desc varchar,
event_date timestamp,
tags varchar
);

DROP TABLE picture;
CREATE TABLE picture
(
filename varchar PRIMARY KEY,
event_code int,
original_filename varchar,
date_taken timestamp,
date_uploaded timestamp,
caption varchar,
uploader varchar,
tags varchar,
CONSTRAINT event_code_fk FOREIGN KEY (event_code) 
REFERENCES event(event_code)
);