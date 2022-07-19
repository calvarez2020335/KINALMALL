USE IN5BV_KinalMall;

-- -----------------------------------------------------
-- PROCEDURE Administracion
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_ListarAdministracion()
BEGIN
	SELECT 
		Administracion.id, 
        Administracion.direccion, 
        Administracion.telefono 
	FROM Administracion;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_BuscarAdministracion(IN _id INT)
BEGIN
	SELECT 
		Administracion.id, 
        Administracion.direccion, 
        Administracion.telefono 
	FROM Administracion
    WHERE id = _id;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarAdministracion; 
DELIMITER $$
CREATE PROCEDURE sp_AgregarAdministracion (
	IN _direccion VARCHAR(100), 
	IN _telefono VARCHAR(8)
)
BEGIN
	INSERT INTO Administracion (direccion, telefono) 
    VALUES (_direccion, _telefono);
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_EditarAdministracion (
	IN _id INT,
	IN _direccion VARCHAR(100),
    IN _telefono VARCHAR(8)
)
BEGIN
	UPDATE Administracion 
    SET 
		direccion = _direccion, 
		telefono = _telefono 
    WHERE id = _id;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_EliminarAdministracion (IN _id INT)
BEGIN
	DELETE FROM Administracion WHERE id = _id;
END $$
DELIMITER ;

-- -----------------------------------------------------
-- PROCEDURE Departamentos
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarDepartamentos;
delimiter $$
CREATE PROCEDURE sp_ListarDepartamentos()
BEGIN
	Select 
    Departamentos.id,
    Departamentos.nombre
    FROM Departamentos;
END$$
delimiter ;


DROP PROCEDURE IF EXISTS sp_BuscarDepartamentos;
delimiter $$
CREATE PROCEDURE sp_BuscarDepartamentos(IN _id int)
BEGIN
	Select 
    Departamentos.id,
    Departamentos.nombre
    FROM Departamentos
    WHERE id = _id;
END$$
delimiter ;


DROP PROCEDURE IF EXISTS sp_AgregarDepartamentos;
delimiter $$
CREATE PROCEDURE sp_AgregarDepartamentos(IN _nombre VARCHAR(45))
BEGIN
	INSERT INTO Departamentos(nombre)
	VALUES (_nombre);
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarDepartamentos;
delimiter $$
CREATE PROCEDURE sp_EditarDepartamentos(IN _id INT, 
										IN _nombre VARCHAR(45))
BEGIN
	UPDATE Departamentos
	set nombre = _nombre 
    WHERE id = _id;
END$$
delimiter ;


DROP PROCEDURE IF EXISTS sp_EliminarDepartamentos;
delimiter $$
CREATE PROCEDURE sp_EliminarDepartamentos(IN _id INT)
BEGIN
	DELETE FROM Departamentos
    where id = _id;
END$$
delimiter ;

-- -----------------------------------------------------
-- PROCEDURE Proveedores
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarProveedores;
delimiter $$
CREATE PROCEDURE sp_ListarProveedores()
BEGIN
	SELECT 
		Proveedores.id,
        Proveedores.nit,
        Proveedores.servicioPrestado,
        Proveedores.telefono,
        Proveedores.direccion,
        Proveedores.saldoFavor,
        Proveedores.saldoContra
	FROM Proveedores;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_BuscarProveedores;
delimiter $$
CREATE PROCEDURE sp_BuscarProveedores(IN _id INT)
BEGIN
	SELECT 
		Proveedores.id,
        Proveedores.nit,
        Proveedores.servicioPrestado,
        Proveedores.telefono,
        Proveedores.direccion,
        Proveedores.saldoFavor,
        Proveedores.saldoContra
	FROM Proveedores
		WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_AgregarProveedores;
delimiter $$
CREATE PROCEDURE sp_AgregarProveedores(IN _nit VARCHAR(45),
									   IN _servicioPrestado VARCHAR(45), 
									   IN _telefono VARCHAR(8),
									   IN _direccion VARCHAR(60),
									   IN _saldoFavor DECIMAL(11,2),
									   IN _saldoContra DECIMAL(11,2))
BEGIN
	INSERT INTO Proveedores(nit, servicioPrestado, telefono, direccion, saldoFavor, saldoContra)
    VALUES(_nit, _servicioPrestado, _telefono, _direccion, _saldoFavor, _saldoContra);
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EditarProveedores;
delimiter $$
CREATE PROCEDURE sp_EditarProveedores(IN _id INT,IN _nit VARCHAR(45),
									  IN _servicioPrestado VARCHAR(45), 
									  IN _telefono VARCHAR(8),
									  IN _direccion VARCHAR(60),
										 IN _saldoFavor DECIMAL(11,2),
                                         IN _saldoContra DECIMAL(11,2))
BEGIN
	UPDATE Proveedores
    SET nit = _nit , servicioPrestado = _servicioPrestado , telefono = _telefono, direccion = _direccion, saldoFavor = _saldoFavor,
    saldoContra = _saldoContra
    WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EliminarProveedores;
delimiter $$
CREATE PROCEDURE sp_EliminarProveedores(IN _id INT)
BEGIN
	DELETE FROM Proveedores
    WHERE id = _id;
END$$
delimiter ;

-- -----------------------------------------------------
-- PROCEDURE Locales
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarLocales;
DELIMITER $$
CREATE PROCEDURE sp_ListarLocales()
BEGIN
	SELECT
		Locales.id,
		Locales.saldoFavor,
		Locales.saldoContra,
		Locales.mesesPendientes,
		Locales.disponibilidad,
		Locales.valorLocal,
		Locales.valorAdministracion
	FROM Locales;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarLocales;
DELIMITER $$
CREATE PROCEDURE sp_BuscarLocales(IN _id INT)
BEGIN
	SELECT
		Locales.saldoFavor,
		Locales.saldoContra,
		Locales.mesesPendientes,
		Locales.disponibilidad,
		Locales.valorLocal,
		Locales.valorAdministracion
	FROM Locales
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarLocales;
DELIMITER $$
CREATE PROCEDURE sp_AgregarLocales(
	IN _saldoFavor DECIMAL(11,2),
	IN _saldoContra DECIMAL(11,2),
	IN _mesesPendientes INT,
	IN _disponibilidad BOOLEAN,
	IN _valorLocal DECIMAL(11,2),
	IN _valorAdministracion DECIMAL(11,2))
BEGIN
	INSERT INTO Locales(saldoFavor, saldoContra, mesesPendientes, disponibilidad, valorLocal, valorAdministracion )
	VALUES (_saldoFavor, _saldoContra, _mesesPendientes, _disponibilidad, _valorLocal, _valorAdministracion);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarLocales;
DELIMITER $$
CREATE PROCEDURE sp_EditarLocales(
	IN _id INT,
	IN _saldoFavor DECIMAL(11,2),
	IN _saldoContra DECIMAL(11,2),
	IN _mesesPendientes INT,
	IN _disponibilidad BOOLEAN,
	IN _valorLocal DECIMAL(11,2),
	IN _valorAdministracion DECIMAL(11,2))
BEGIN
	UPDATE Locales
	SET
		saldoFavor = _saldoFavor,
		saldoContra = _saldoContra,
		mesesPendientes = _mesesPendientes,
		disponibilidad = _disponibilidad,
		valorLocal = _valorLocal,
		valorAdministracion = _valorAdministracion
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarLocales;
DELIMITER $$
CREATE PROCEDURE sp_EliminarLocales(IN _id INT)
BEGIN
	DELETE FROM Locales WHERE id = _id;
END$$
DELIMITER ;


-- -----------------------------------------------------
-- PROCEDURE TipoCliente
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_ListarTipoCliente()
BEGIN
	SELECT
		TipoCliente.id,
		TipoCliente.descripcion
	FROM TipoCliente;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_BuscarTipoCliente(IN _id INT)
BEGIN
	SELECT
		TipoCliente.id,
		TipoCliente.descripcion
	FROM TipoCliente
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_AgregarTipoCliente(
	IN _descripcion VARCHAR(45)
)
BEGIN
	INSERT INTO TipoCliente(descripcion)
	VALUES (_descripcion);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_EditarTipoCliente(
	IN _id INT,
	IN _descripcion VARCHAR(45)
)
BEGIN
	UPDATE TipoCliente
	SET
		descripcion = _descripcion
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_EliminarTipoCliente(IN _id INT)
BEGIN
	DELETE FROM TipoCliente WHERE id = _id;
END$$
DELIMITER ;


-- -----------------------------------------------------
-- PROCEDURE Cliente
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarClientes;
DELIMITER $$
CREATE PROCEDURE sp_ListarClientes()
BEGIN
	SELECT 
		Clientes.id,
        Clientes.nombres,
        Clientes.apellidos,
        Clientes.telefono,
        Clientes.direccion,
        Clientes.email,
        Clientes.idTipoCliente
	FROM Clientes;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarClientes;
DELIMITER $$
CREATE PROCEDURE sp_BuscarClientes(IN _id INT)
BEGIN
	SELECT 
		Clientes.id,
        Clientes.nombres,
        Clientes.apellidos,
        Clientes.telefono,
        Clientes.direccion,
        Clientes.email,
        Clientes.idTipoCliente
	FROM Clientes
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarClientes;
DELIMITER $$
CREATE PROCEDURE sp_AgregarClientes(
    IN _nombres VARCHAR(45),
    IN _apellidos VARCHAR(45),
    IN _telefono VARCHAR(8),
    IN _direccion VARCHAR(100),
    IN _email VARCHAR(45),
    IN _idTipoCliente INT
)
BEGIN
	INSERT INTO Clientes(nombres, apellidos, telefono, direccion, email, idTipoCliente)
	VALUES (_nombres, _apellidos, _telefono, _direccion, _email, _idTipoCliente);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarClientes;
DELIMITER $$
CREATE PROCEDURE sp_EditarClientes(
	IN _id INT,
    IN _nombres VARCHAR(45),
    IN _apellidos VARCHAR(45),
    IN _telefono VARCHAR(8),
    IN _direccion VARCHAR(100),
    IN _email VARCHAR(45),
    IN _idTipoCliente INT
)
BEGIN
	UPDATE Clientes
	SET
		nombres = _nombres, 
        apellidos = _apellidos, 
        telefono = _telefono, 
        direccion = _direccion, 
        email = _email, 
        idTipoCliente = _idTipoCliente
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarClientes;
DELIMITER $$
CREATE PROCEDURE sp_EliminarClientes(IN _id INT)
BEGIN
	DELETE FROM Clientes WHERE id = _id;
END$$
DELIMITER ;

-- -----------------------------------------------------
-- PROCEDURE Horarios
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarHorarios;
delimiter $$
CREATE PROCEDURE sp_ListarHorarios()
BEGIN
	SELECT
		Horarios.id,
        Horarios.horarioEntrada,
        Horarios.horarioSalida,
        Horarios.lunes,
        Horarios.martes,
        Horarios.miercoles,
        Horarios.jueves,
        Horarios.Viernes
        FROM Horarios;
END$$
delimiter ;
/*
DROP PROCEDURE IF EXISTS sp_ReporteHorarios;
delimiter $$
CREATE PROCEDURE sp_ReporteHorarios()
BEGIN
	SELECT
		Horarios.id,
        Horarios.horarioEntrada,
        Horarios.horarioSalida,
        Horarios.lunes = TRUE = THEN "SI" ELSE "NO" END AS lunes,
        Horarios.martes,
        Horarios.miercoles,
        Horarios.jueves,
        Horarios.Viernes
        FROM Horarios;
END$$
delimiter ;
*/

DROP PROCEDURE IF EXISTS sp_BuscarHorarios;
delimiter $$
CREATE PROCEDURE sp_BuscarHorarios(IN _id INT)
BEGIN
	SELECT
		Horarios.id,
        Horarios.horarioEntrada,
        Horarios.horarioSalida,
        Horarios.lunes,
        Horarios.martes,
        Horarios.miercoles,
        Horarios.jueves,
        Horarios.Viernes
        FROM Horarios
        WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_AgregarHorarios;
delimiter $$
CREATE PROCEDURE sp_AgregarHorarios(IN _horarioEntrada TIME, 
									IN _horarioSalida TIME, 
									IN _lunes BOOLEAN,
                                    IN _martes BOOLEAN, 
									IN _miercoles BOOLEAN, 
                                    IN _jueves BOOLEAN, 
                                    IN _viernes BOOLEAN)
BEGIN
	INSERT INTO Horarios(horarioEntrada, horarioSalida, lunes, martes, miercoles, jueves, viernes)
    VALUES(_horarioEntrada, _horarioSalida, _lunes, _martes, _miercoles, _jueves, _viernes);
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EditarHorarios;
delimiter $$
CREATE PROCEDURE sp_EditarHorarios(IN _id INT,
								   IN _horarioEntrada TIME, 
								   IN _horarioSalida TIME, 
								   IN _lunes BOOLEAN, 
								   IN _martes BOOLEAN, 
								   IN _miercoles BOOLEAN, 
								   IN _jueves BOOLEAN, 
								   IN _viernes BOOLEAN)
BEGIN
	UPDATE Horarios
    SET horarioEntrada = _horarioEntrada, horarioSalida = _horarioSalida,
    lunes = _lunes, martes = _martes, miercoles = _miercoles, jueves = _jueves, viernes = _viernes WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EliminarHorarios;
delimiter $$
CREATE PROCEDURE sp_EliminarHorarios(IN _id INT)
BEGIN 
	DELETE FROM Horarios
    where id = _id;
END$$
delimiter ;

-- -----------------------------------------------------
-- PROCEDURE CuentasPorPagar
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarCuentasPorPagar;
delimiter $$
CREATE PROCEDURE sp_ListarCuentasPorPagar()
BEGIN
	select
		CuentasPorPagar.id,
        CuentasPorPagar.numeroFactura,
        CuentasPorPagar.fechaLimitePago,
        CuentasPorPagar.estadoPago,
        CuentasPorPagar.valorNetoPago,
        CuentasPorPagar.idAdministracion, 
        CuentasPorPagar.idProveedor
        FROM CuentasPorPagar;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_BuscarCuentasPorPagar;
delimiter $$
CREATE PROCEDURE sp_BuscarCuentasPorPagar(IN _id INT)
BEGIN
	select
		CuentasPorPagar.id,
        CuentasPorPagar.numeroFactura,
        CuentasPorPagar.fechaLimitePago,
        CuentasPorPagar.estadoPago,
        CuentasPorPagar.valorNetoPago,
        CuentasPorPagar.idAdministracion, 
        CuentasPorPagar.idProveedor
        FROM CuentasPorPagar
        WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_AgregarCuentasPorPagar;
delimiter $$
CREATE PROCEDURE sp_AgregarCuentasPorPagar(IN _numeroFactura VARCHAR(45),
										   IN _fechaLimitePago DATE, 
										   IN _estadoPago VARCHAR(45),
										   IN _valorNeto DECIMAL(11,2), 
										   IN _idAdministracion INT, 
										   IN _idProveedor INT)
BEGIN
	INSERT INTO CuentasPorPagar(numeroFactura, fechaLimitePago, estadoPago, valorNetoPago, idAdministracion, idProveedor)
    VALUES(_numeroFactura, _fechaLimitePago, _estadoPago, _valorNeto, _idAdministracion, _idProveedor);
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EditarCuentasPorPagar;
delimiter $$
CREATE PROCEDURE sp_EditarCuentasPorPagar(IN _id INT,
										  IN _numeroFactura VARCHAR(45), 
										  IN _fechaLimitePago DATE, 
										  IN _estadoPago VARCHAR(45),
										  IN _valorNeto DECIMAL(11,2),
										  IN _idAdministracion INT,
										  IN _idProveedor INT)
BEGIN
	UPDATE CuentasPorPagar
    SET numeroFactura = _numeroFactura, fechaLimitePago = _fechaLimitePago, estadoPago = _estadoPago,
    valorNetoPago = _valorNeto, idAdministracion = _idAdministracion, idProveedor = _idProveedor WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EliminarCuentasPorPagar;
delimiter $$
CREATE PROCEDURE sp_EliminarCuentasPorPagar(IN _id INT)
BEGIN
	DELETE FROM CuentasPorPagar
    WHERE id = _id;
END$$
delimiter ;

-- -----------------------------------------------------
-- PROCEDURE CuentasPorCobrar
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarCuentasPorCobrar;
delimiter $$
CREATE PROCEDURE sp_ListarCuentasPorCobrar()
BEGIN
	SELECT
		CuentasPorCobrar.id,
        CuentasPorCobrar.numeroFactura,
        CuentasPorCobrar.anio,
        CuentasPorCobrar.mes,
        CuentasPorCobrar.valorNetoPago,
        CuentasPorCobrar.estadoPago,
        CuentasPorCobrar.idAdministracion,
        CuentasPorCobrar.idCliente,
        CuentasPorCobrar.idLocal
        FROM CuentasPorCobrar;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_BuscarCuentasPorCobrar;
delimiter $$
CREATE PROCEDURE sp_BuscarCuentasPorCobrar(IN _id INT)
BEGIN
	SELECT
		CuentasPorCobrar.id,
        CuentasPorCobrar.numeroFactura,
        CuentasPorCobrar.anio,
        CuentasPorCobrar.mes,
        CuentasPorCobrar.valorNetoPago,
        CuentasPorCobrar.estadoPago,
        CuentasPorCobrar.idAdministracion,
        CuentasPorCobrar.idCliente,
        CuentasPorCobrar.idLocal
        FROM CuentasPorCobrar 
        WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_AgregarCuentasPorCobrar;
delimiter $$
CREATE PROCEDURE sp_AgregarCuentasPorCobrar(IN _numeroFactura varchar(45),
											IN _anio VARCHAR(4), 
											IN _mes INT(2),
											IN _valorNetoPago DECIMAL(11,2),
											  IN _estadoPago VARCHAR(45),
                                              IN _idAdministracion INT,
                                              IN _idCliente INT, 
                                              IN _idLocal INT)
BEGIN
	INSERT INTO CuentasPorCobrar(numeroFactura, anio, mes, valorNetoPago, estadoPago, idAdministracion, idCliente, idLocal)
    VALUES(_numeroFactura, _anio, _mes, _valorNetoPago, _estadoPago, _idAdministracion, _idCliente, _idLocal);
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EditarCuentasPorCobrar;
delimiter $$
CREATE PROCEDURE sp_EditarCuentasPorCobrar(IN _id INT,
											IN _numeroFactura varchar(45), 
											IN _anio VARCHAR(4),
											IN _mes INT(2),
											IN _valorNetoPago DECIMAL(11,2),
											  IN _estadoPago VARCHAR(45),
                                              IN _idAdministracion INT,
                                              IN _idCliente INT, 
                                              IN _idLocal INT)
BEGIN
	UPDATE CuentasPorCobrar
    SET numeroFactura = _numeroFactura, anio = _anio, mes = _mes, valorNetoPago = _valorNetoPago, estadoPago = _estadoPago,
    idAdministracion = _idAdministracion, idCliente = _idCliente, idLocal = _idLocal
    WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EliminarCuentasPorCobrar;
delimiter $$
CREATE PROCEDURE sp_EliminarCuentasPorCobrar(IN _id INT)
BEGIN
	DELETE FROM CuentasPorCobrar
    WHERE id = _id;
END$$
delimiter ;

-- -----------------------------------------------------
-- PROCEDURE Empleados
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarEmpleados;
delimiter $$
CREATE PROCEDURE sp_ListarEmpleados()
BEGIN
	SELECT 
		Empleados.id,
        Empleados.nombres,
        Empleados.apellidos,
        Empleados.email,
        Empleados.telefono,
        Empleados.fechaContratacion,
        Empleados.sueldo,
        Empleados.idDepartamento,
        Empleados.idCargo,
        Empleados.idHorario,
        Empleados.idAdministracion
        FROM Empleados;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_BuscarEmpleados;
delimiter $$
CREATE PROCEDURE sp_BuscarEmpleados(IN _id INT)
BEGIN
	SELECT 
		Empleados.id,
        Empleados.nombres,
        Empleados.apellidos,
        Empleados.email,
        Empleados.telefono,
        Empleados.fechaContratacion,
        Empleados.sueldo,
        Empleados.idDepartamento,
        Empleados.idCargo,
        Empleados.idHorario,
        Empleados.idAdministracion
        FROM Empleados
        WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_AgregarEmpleados;
delimiter $$
CREATE PROCEDURE sp_AgregarEmpleados(IN _nombre VARCHAR(45),
									 IN _apellido VARCHAR(45),
									 IN _email VARCHAR(45),
									 IN _telefono VARCHAR(8),
									   IN _fechaContratacion DATE,
                                       IN _sueldo DECIMAL(11,2),
                                       IN _idDepartamento INT,
                                       IN _idCargo INT, 
                                       IN _idHorario INT,
                                       IN _idAdministracion INT)
BEGIN 
	INSERT INTO Empleados(nombres, apellidos, email, telefono, fechaContratacion, sueldo, idDepartamento, idCargo, idHorario, idAdministracion)
    VALUES(_nombre, _apellido, _email, _telefono, _fechaContratacion, _sueldo, _idDepartamento, _idCargo, _idHorario, _idAdministracion);
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EditarEmpleados;
delimiter $$
CREATE PROCEDURE sp_EditarEmpleados(IN _id INT,
									IN _nombre VARCHAR(45),
									IN _apellido VARCHAR(45),
									IN _email VARCHAR(45),
									IN _telefono VARCHAR(8),
									IN _fechaContratacion DATE,
                                    IN _sueldo DECIMAL(11,2),
                                    IN _idDepartamento INT,
                                    IN _idCargo INT, 
									IN _idHorario INT,
                                    IN _idAdministracion INT)
BEGIN
	UPDATE Empleados
    SET nombres = _nombre, apellidos = _apellido, email = _email, telefono = _telefono, fechaContratacion = _fechaContratacion, 
    sueldo = _sueldo, idDepartamento = _idDepartamento, idCargo = _idCargo, idHorario = _idHorario, idAdministracion = _idAdministracion
    WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EliminarEmpleados;
delimiter $$
CREATE PROCEDURE sp_EliminarEmpleados(IN _id INT)
BEGIN
	DELETE FROM Empleados WHERE id = _id;
END$$
delimiter ;

-- -----------------------------------------------------
-- Procedure Cargos
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_AgregarCargos;
delimiter $$
CREATE PROCEDURE sp_AgregarCargos(IN _nombre VARCHAR(45))
BEGIN
	INSERT INTO Cargos(nombre)
    VALUES(_nombre);
END $$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EditarCargos;
delimiter $$
CREATE PROCEDURE sp_EditarCargos(IN _id INT,
								 IN _nombre VARCHAR(45))
BEGIN
	UPDATE Cargos
    set nombre = _nombre 
    WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_EliminarCargos;
delimiter $$
CREATE PROCEDURE sp_EliminarCargos(IN _id INT)
BEGIN
	DELETE FROM Cargos 
    where id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_BuscarCargos;
delimiter $$
CREATE PROCEDURE sp_BuscarCargos(IN _id INT)
BEGIN
	Select 
    Cargos.id,
    Cargos.nombre
    FROM Cargos
    WHERE id = _id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_ListarCargos;
delimiter $$
CREATE PROCEDURE sp_ListarCargos()
BEGIN
	Select 
    Cargos.id,
    Cargos.nombre
    FROM Cargos;
END$$
delimiter ;

-- -----------------------------------------------------
-- Procedure Login
-- -----------------------------------------------------

Drop procedure IF EXISTS sp_BuscarUsuario;
delimiter $$
CREATE PROCEDURE sp_BuscarUsuario(IN _User Varchar(50))
BEGIN
	SELECT Usuario.user, Usuario.pass, Usuario.nombre, Usuario.rol, Rol.descripcion
    FROM Usuario INNER JOIN Rol
    on Usuario.rol = Rol.id
    WHERE user = _User;
END$$
delimiter ;

call sp_BuscarUsuario("root");
-- -----------------------------------------------------
-- Datos de prueba
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Datos de Administracion
-- -----------------------------------------------------

CALL sp_AgregarAdministracion("Diagonal 6 12-42, Zona 10, Ciudad de Guatemala", "45623589");
CALL sp_AgregarAdministracion("12 Calle 1-25, Zona 10, Ciudad de Guatemala", "79654875");
CALL sp_AgregarAdministracion("9na calle 15-77, Zona 7, Ciudad de Guatemala", "79654875");
CALL sp_AgregarAdministracion("3ra Avenida 8-05, Bosques de San Nicolás, Zona 8, Guatemala", "79654875");
CALL sp_AgregarAdministracion("Santa fe, zona 13, Ciudad de Guatemala", "78965320");
CALL sp_AgregarAdministracion("15 Avenida A 15-16, Cdad. de Guatemala", "24119191");
CALL sp_AgregarAdministracion("Finca Nacional La Aurora, Cdad. de Guatemala", "22099100");
CALL sp_AgregarAdministracion("15 Calle 11-41 Zona 1. Ciudad de Guatemala", "22990200");
CALL sp_AgregarAdministracion("Av. Simeón Cañas, Cdad. de Guatemala", "22067400");
CALL sp_AgregarAdministracion("Av. Las Americas, Cdad. de Guatemala", "55553602");
call sp_ListarAdministracion();

-- -----------------------------------------------------
-- Datos de Tipo Cliente
-- -----------------------------------------------------

CALL sp_AgregarTipoCliente("Bronce");
CALL sp_AgregarTipoCliente("Plata");
CALL sp_AgregarTipoCliente("Oro");
CALL sp_AgregarTipoCliente("Diamante");

-- -----------------------------------------------------
-- Datos de Departamentos
-- -----------------------------------------------------

call sp_AgregarDepartamentos("Financiero");
call sp_AgregarDepartamentos("Recursos Humanos");
call sp_AgregarDepartamentos("Marketing");
call sp_AgregarDepartamentos("Comercial");
call sp_AgregarDepartamentos("Compras");

-- -----------------------------------------------------
-- Datos de Cargos
-- -----------------------------------------------------

call sp_AgregarCargos("CEO");
call sp_AgregarCargos("Gerente financiero");
call sp_AgregarCargos("Gerente comercial");
call sp_AgregarCargos("Gerente de TI");
call sp_AgregarCargos("Gerente de logista");

-- -----------------------------------------------------
-- Datos de Horarios
-- -----------------------------------------------------

call sp_AgregarHorarios("07:00:00","15:00:00",true,false,true,false,true);
call sp_AgregarHorarios("13:00:00","21:00:00",false,true,false,true,false);
call sp_AgregarHorarios("08:00:00","14:00:00",true,true,false,false,true);
call sp_AgregarHorarios("14:00:00","21:00:00",false,false,true,true,true);
call sp_AgregarHorarios("07:30:00","15:30:00",true,true,true,true,true);
call sp_AgregarHorarios("15:30:00","22:00:00",true,true,true,true,true);

-- -----------------------------------------------------
-- Datos de Empleados
-- -----------------------------------------------------

call sp_AgregarEmpleados("Carlos", "Alvarez", "calvarez@gmail.com", "12345678", curdate(), 12356.60, 1, 1, 1, 1 );
call sp_AgregarEmpleados("Antonio", "Garcia", "agarcia@gmail.com", "78965236", curdate(), 5863.30, 1, 1, 1, 2 );
call sp_AgregarEmpleados("Jose", "Matinez", "jmartine@outlook.es", "78523694", curdate(), 2000.00, 2, 2, 2, 3 );
call sp_AgregarEmpleados("Josefa", "Lopez", "jlopez@yopmail.com", "12305896", curdate(), 2000.00, 2, 2, 2, 4 );
call sp_AgregarEmpleados("Manuel", "Gonzalez", "mgonzales@GMX.com", "85236547", curdate(), 3000.00, 3, 3, 3, 5 );
call sp_AgregarEmpleados("Pedro", "Gomez", "pgomez@yahoo.com", "75369825", curdate(), 3000.00, 3, 3, 3, 6 );
call sp_AgregarEmpleados("Francisca", "Fernandez", "ffernadez@zoo.com", "75968423", curdate(), 4000.00, 4, 4, 4, 7 );
call sp_AgregarEmpleados("Angel", "Moreno", "mmoreno@gmail.com", "89630254", curdate(), 4000.00, 4, 4, 4, 8 );
call sp_AgregarEmpleados("Dolores", "Jimenez", "jjimenez@yopmail.com", "89630145", curdate(), 5000.00, 5, 5, 5, 9 );
call sp_AgregarEmpleados("Mario", "Ramirez", "mramirez@outlook.es", "85203369", curdate(), 5000.00, 5, 5, 5, 10 );

-- -----------------------------------------------------
-- Datos de Cliente
-- -----------------------------------------------------

CALL sp_AgregarClientes("Jorge", "Pérez", "45236598", "Zona 11", "jperez@gmail.com", 1);
CALL sp_AgregarClientes("Luis", "Aguayo", "65987465", "Zona 7", "laguayo@gmail.com", 1);
CALL sp_AgregarClientes("Mariela", "Hernández", "75648934", "Zona 14", "mhernandez@gmail.com", 2);
CALL sp_AgregarClientes("Andrea", "Heredia", "78965302", "Zona 13", "aheredia@yopmail.com", 2);
CALL sp_AgregarClientes("Ruth", "Cortés", "74523015", "Zona 1", "rcortes@yahoo.com", 3);
CALL sp_AgregarClientes("Ariana", "Ramos", "85203649", "Zona 5", "aramos@gmail.com", 3);
CALL sp_AgregarClientes("Felipe", "Barrón", "89630452", "Zona 21", "fbarron@outlook.es", 4);
CALL sp_AgregarClientes("Hansel", "Espejo", "23014568", "Zona 13", "hespejo@gmail.com", 4);
CALL sp_AgregarClientes("Sarai", "Aguilar", "78963026", "San Marcos", "saguilar@gmail.com", 1);
CALL sp_AgregarClientes("Karla", "Silva", "23569874", "Zona 10", "ksilva@outlook.es", 2);

-- -----------------------------------------------------
-- Datos de Proveedores
-- -----------------------------------------------------

call sp_AgregarProveedores("1236544-9", "Traer alimentos", "78945201", "Ciudad de Guatemala", 7590.50, 00.00);
call sp_AgregarProveedores("8965203-5", "Prestamo Muebles", "52366478", "Ciudad de Guatemala", 5000.00, 0.00);
call sp_AgregarProveedores("9630215-5", "Prestamo vehículos", "85693021", "Zona 13", 00.00, 1236.50);
call sp_AgregarProveedores("4112354-9", "Servicio de Internet", "47203659", "Zona 14", 500.00, 00.00);
call sp_AgregarProveedores("2586315-6", "Servicio de agua", "48569320", "Zona 14", 5000.50, 00.00);
call sp_AgregarProveedores("2132143-9", "Traer madera", "63696532", "Ciudad de Guatemala", 00.00, 875.30);
call sp_AgregarProveedores("4532361-5", "Servicio de luz", "78963025", "Zona 21", 00.00, 785.62);
call sp_AgregarProveedores("5631010-0", "Mercaderia", "52031458", "Zona 8", 54863.30, 00.00);
call sp_AgregarProveedores("4878524-8", "Servicio de telefonia", "12634201", "Zona 1", 7896.30, 00.30);
call sp_AgregarProveedores("5522336-1", "Extraccion de basura", "45632014", "Zona 14", 4563.30, 52.00);

-- -----------------------------------------------------
-- Datos de Cuentas por pagar
-- -----------------------------------------------------

call sp_AgregarCuentasPorPagar("12345678-2"," 2021-07-23", "CANCELADO", 0.00, 1, 1);
call sp_AgregarCuentasPorPagar("12312321-0"," 2021-07-01", "PENDIENTE", 45623.20, 2, 2);
call sp_AgregarCuentasPorPagar("56542122-2"," 2021-07-23", "CANCELADO", 0.00, 3, 3);
call sp_AgregarCuentasPorPagar("45852212-6","2021-07-01 ", "PENDIENTE", 6325.30, 4, 4);
call sp_AgregarCuentasPorPagar("45522215-6","2021-07-23 ", "CANCELADO", 0.00, 5, 5);
call sp_AgregarCuentasPorPagar("25331212-6"," 2021-07-01", "PENDIENTE", 4126.30, 6, 6);
call sp_AgregarCuentasPorPagar("85239562-6","2021-07-23 ", "CANCELADO", 0.00, 7, 7);
call sp_AgregarCuentasPorPagar("25896314-5","2021-07-01 ", "PENDIENTE", 7412.30, 8, 8);
call sp_AgregarCuentasPorPagar("74295265-8"," 2021-07-23", "CANCELADO", 0.00, 9, 9);
call sp_AgregarCuentasPorPagar("12364895-9","2021-07-01 ", "PENDIENTE", 852.60, 10, 10);

/************************************************************************************************************************************/

drop trigger if exists tr_CuentasPorCobrar_After_Insert;
delimiter $$
CREATE TRIGGER tr_CuentasPorCobrar_After_Insert
AFTER INSERT ON CuentasPorCobrar
FOR EACH ROW
BEGIN
	DECLARE _mesesPendientes INT;
	SET _mesesPendientes = (SELECT COUNT(*) FROM cuentasPorCobrar WHERE idLocal = NEW.idLocal AND estadoPago = "Pendiente");
	UPDATE Locales SET mesesPendientes = _mesesPendientes WHERE Locales.id = NEW.idLocal;
END$$

delimiter ;

drop trigger if exists tr_CuentasPorCobrar_After_UPDATE;
delimiter $$
CREATE TRIGGER tr_CuentasPorCobrar_After_UPDATE
AFTER UPDATE ON CuentasPorCobrar
FOR EACH ROW
BEGIN
	DECLARE _mesesPendientes INT;
	SET _mesesPendientes = (SELECT COUNT(*) FROM cuentasPorCobrar WHERE idLocal = NEW.idLocal AND estadoPago = "Pendiente");
	UPDATE Locales SET mesesPendientes = _mesesPendientes WHERE Locales.id = NEW.idLocal;
END$$
delimiter ;

drop trigger if exists tr_CuentasPorCobrar_After_DELETE;
delimiter $$
CREATE TRIGGER tr_CuentasPorCobrar_After_DELETE
AFTER DELETE ON CuentasPorCobrar
FOR EACH ROW
BEGIN
	DECLARE _mesesPendientes INT;
	SET _mesesPendientes = (SELECT COUNT(*) FROM cuentasPorCobrar WHERE idLocal = old.idLocal AND estadoPago = "Pendiente");
	UPDATE Locales SET mesesPendientes = _mesesPendientes WHERE Locales.id = old.idLocal;
END$$
delimiter ;

/********************************************************************************************************************************/

-- -----------------------------------------------------
-- Datos de Locales
-- -----------------------------------------------------

CALL sp_AgregarLocales(5000.00, 7000.00, 1, false, 20000.00, 1000.00);
CALL sp_AgregarLocales(00.00, 00.00, 0, true, 5000.00, 1000.00);
CALL sp_AgregarLocales(0.00, 300.00, 2, false, 5000.00, 1000.00);
CALL sp_AgregarLocales(00.00, 00.00, 0, true, 5000.00, 1000.00);
CALL sp_AgregarLocales(5000.50, 00.00, 4, false, 10000, 5000.00);
CALL sp_AgregarLocales(00.00, 00.00, 0, true, 5000.00, 2000.00);
CALL sp_AgregarLocales(200.00, 100.00, 5, false, 10000.00, 5000.00);
CALL sp_AgregarLocales(00.00, 00.00, 0, true, 8000.00, 2000.00);
CALL sp_AgregarLocales(1000.00, 00.00, 1, false, 6000.00, 1000.00);
CALL sp_AgregarLocales(00.00, 00.00, 0, true, 2000.00, 500.00);

-- -----------------------------------------------------
-- Datos de Cuenteas por cobrar
-- -----------------------------------------------------

call sp_AgregarCuentasPorCobrar("B-123", 2021, 7, 7856.30, "PENDIENTE", 1, 1, 1);
call sp_AgregarCuentasPorCobrar("D-321", 2021, 7, 0.00, "CANCELADO", 2, 2, 2);
call sp_AgregarCuentasPorCobrar("C-965", 2021, 7, 7000.50 , "PENDIENTE", 3, 3, 3);
call sp_AgregarCuentasPorCobrar("E-965", 2021, 7, 0.00, "CANCELADO", 4, 4, 4);
call sp_AgregarCuentasPorCobrar("F-965", 2021, 7, 3000.00, "PENDIENTE", 5, 5, 5);
call sp_AgregarCuentasPorCobrar("G-965", 2021, 7, 0.00 , "CANCELADO", 6, 6, 6);
call sp_AgregarCuentasPorCobrar("H-965", 2021, 7, 4000.00 , "PENDIENTE", 7, 7, 7);
call sp_AgregarCuentasPorCobrar("I-965", 2021, 7, 0.00 , "CANCELADO", 8, 8, 8);
call sp_AgregarCuentasPorCobrar("J-965", 2021, 7, 1200.30 , "PENDIENTE", 9, 9, 9);
call sp_AgregarCuentasPorCobrar("T-965", 2021, 7, 00.00 , "CANCELADO", 10, 10, 10);