-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 28-01-2020 a las 03:26:37
-- Versión del servidor: 5.7.24
-- Versión de PHP: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_ventas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE IF NOT EXISTS `cliente` (
  `idCliente` int(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Dni` varchar(8) NOT NULL,
  `Nombres` varchar(255) NOT NULL,
  `Direccion` varchar(255) DEFAULT NULL,
  `Estado` varchar(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`idCliente`, `Dni`, `Nombres`, `Direccion`, `Estado`) VALUES
(1, '2574539', 'Ivonne Perez Chavez', 'Calle sin nombre # 35', '1');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_ventas`
--

DROP TABLE IF EXISTS `detalle_ventas`;
CREATE TABLE IF NOT EXISTS `detalle_ventas` (
  `idDetalleVentas` int(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Ventas_idVentas` int(255) UNSIGNED NOT NULL,
  `Producto_idProducto` int(255) UNSIGNED NOT NULL,
  `Cantidad` int(255) NOT NULL DEFAULT '0',
  `PrecioVenta` double(255,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idDetalleVentas`),
  KEY `fk_producto_detalle` (`Producto_idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

DROP TABLE IF EXISTS `producto`;
CREATE TABLE IF NOT EXISTS `producto` (
  `idProducto` int(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Nombres` varchar(255) NOT NULL,
  `Precio` double(255,0) NOT NULL DEFAULT '0',
  `Stock` int(11) NOT NULL DEFAULT '0',
  `Estado` varchar(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vendedor`
--

DROP TABLE IF EXISTS `vendedor`;
CREATE TABLE IF NOT EXISTS `vendedor` (
  `idVEndedor` int(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Dni` varchar(8) NOT NULL,
  `Nombres` varchar(255) NOT NULL,
  `Telefono` varchar(10) DEFAULT NULL,
  `Estado` varchar(1) NOT NULL DEFAULT '1',
  `User_2` varchar(255) NOT NULL,
  PRIMARY KEY (`idVEndedor`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `vendedor`
--

INSERT INTO `vendedor` (`idVEndedor`, `Dni`, `Nombres`, `Telefono`, `Estado`, `User_2`) VALUES
(1, '12345', 'Nancy Elena', '3122563688', '1', 'emp01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

DROP TABLE IF EXISTS `ventas`;
CREATE TABLE IF NOT EXISTS `ventas` (
  `idVentas` int(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Cliente_idCliente` int(255) UNSIGNED NOT NULL,
  `Vendedor_idVendedor` int(255) UNSIGNED NOT NULL,
  `NumeroVentas` varchar(255) DEFAULT '0',
  `FechaVenta` date DEFAULT NULL,
  `Monto` double(255,0) NOT NULL DEFAULT '0',
  `Estado` varchar(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idVentas`),
  KEY `fk_cliente_ventas` (`Cliente_idCliente`),
  KEY `fk_vendedor_ventas` (`Vendedor_idVendedor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalle_ventas`
--
ALTER TABLE `detalle_ventas`
  ADD CONSTRAINT `fk_producto_detalle` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ventas_detalle` FOREIGN KEY (`idDetalleVentas`) REFERENCES `ventas` (`idVentas`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD CONSTRAINT `fk_cliente_ventas` FOREIGN KEY (`Cliente_idCliente`) REFERENCES `cliente` (`idCliente`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_vendedor_ventas` FOREIGN KEY (`Vendedor_idVendedor`) REFERENCES `vendedor` (`idVEndedor`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
