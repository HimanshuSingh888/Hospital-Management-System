CREATE DATABASE hospital;
USE hospital;

create table PATIENTS(
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
age INT NOT NULL,
gender VARCHAR(10) NOT NULL
);

create table DOCTORS(
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
specialization VARCHAR(255) NOT NULL
);

create table APPOINTMENTS(
id INT AUTO_INCREMENT PRIMARY KEY,
patient_id INT NOT NULL,
doctor_id INT NOT NULL,
appointment_date DATE NOT NULL,
FOREIGN KEY (patient_id) REFERENCES patients(id),
FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

SHOW TABLES;

INSERT INTO DOCTORS(name,specialization) VALUES("Himanshu Singh","Physician"),
("Ayush Jaiswal","NeuroSurgeon");

SELECT * FROM DOCTORS;