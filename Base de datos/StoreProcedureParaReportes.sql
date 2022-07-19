USE IN5BV_KinalMall;

DROP PROCEDURE IF EXISTS sp_ReporteClientes;
DELIMITER $$
CREATE PROCEDURE sp_ReporteClientes()
BEGIN
SELECT 
		Clientes.id,
        Clientes.nombres,
        Clientes.apellidos,
        Clientes.telefono,
        Clientes.direccion,
        Clientes.email,
        TipoCliente.descripcion AS Tipo_Cliente
	FROM Clientes INNER JOIN TipoCliente
    ON Clientes.idTipoCliente = TipoCliente.id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_ReporteCuentasPorCobrar;
delimiter $$
CREATE PROCEDURE sp_ReporteCuentasPorCobrar()
BEGIN
	SELECT
		CuentasPorCobrar.id,
        CuentasPorCobrar.numeroFactura,
        CuentasPorCobrar.anio,
        CuentasPorCobrar.mes,
        CuentasPorCobrar.valorNetoPago,
        CuentasPorCobrar.estadoPago,
        Administracion.direccion,
        CONCAT(Clientes.nombres, " ", Clientes.apellidos) AS Nombre_Completo,
        Clientes.telefono,
        Locales.valorLocal,
        Locales.valorAdministracion,
        Locales.mesesPendientes
       FROM CuentasPorCobrar INNER JOIN Administracion
       on CuentasPorCobrar.idAdministracion = Administracion.id
       INNER JOIN Clientes 
       ON CuentasPorCobrar.idCliente = Clientes.id
       INNER JOIN Locales
       ON CuentasPorCobrar.idLocal = Locales.id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_ReporteAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_ReporteAdministracion()
BEGIN
	SELECT 
		Administracion.id, 
        Administracion.direccion, 
        Administracion.telefono 
	FROM Administracion;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_ReporteDepartamentos;
delimiter $$
CREATE PROCEDURE sp_ReporteDepartamentos()
BEGIN
	Select 
    Departamentos.id,
    Departamentos.nombre
    FROM Departamentos;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_ReporteCargos;
delimiter $$
CREATE PROCEDURE sp_ReporteCargos()
BEGIN
	Select 
    Cargos.id,
    Cargos.nombre
    FROM Cargos;
END $$
delimiter ;

DROP PROCEDURE IF EXISTS sp_ReporteTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_ReporteTipoCliente()
BEGIN
	SELECT
		TipoCliente.id,
		TipoCliente.descripcion
	FROM TipoCliente;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_ReporteLocales;
DELIMITER $$
CREATE PROCEDURE sp_ReporteLocales()
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

DROP PROCEDURE IF EXISTS sp_ReporteCuentasPorPagar;
delimiter $$
CREATE PROCEDURE sp_ReporteCuentasPorPagar()
BEGIN
	select
		CuentasPorPagar.id,
        CuentasPorPagar.numeroFactura,
        CuentasPorPagar.fechaLimitePago,
        CuentasPorPagar.estadoPago,
        CuentasPorPagar.valorNetoPago,
        Administracion.direccion, 
        proveedores.nit,
        proveedores.servicioPrestado,
        proveedores.telefono,
        proveedores.direccion AS DIRECCION,
        proveedores.saldoFavor,
        proveedores.saldoContra
        FROM CuentasPorPagar INNER JOIN Administracion
       on CuentasPorPagar.idAdministracion = Administracion.id
       INNER JOIN Proveedores 
       ON CuentasPorPagar.idProveedor = Proveedores.id;
END$$
delimiter ;

DROP PROCEDURE IF EXISTS sp_ReporteEmpleados;
DELIMITER $$
CREATE PROCEDURE sp_ReporteEmpleados()
BEGIN
	SELECT 
		Empleados.id,
        Empleados.nombres,
        Empleados.apellidos,
		Empleados.email,
        Empleados.telefono,
        Empleados.fechaContratacion,
        Empleados.Sueldo,
        Departamentos.nombre AS nombreDepartamento,
        Cargos.nombre As cargo,
        Horarios.horarioEntrada,
        Horarios.horarioSalida,
        Administracion.direccion
	FROM Empleados 
    INNER JOIN Departamentos ON Empleados.idDepartamento = Departamentos.id
    INNER JOIN Cargos ON Empleados.idCargo = Cargos.id
    INNER JOIN Horarios ON Empleados.idHorario = Horarios.id
    INNER JOIN Administracion ON Empleados.idAdministracion = Administracion.id;
        
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_ReporteAdministracionPorId;
DELIMITER $$
CREATE PROCEDURE sp_ReporteAdministracionPorId(IN _id INT)
BEGIN
SELECT 
		Empleados.id,
        Empleados.nombres,
        Empleados.apellidos,
		Empleados.email,
        Empleados.telefono,
        Empleados.fechaContratacion,
        Empleados.Sueldo,
        Departamentos.nombre AS nombreDepartamento,
        Cargos.nombre As cargo,
        Horarios.horarioEntrada,
        Horarios.horarioSalida,
        Administracion.id AS IDADMIN,
        Administracion.direccion,
        Administracion.telefono AS TELEFONOADMIN
	FROM Empleados 
    INNER JOIN Departamentos ON Empleados.idDepartamento = Departamentos.id
    INNER JOIN Cargos ON Empleados.idCargo = Cargos.id
    INNER JOIN Horarios ON Empleados.idHorario = Horarios.id
    INNER JOIN Administracion ON Empleados.idAdministracion = Administracion.id
    WHERE Administracion.id = _ID;
END $$
DELIMITER ;