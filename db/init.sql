CREATE TABLE "planets" (
	"id" serial NOT NULL,
	"planet_name" varchar(255) NOT NULL,
	"size_x" int NOT NULL,
	"size_y" int NOT NULL,
	"explorer_name" varchar(255) NOT NULL,
	"coo_x" int NOT NULL,
	"coo_y" int NOT NULL,
	"new_coo_x" int,
	"new_coo_y" int,
	CONSTRAINT "planets_pk" PRIMARY KEY ("id")

) WITH (
	OIDS=FALSE
);
