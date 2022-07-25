CREATE TABLE b_fw_sample_item (
	item_cd			varchar(20)	not null,
	name			varchar(128) not null,
	price			decimal(9) not null,
	simple_note		varchar(255),
	detail_note		varchar(255),
	image_path		varchar(255) not null,
	primary key(item_cd)
);
