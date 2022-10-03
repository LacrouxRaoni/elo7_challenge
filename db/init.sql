CREATE TABLE "planets" (
	"id" serial NOT NULL,
	"planet_name" varchar(255) NOT NULL,
	"size_x" int NOT NULL,
	"size_y" int NOT NULL,
	CONSTRAINT "planets_pk" PRIMARY KEY ("id")
) WITH (
	OIDS=FALSE
);

CREATE TABLE "explorers" (
	"id" int NOT NULL,
	"explorer_name" varchar(255) NOT NULL,
	"direction" varchar(255) NOT NULL,
	"coo_x" int NOT NULL,
	"coo_y" int NOT NULL,
	CONSTRAINT "explorers_pk" PRIMARY KEY ("id")
) WITH (
	OIDS=FALSE
);

ALTER TABLE "planets" ADD CONSTRAINT "explorerfk" FOREIGN KEY ("id") REFERENCES "explorers"("id");
