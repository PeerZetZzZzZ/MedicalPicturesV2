CREATE TABLE IF NOT EXISTS MedicalPictures.UsersDB(username varchar(100) primary key,password varchar(128),accountType varchar(15))
CREATE TABLE IF NOT EXISTS MedicalPictures.Admin(username varchar(100), name varchar(255) not null, surname varchar(255) not null, age integer not null, foreign key (username) references MedicalPictures.UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE);
CREATE TABLE IF NOT EXISTS MedicalPictures.BodyPart(id varchar(36) primary key,bodyPart varchar(100));
