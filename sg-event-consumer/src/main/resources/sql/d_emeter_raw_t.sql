CREATE TABLE `d_emeter_raw_t` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(50) DEFAULT NULL,
  `value` double DEFAULT -1,
  `run_at` bigint(20) DEFAULT NULL,
  `original` int(1) DEFAULT 0,
  `anomaly` int(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `indx_query` (`uuid`,`run_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8