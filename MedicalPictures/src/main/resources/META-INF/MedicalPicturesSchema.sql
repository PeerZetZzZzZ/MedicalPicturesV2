CREATE TABLE IF NOT EXISTS MedicalPictures.UsersDB(username varchar(100) primary key,password varchar(128),accountType varchar(15))
CREATE TABLE IF NOT EXISTS MedicalPictures.Admin(username varchar(100), name varchar(255) not null, surname varchar(255) not null, age integer not null, foreign key (username) references MedicalPictures.UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE);
CREATE TABLE IF NOT EXISTS MedicalPictures.BodyPart(id varchar(36) primary key,bodyPart varchar(255) unique not null);
CREATE TABLE IF NOT EXISTS MedicalPictures.Patient(username varchar(100), name varchar(255), surname varchar(255), age integer not null, foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)
CREATE TABLE IF NOT EXISTS MedicalPictures.Technician(username varchar(100), name varchar(255), surname varchar(255), age integer not null, foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)
CREATE TABLE IF NOT EXISTS MedicalPictures.Doctor(username varchar(100), name varchar(255), surname varchar(255), age integer not null, specialization varchar(255), foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)
CREATE TABLE IF NOT EXISTS MedicalPictures.PictureType(id varchar(36) primary key, pictureType varchar(255) unique not null)
