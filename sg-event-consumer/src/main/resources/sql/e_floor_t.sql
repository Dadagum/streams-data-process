CREATE TABLE `e_floor_t` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `desc` varchar(50) DEFAULT NULL,
  `order` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8