DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS shows;
DROP TABLE IF EXISTS show_detail;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS reservation;

create table users (
	id bigint not null auto_increment,
	del_yn varchar(1),
	email varchar(50),
	password varchar(255),
	role varchar(20),
	username varchar(50),
	created_at datetime(6),
	created_by bigint,
	modified_at datetime(6),
	modified_by bigint,
	primary key (id),
	unique key (email)
)
;

create table shows (
	id bigint not null auto_increment,
	artist varchar(50),
	del_yn varchar(1),
	place varchar(255),
	show_from_date datetime(6),
	show_to_date datetime(6),
	title varchar(255),
	user_id bigint,
	created_at datetime(6),
	created_by bigint,
	modified_at datetime(6),
	modified_by bigint,
	primary key (id)
)
;

create table show_detail (
	id bigint not null auto_increment,
	del_yn varchar(1),
	reservation_from_dt datetime(6),
	reservation_to_dt datetime(6),
	sequence integer,
	show_from_dt datetime(6),
	show_to_dt datetime(6),
	show_id bigint,
	created_at datetime(6),
	created_by bigint,
	modified_at datetime(6),
	modified_by bigint,
	primary key (id)
)
;

create table seat (
	id bigint not null auto_increment,
	current_quantity integer not null,
	grade varchar(10),
	price integer,
	total_quantity integer not null,
	show_detail_id bigint,
	created_at datetime(6),
	created_by bigint,
	modified_at datetime(6),
	modified_by bigint,
	primary key (id)
)
;

create table reservation (
    id bigint not null auto_increment,
	del_yn varchar(1),
	user_id bigint,
	seat_id bigint,
    created_at datetime(6),
    modified_at datetime(6),
	primary key (id)
)
;