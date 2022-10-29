CREATE TABLE security.oauth_access_token (
	token_id varchar(255),
	"token" bytea,
	authentication_id varchar(255) NOT NULL,
	user_name varchar(255),
	client_id varchar(255),
	authentication bytea,
	refresh_token varchar(255),
	PRIMARY KEY (authentication_id)
);
CREATE TABLE security.oauth_approvals (
	userId varchar(255),
	clientId varchar(255),
	"scope" varchar(255),
	status varchar(10),
	expiresAt timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	lastModifiedAt timestamp
);
CREATE TABLE security.oauth_client_details (
	client_id varchar(255) NOT NULL,
	resource_ids varchar(255),
	client_secret varchar(255),
	"scope" varchar(255),
	authorized_grant_types varchar(255),
	web_server_redirect_uri varchar(255),
	authorities varchar(255),
	access_token_validity int,
	refresh_token_validity int,
	additional_information varchar(4096),
	autoapprove varchar(255),
	PRIMARY KEY (client_id)
);
CREATE TABLE security.oauth_client_token (
	token_id varchar(255),
	"token" bytea,
	authentication_id varchar(255) NOT NULL,
	user_name varchar(255),
	client_id varchar(255),
	PRIMARY KEY (authentication_id)
);
CREATE TABLE security.oauth_code (
	code varchar(255),
	authentication bytea
);
CREATE TABLE security.oauth_refresh_token (
	token_id varchar(255),
	"token" bytea,
	authentication bytea
);

CREATE TABLE security.sec_menu (
	menu_uuid varchar(36) NOT NULL,
	code varchar(200) DEFAULT 'N/A' NOT NULL,
	url text,
	"level" int NOT NULL,
	"ordering" int NOT NULL,
	ordering_str varchar(100),
	icon varchar(100),
	"type" varchar(50) DEFAULT 'main' NOT NULL,
	is_leaf boolean DEFAULT false NOT NULL,
	is_home boolean DEFAULT false NOT NULL,
	is_group boolean DEFAULT false NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	parent_uuid varchar(36),
	PRIMARY KEY (menu_uuid)
);
CREATE TABLE security.sec_menu_i18n (
	menu_i18n_uuid varchar(36) NOT NULL,
	menu_uuid varchar(36) NOT NULL,
	locale_code varchar(10),
	title varchar(100) NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (menu_i18n_uuid)
);
CREATE TABLE security.sec_sys_auth (
	sys_auth_uuid varchar(36) NOT NULL,
	sys_auth_code varchar(50) NOT NULL,
	sys_auth_name varchar(50) NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (sys_auth_uuid)
);
CREATE TABLE security.sec_role (
	role_uuid varchar(36) NOT NULL,
	role_name varchar(50) NOT NULL,
	description text,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	sys_auth_uuid varchar(36) NOT NULL,
	PRIMARY KEY (role_uuid)
);
CREATE TABLE security.sec_function (
	function_uuid varchar(36) NOT NULL,
	menu_uuid varchar(36) NOT NULL,
	role_uuid varchar(36) NOT NULL,
	"access" varchar(30) DEFAULT 'read' NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (function_uuid)
);
CREATE TABLE security.sec_user (
	user_uuid varchar(36) NOT NULL,
	username varchar(50) NOT NULL,
	"password" text,
	account_enabled boolean DEFAULT true NOT NULL,
	account_non_expired boolean DEFAULT true NOT NULL,
	account_non_locked boolean DEFAULT true NOT NULL,
	credentials_non_expired boolean DEFAULT true NOT NULL,
	email varchar(150) NOT NULL,
	fullname varchar(75) NOT NULL,
	provider varchar(100) DEFAULT 'local' NOT NULL,
	verification_code varchar(100),
	verification_expired timestamp,
	activate_code varchar(100),
	activate_expired timestamp,
	raw text,
	authority_default varchar(100),
	"app_code" varchar(50) DEFAULT 'DONGKAP',
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (user_uuid)
);
CREATE TABLE security.sec_r_user_role (
	user_uuid varchar(36) NOT NULL,
	role_uuid varchar(36) NOT NULL
);
CREATE TABLE security.sec_contact_user (
	contact_user_uuid varchar(36) NOT NULL,
	phone_number varchar(20),
	administrative_area_name varchar(255),
	address text,
	country varchar(200),
	country_code varchar(100),
	province varchar(200),
	province_code varchar(100),
	city varchar(200),
	city_code varchar(100),
	district varchar(200),
	district_code varchar(100),
	sub_district varchar(200),
	sub_district_code varchar(100),
	zipcode varchar(200),
	description text,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	user_uuid varchar(36) NOT NULL,
	PRIMARY KEY (contact_user_uuid)
);
CREATE TABLE security.sec_personal_info (
	personal_info_uuid varchar(36) NOT NULL,
	id_number varchar(50),
	gender varchar(20),
	place_of_birth varchar(50),
	date_of_birth date,
	height numeric(5,2),
	weight numeric(5,2),
	blood_type varchar(3),
	image varchar(250),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	user_uuid varchar(36) NOT NULL,
	PRIMARY KEY (personal_info_uuid)
);
CREATE TABLE security.sec_emergency_contact (
	emergency_contact_uuid varchar(36) NOT NULL,
	reference_name varchar(200) NOT NULL,
	reference_address text,
	reference_phone_number varchar(20),
	relationship varchar(50),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	user_uuid varchar(36) NOT NULL,
	PRIMARY KEY (emergency_contact_uuid)
);
CREATE TABLE security.sec_settings (
	settings_uuid varchar(36) NOT NULL,
	locale_code varchar(10) DEFAULT 'en-US' NOT NULL,
	locale_identifier varchar(100) DEFAULT 'English (United States)' NOT NULL,
	locale_icon varchar(100) DEFAULT 'flag-icon flag-icon-us',
	theme varchar(10) DEFAULT 'default' NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	user_uuid varchar(36) NOT NULL,
	PRIMARY KEY (settings_uuid)
);

CREATE TABLE security.sec_corporate (
	corporate_uuid varchar(36) NOT NULL,
	corporate_code varchar(50) NOT NULL,
	corporate_name varchar(255) NOT NULL,
	corporate_non_expired boolean DEFAULT true NOT NULL,
	email varchar(150),
	address text,
	telp_number varchar(20),
	fax_number varchar(20),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (corporate_uuid)
);
CREATE TABLE security.sec_regional (
	regional_uuid varchar(36) NOT NULL,
	regional_name varchar(255) NOT NULL,
	latitude numeric,
	longitude numeric,
	administrative_area_short varchar(255),
	administrative_area_name varchar(255),
	address text,
	telp_number varchar(20),
	fax_number varchar(20),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (regional_uuid)
);
CREATE TABLE security.sec_occupation (
	occupation_uuid varchar(36) NOT NULL,
	occupation_code varchar(50) NOT NULL,
	occupation_name varchar(50) NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (occupation_uuid)
);
CREATE TABLE security.sec_employee (
	employee_uuid varchar(36) NOT NULL,
	id_employee varchar(50) NOT NULL,
	last_educational_level varchar(50) DEFAULT 'N/A',
	id_card_image varchar(250),
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	user_uuid varchar(36) NOT NULL,
	parent_uuid varchar(36),
	occupation_uuid varchar(36) NOT NULL,
	regional_uuid varchar(36) NOT NULL,
	corporate_uuid varchar(36) NOT NULL,
	PRIMARY KEY (employee_uuid)
);

ALTER TABLE security.sec_sys_auth ADD CONSTRAINT sys_auth_code UNIQUE (sys_auth_code);
ALTER TABLE security.sec_user ADD CONSTRAINT username UNIQUE (username);
ALTER TABLE security.sec_user ADD CONSTRAINT email UNIQUE (email);
ALTER TABLE security.sec_personal_info ADD CONSTRAINT id_number UNIQUE (id_number);
ALTER TABLE security.sec_corporate ADD CONSTRAINT corporate_code UNIQUE (corporate_code);

ALTER TABLE security.sec_menu
	ADD FOREIGN KEY (parent_uuid) 
	REFERENCES security.sec_menu (menu_uuid);

ALTER TABLE security.sec_menu_i18n
	ADD FOREIGN KEY (menu_uuid) 
	REFERENCES security.sec_menu (menu_uuid);

ALTER TABLE security.sec_role
	ADD FOREIGN KEY (sys_auth_uuid) 
	REFERENCES security.sec_sys_auth (sys_auth_uuid);

ALTER TABLE security.sec_function
	ADD FOREIGN KEY (role_uuid) 
	REFERENCES security.sec_role (role_uuid);

ALTER TABLE security.sec_function
	ADD FOREIGN KEY (menu_uuid) 
	REFERENCES security.sec_menu (menu_uuid);

ALTER TABLE security.sec_r_user_role
	ADD FOREIGN KEY (role_uuid) 
	REFERENCES security.sec_role (role_uuid);

ALTER TABLE security.sec_r_user_role
	ADD FOREIGN KEY (user_uuid) 
	REFERENCES security.sec_user (user_uuid);

ALTER TABLE security.sec_contact_user
	ADD FOREIGN KEY (user_uuid) 
	REFERENCES security.sec_user (user_uuid);

ALTER TABLE security.sec_personal_info
	ADD FOREIGN KEY (user_uuid) 
	REFERENCES security.sec_user (user_uuid);

ALTER TABLE security.sec_emergency_contact
	ADD FOREIGN KEY (user_uuid) 
	REFERENCES security.sec_user (user_uuid);

ALTER TABLE security.sec_settings
	ADD FOREIGN KEY (user_uuid) 
	REFERENCES security.sec_user (user_uuid);

ALTER TABLE security.sec_employee
	ADD FOREIGN KEY (user_uuid) 
	REFERENCES security.sec_user (user_uuid);

ALTER TABLE security.sec_employee
	ADD FOREIGN KEY (parent_uuid) 
	REFERENCES security.sec_employee (employee_uuid);

ALTER TABLE security.sec_employee
	ADD FOREIGN KEY (occupation_uuid) 
	REFERENCES security.sec_occupation (occupation_uuid);

ALTER TABLE security.sec_employee
	ADD FOREIGN KEY (regional_uuid) 
	REFERENCES security.sec_regional (regional_uuid);

ALTER TABLE security.sec_employee
	ADD FOREIGN KEY (corporate_uuid) 
	REFERENCES security.sec_corporate (corporate_uuid);

GRANT ALL ON TABLE security.oauth_access_token TO dongkap;

GRANT ALL ON TABLE security.oauth_approvals TO dongkap;

GRANT ALL ON TABLE security.oauth_client_details TO dongkap;

GRANT ALL ON TABLE security.oauth_client_token TO dongkap;

GRANT ALL ON TABLE security.oauth_code TO dongkap;

GRANT ALL ON TABLE security.oauth_refresh_token TO dongkap;

GRANT ALL ON TABLE security.sec_menu TO dongkap;

GRANT ALL ON TABLE security.sec_menu_i18n TO dongkap;

GRANT ALL ON TABLE security.sec_sys_auth TO dongkap;

GRANT ALL ON TABLE security.sec_role TO dongkap;

GRANT ALL ON TABLE security.sec_function TO dongkap;

GRANT ALL ON TABLE security.sec_user TO dongkap;

GRANT ALL ON TABLE security.sec_r_user_role TO dongkap;

GRANT ALL ON TABLE security.sec_contact_user TO dongkap;

GRANT ALL ON TABLE security.sec_personal_info TO dongkap;

GRANT ALL ON TABLE security.sec_emergency_contact TO dongkap;

GRANT ALL ON TABLE security.sec_settings TO dongkap;

GRANT ALL ON TABLE security.sec_regional TO dongkap;

GRANT ALL ON TABLE security.sec_corporate TO dongkap;

GRANT ALL ON TABLE security.sec_occupation TO dongkap;

GRANT ALL ON TABLE security.sec_employee TO dongkap;
