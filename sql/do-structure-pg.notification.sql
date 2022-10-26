CREATE TABLE "notification".web_subscription ( 
	web_subscription_uuid varchar(36) NOT NULL,
	endpoint text NOT NULL,
	expiration_time int,
	p256dh text NOT NULL,
	auth text NOT NULL,
	user_name varchar(50) NOT NULL,
	subscribed boolean DEFAULT false NOT NULL,
	"version" int DEFAULT 0 NOT NULL,
	is_active boolean DEFAULT true NOT NULL,
	created_date timestamp DEFAULT CURRENT_TIMESTAMP,
	created_by varchar(150),
	modified_date timestamp,
	modified_by varchar(150),
	PRIMARY KEY (web_subscription_uuid)
);
ALTER TABLE "notification".web_subscription ADD CONSTRAINT endpoint UNIQUE (endpoint);

GRANT ALL ON TABLE "notification".web_subscription TO dongkap;