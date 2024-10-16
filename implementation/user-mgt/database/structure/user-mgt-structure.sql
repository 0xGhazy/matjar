-- USER-MANAGEMENT SCHEMA DEFINITION
CREATE SCHEMA user_management AUTHORIZATION postgres;

-- "USER GENDER" TABLE DEFINITION
CREATE TABLE user_management.gender (
	"name" varchar(50) NOT NULL,
	description varchar(255) NULL,
	CONSTRAINT gender_pk PRIMARY KEY (name)
);

-- "USER STATUS" TABLE DEFINITION
CREATE TABLE user_management.user_status (
	"name" varchar(50) NOT NULL,
	description varchar(255) NULL,
	CONSTRAINT status_pk PRIMARY KEY (name)
);

-- USER TABLE DEFINITION
CREATE TABLE user_management."user" (
	id varchar(255) NOT NULL,
	email varchar(255) NULL,
	"password" varchar(255) NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	status varchar(255) NULL,
	phone varchar(255) NULL,
	gender varchar(255) NULL,
	date_of_birth date NULL,
	created_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,
	last_login_time timestamp(6) NULL,
	CONSTRAINT email_unique_constraint UNIQUE (email),
	CONSTRAINT user_pk PRIMARY KEY (id)
);
ALTER TABLE user_management."user" ADD CONSTRAINT user_gender_fk FOREIGN KEY (gender) REFERENCES user_management.gender("name");
ALTER TABLE user_management."user" ADD CONSTRAINT user_status_fk FOREIGN KEY (status) REFERENCES user_management.user_status("name");

-- CODE STATUS TABLE DEFINITION
CREATE TABLE user_management.code_status (
	"name" varchar NOT NULL,
	description varchar NULL,
	CONSTRAINT code_status_pk PRIMARY KEY ("name")
);

-- CODE TABLE DEFINITION
CREATE TABLE user_management.code (
    id varchar(255) NOT NULL,
    code varchar(255) NOT NULL UNIQUE,
    email varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    usage_time timestamp(6) NULL,
    valid_from timestamp(6) NOT NULL,
    valid_to timestamp(6) NOT NULL,
    CONSTRAINT code_pk PRIMARY KEY (id),
    CONSTRAINT usage_time_check CHECK (usage_time BETWEEN valid_from AND valid_to)
);
ALTER TABLE user_management.code ADD CONSTRAINT code_status_fk FOREIGN KEY (status) REFERENCES user_management.code_status("name");
ALTER TABLE user_management.code ADD CONSTRAINT code_email_fk FOREIGN KEY (email) REFERENCES user_management.user("email");
