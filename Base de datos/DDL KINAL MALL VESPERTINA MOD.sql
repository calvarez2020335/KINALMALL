DROP DATABASE IF EXISTS IN5BV_KinalMall;
CREATE DATABASE IN5BV_KinalMall;
USE IN5BV_KinalMall;

-- -----------------------------------------------------
-- Tabla Administracion
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS Administracion (
  id INT NOT NULL AUTO_INCREMENT,
  direccion VARCHAR(100) NOT NULL,
  telefono VARCHAR(8) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla Cargos
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Cargos (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla TipoCliente
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TipoCliente (
  id INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla Clientes
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Clientes (
  id INT NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(45) NOT NULL,
  apellidos VARCHAR(45) NOT NULL,
  telefono VARCHAR(8) NOT NULL,
  direccion VARCHAR(100) NOT NULL,
  email VARCHAR(45) NOT NULL,
  idTipoCliente INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_Clientes_TipoCliente
    FOREIGN KEY (idTipoCliente)
    REFERENCES TipoCliente (id));


-- -----------------------------------------------------
-- Tabla Locales
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Locales (
  id INT NOT NULL AUTO_INCREMENT,
  saldoFavor DECIMAL(11,2) NULL DEFAULT 0.00,
  saldoContra DECIMAL(11,2) NULL DEFAULT 0.00,
  mesesPendientes INT NULL DEFAULT NULL,
  disponibilidad BOOLEAN NOT NULL,
  valorLocal DECIMAL(11,2) NOT NULL,
  valorAdministracion DECIMAL(11,2) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla CuentasPorCobrar
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CuentasPorCobrar (
  id INT NOT NULL AUTO_INCREMENT,
  numeroFactura VARCHAR(45) NOT NULL,
  anio YEAR NOT NULL,
  mes INT NOT NULL,
  valorNetoPago DECIMAL(11,2) NOT NULL,
  estadoPago VARCHAR(45) NOT NULL,
  idAdministracion INT NOT NULL,
  idCliente INT NOT NULL,
  idLocal INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_CuentasPorCobrar_Administracion
    FOREIGN KEY (idAdministracion)
    REFERENCES Administracion (id),
  CONSTRAINT FK_CuentasPorCobrar_Clientes
    FOREIGN KEY (idCliente)
    REFERENCES Clientes (id),
  CONSTRAINT FK_CuentasPorCobrar_Locales
    FOREIGN KEY (idLocal)
    REFERENCES Locales (id));


-- -----------------------------------------------------
-- Tabla Proveedores
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Proveedores (
  id INT NOT NULL AUTO_INCREMENT,
  nit VARCHAR(45) NOT NULL,
  servicioPrestado VARCHAR(45) NOT NULL,
  telefono VARCHAR(8) NOT NULL,
  direccion VARCHAR(100) NOT NULL,
  saldoFavor DECIMAL(11,2) NOT NULL,
  saldoContra DECIMAL(11,2) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla CuentasPorPagar
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CuentasPorPagar (
  id INT NOT NULL AUTO_INCREMENT,
  numeroFactura VARCHAR(45) NOT NULL,
  fechaLimitePago DATE NOT NULL,
  estadoPago VARCHAR(45) NOT NULL,
  valorNetoPago DECIMAL(11,2) NOT NULL,
  idAdministracion INT NOT NULL,
  idProveedor INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_CuentasPorPagar_Administracion
    FOREIGN KEY (idAdministracion)
    REFERENCES Administracion (id),
  CONSTRAINT FK_CuentasPorPagar_Proveedores
    FOREIGN KEY (idProveedor)
    REFERENCES proveedores (id));


-- -----------------------------------------------------
-- Tabla Departamentos
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Departamentos (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla Horarios
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Horarios (
  id INT NOT NULL AUTO_INCREMENT,
  horarioEntrada TIME NOT NULL,
  horarioSalida TIME NOT NULL,
  lunes BOOLEAN NULL DEFAULT NULL,
  martes BOOLEAN NULL DEFAULT NULL,
  miercoles BOOLEAN NULL DEFAULT NULL,
  jueves BOOLEAN NULL DEFAULT NULL,
  viernes BOOLEAN NULL DEFAULT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Tabla Empleados
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Empleados (
  id INT NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(45) NOT NULL,
  apellidos VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  telefono VARCHAR(8) NOT NULL,
  fechaContratacion DATE NOT NULL,
  sueldo DECIMAL(11,2) NOT NULL,
  idDepartamento INT NOT NULL,
  idCargo INT NOT NULL,
  idHorario INT NOT NULL,
  idAdministracion INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_Empleados_Administracion
    FOREIGN KEY (idAdministracion)
    REFERENCES Administracion (id),
  CONSTRAINT FK_Empleados_Cargos
    FOREIGN KEY (idCargo)
    REFERENCES Cargos (id),
  CONSTRAINT FK_Empleados_Departamento
    FOREIGN KEY (idDepartamento)
    REFERENCES Departamentos (id),
  CONSTRAINT FK_Empleados_Horarios
    FOREIGN KEY (idHorario)
    REFERENCES Horarios (id));
    
--------------------------------------------------------
-- Tabla Usuario
-- -----------------------------------------------------

--------------------------------------------------------
-- Tabla Rol
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS Rol (

	id INT NOT NULL auto_increment,
    descripcion varchar(45) not null,
	PRIMARY KEY pk_Rol(id)
);

CREATE TABLE IF NOT EXISTS Usuario (

	user Varchar(45) not null,
    pass Varchar (50) not null,
    nombre varchar(45) not null,
    rol int ,
	Primary Key PK_Usuario(user),
    constraint FK_Usuario
    FOREIGN KEY (rol) REFERENCES Rol (id) 
    
);

Insert into Rol(descripcion) 
values ("Usuario Administrador");
Insert into Rol(descripcion) 
values ("Usuario Estandar");

INSERT INTO Usuario(user, pass, nombre, rol) VALUES("root", "YWRtaW4=", "Jorge Pérez", 1);
INSERT INTO Usuario(user, pass, nombre, rol) VALUES ("kinal", "MTIzNDU=", "Luis Canto", 2);
