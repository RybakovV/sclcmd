-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               5.7.13-log - MySQL Community Server (GPL)
-- ОС Сервера:                   Win64
-- HeidiSQL Версия:              9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Дамп структуры базы данных mysqlcmd
CREATE DATABASE IF NOT EXISTS `mysqlcmd` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mysqlcmd`;


-- Дамп структуры для таблица mysqlcmd.empty
CREATE TABLE IF NOT EXISTS `empty` (
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Дамп данных таблицы mysqlcmd.empty: ~0 rows (приблизительно)
/*!40000 ALTER TABLE `empty` DISABLE KEYS */;
/*!40000 ALTER TABLE `empty` ENABLE KEYS */;


-- Дамп структуры для таблица mysqlcmd.test-sql
CREATE TABLE IF NOT EXISTS `test-sql` (
  `ids` int(11) DEFAULT NULL,
  `first-name` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Дамп данных таблицы mysqlcmd.test-sql: ~0 rows (приблизительно)
/*!40000 ALTER TABLE `test-sql` DISABLE KEYS */;
/*!40000 ALTER TABLE `test-sql` ENABLE KEYS */;


-- Дамп структуры для таблица mysqlcmd.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) DEFAULT NULL,
  `name` text,
  `password` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Дамп данных таблицы mysqlcmd.user: ~1 rows (приблизительно)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `name`, `password`) VALUES
	(17, 'testUpdate', 'pswd-testUpdate');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
