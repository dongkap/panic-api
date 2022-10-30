CREATE TABLE panic.panic_location (
	location_uuid varchar(36) NOT NULL,
	latitude numeric NOT NULL,
	longitude numeric NOT NULL,
	formatted_address text,
	province varchar(250),
	city varchar(250),
	district varchar(250),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (location_uuid)
);
CREATE TABLE panic.panic_device (
	device_uuid varchar(36) NOT NULL,
	device_id varchar(100),
	device_name varchar(100),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (device_uuid)
);
CREATE TABLE panic.panic_report (
	panic_uuid varchar(36) NOT NULL,
	panic_code varchar(255) NOT NULL,
	username varchar(50) NOT NULL,
	fullname varchar(75) NOT NULL,
	gender varchar(20) NOT NULL,
	age int NOT NULL,
	phone_number varchar(20) NOT NULL,
	id_number varchar(50) NOT NULL,
	month_report varchar(20) NOT NULL,
	year_report int NOT NULL,
	administrative_area_short varchar(255),
	latest_latitude numeric NOT NULL,
	latest_longitude numeric NOT NULL,
	latest_formatted_address text,
	latest_province varchar(250),
	latest_city varchar(250),
	latest_district varchar(250),
	latest_file_checksum varchar(36),
	latest_device_id varchar(100),
	latest_device_name varchar(100),
	emergency_category varchar(200),
	status varchar(200),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (panic_uuid)
);
CREATE TABLE panic.panic_detail (
	panic_detail_uuid varchar(36) NOT NULL,
	file_checksum varchar(36),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	panic_uuid varchar(36) NOT NULL,
	device_uuid varchar(36),
	location_uuid varchar(36),
	PRIMARY KEY (panic_detail_uuid)
);
CREATE TABLE panic.fake_report (
	fake_uuid varchar(36) NOT NULL,
	fake_code varchar(255) NOT NULL,
	username varchar(50) NOT NULL,
	fullname varchar(75) NOT NULL,
	gender varchar(20) NOT NULL,
	age int NOT NULL,
	phone_number varchar(20) NOT NULL,
	id_number varchar(50) NOT NULL,
	month_report varchar(20) NOT NULL,
	year_report int NOT NULL,
	administrative_area_short varchar(255),
	latest_latitude numeric NOT NULL,
	latest_longitude numeric NOT NULL,
	latest_formatted_address text,
	latest_province varchar(250),
	latest_city varchar(250),
	latest_district varchar(250),
	latest_file_checksum varchar(36),
	latest_device_id varchar(100),
	latest_device_name varchar(100),
	emergency_category varchar(200),
	status varchar(200),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (fake_uuid)
);
CREATE TABLE panic.fake_detail (
	fake_detail_uuid varchar(36) NOT NULL,
	file_checksum varchar(36),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	fake_uuid varchar(36) NOT NULL,
	device_uuid varchar(36),
	location_uuid varchar(36),
	PRIMARY KEY (fake_detail_uuid)
);

ALTER TABLE panic.panic_report ADD CONSTRAINT panic_code UNIQUE (panic_code);
ALTER TABLE panic.fake_report ADD CONSTRAINT fake_code UNIQUE (fake_code);

ALTER TABLE panic.panic_detail
	ADD FOREIGN KEY (panic_uuid) 
	REFERENCES panic.panic_report (panic_uuid);

ALTER TABLE panic.panic_detail
	ADD FOREIGN KEY (device_uuid) 
	REFERENCES panic.panic_device (device_uuid);

ALTER TABLE panic.panic_detail
	ADD FOREIGN KEY (location_uuid) 
	REFERENCES panic.panic_location (location_uuid);

ALTER TABLE panic.fake_detail
	ADD FOREIGN KEY (fake_uuid) 
	REFERENCES panic.fake_report (fake_uuid);

ALTER TABLE panic.fake_detail
	ADD FOREIGN KEY (device_uuid) 
	REFERENCES panic.panic_device (device_uuid);

ALTER TABLE panic.fake_detail
	ADD FOREIGN KEY (location_uuid) 
	REFERENCES panic.panic_location (location_uuid);

GRANT ALL ON TABLE panic.panic_location TO dongkap;

GRANT ALL ON TABLE panic.panic_device TO dongkap;

GRANT ALL ON TABLE panic.panic_report TO dongkap;

GRANT ALL ON TABLE panic.panic_detail TO dongkap;

GRANT ALL ON TABLE panic.fake_report TO dongkap;

GRANT ALL ON TABLE panic.fake_detail TO dongkap;
