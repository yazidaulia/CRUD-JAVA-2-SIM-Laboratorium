-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 02, 2020 at 06:43 PM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_lab`
--

-- --------------------------------------------------------

--
-- Table structure for table `analis`
--

CREATE TABLE `analis` (
  `id_analis` varchar(6) NOT NULL,
  `nama_analis` varchar(30) NOT NULL,
  `alamat_analis` varchar(40) NOT NULL,
  `password` varchar(10) NOT NULL,
  `pend_terakhir` varchar(20) NOT NULL,
  `no_hp` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `analis`
--

INSERT INTO `analis` (`id_analis`, `nama_analis`, `alamat_analis`, `password`, `pend_terakhir`, `no_hp`) VALUES
('AN001', 'Yazid Aulia', 'Perum Griya Timur Indah', 'yazid001', 'S1', '082298438725'),
('AN002', 'Aulia Saraswati', 'Grand Wisata', 'aul332', 'D3', '082298438726');

-- --------------------------------------------------------

--
-- Table structure for table `buat`
--

CREATE TABLE `buat` (
  `id_pas` varchar(6) NOT NULL,
  `subtest` varchar(40) NOT NULL,
  `jtanggal_periksa` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `hasil_pemeriksaan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `buat`
--

INSERT INTO `buat` (`id_pas`, `subtest`, `jtanggal_periksa`, `hasil_pemeriksaan`) VALUES
('PS0001', 'Hemoglobin', '2020-06-18 17:01:30', '200'),
('PS0001', 'Leukosit', '2020-06-22 00:41:22', '200'),
('PS0001', 'Hematokrit', '2020-06-22 00:59:01', '100'),
('PS0001', 'Trombosit', '2020-06-23 05:49:30', '200'),
('PS0002', 'Salmonella typhi O', '2020-07-02 01:11:54', '200'),
('PS0002', 'Salmonella Para typhi OA', '2020-07-02 01:58:53', '100'),
('PS0002', 'Salmonella Para typhi OB', '2020-06-18 02:32:09', ''),
('PS0002', 'Salmonella Para typhi OC', '2020-06-18 02:32:10', ''),
('PS0002', 'Salmonella typhi H', '2020-06-18 02:32:10', ''),
('PS0002', 'Salmonella Para typhi HA', '2020-06-18 02:32:10', ''),
('PS0002', 'Salmonella Para typhi HB', '2020-06-18 02:32:10', ''),
('PS0003', 'Salmonella typhi O', '2020-07-01 11:09:12', ''),
('PS0003', 'Salmonella Para typhi OA', '2020-07-01 11:09:12', ''),
('PS0003', 'Salmonella Para typhi OB', '2020-07-01 11:09:12', ''),
('PS0003', 'Salmonella Para typhi OC', '2020-07-01 11:09:12', ''),
('PS0003', 'Salmonella typhi H', '2020-07-01 11:09:12', ''),
('PS0003', 'Salmonella Para typhi HA', '2020-07-01 11:09:12', ''),
('PS0003', 'Salmonella Para typhi HB', '2020-07-01 11:09:12', '');

-- --------------------------------------------------------

--
-- Table structure for table `nota`
--

CREATE TABLE `nota` (
  `id_nota` varchar(6) NOT NULL,
  `jtanggal_periksa` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id_pas` varchar(6) NOT NULL,
  `id_analis` varchar(6) NOT NULL,
  `nm_test` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nota`
--

INSERT INTO `nota` (`id_nota`, `jtanggal_periksa`, `id_pas`, `id_analis`, `nm_test`) VALUES
('RST000', '2020-06-18 02:16:52', 'null', 'null', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pasien`
--

CREATE TABLE `pasien` (
  `id_pasien` varchar(6) NOT NULL,
  `nama_pasien` varchar(30) NOT NULL,
  `alamat` varchar(40) NOT NULL,
  `nama_test` varchar(30) NOT NULL,
  `tanggal_lahir` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pasien`
--

INSERT INTO `pasien` (`id_pasien`, `nama_pasien`, `alamat`, `nama_test`, `tanggal_lahir`) VALUES
('PS0001', 'Yazid Aulia', 'Griya', 'H2TL', '1999-02-28'),
('PS0002', 'Aulia', 'PTI', 'Widal', '2000-01-02'),
('PS0003', 'Leilaa', 'PTI', 'Widal', '1999-02-28');

-- --------------------------------------------------------

--
-- Table structure for table `pemeriksaan`
--

CREATE TABLE `pemeriksaan` (
  `kd_pemeriksaan` varchar(6) NOT NULL,
  `nama_pemeriksaan` varchar(30) NOT NULL,
  `sub_test` varchar(40) NOT NULL,
  `jenis_pemeriksaan` varchar(20) NOT NULL,
  `kelompok` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pemeriksaan`
--

INSERT INTO `pemeriksaan` (`kd_pemeriksaan`, `nama_pemeriksaan`, `sub_test`, `jenis_pemeriksaan`, `kelompok`) VALUES
('TB0002', 'H2TL', 'Gula', 'SINGLE', 'Hematologi'),
('TB0007', 'H2TL', 'Hemoglobin', 'SINGLE', 'Hematologi'),
('TB0009', 'H2TL', 'Leukosit', 'SINGLE', 'Hematologi'),
('TB0011', 'H2TL', 'Hematokrit', 'SINGLE', 'Hematologi'),
('TB0013', 'H2TL', 'Trombosit', 'SINGLE', 'Hematologi'),
('TB4028', 'Widal', 'Salmonella typhi O', 'SINGLE', 'IMUNO-SEROLOGI'),
('TB4029', 'Widal', 'Salmonella Para typhi OA', 'SINGLE', 'IMUNO-SEROLOGI'),
('TB4030', 'Widal', 'Salmonella Para typhi OB', 'SINGLE', 'IMUNO-SEROLOGI'),
('TB4031', 'Widal', 'Salmonella Para typhi OC', 'SINGLE', 'IMUNO-SEROLOGI'),
('TB4032', 'Widal', 'Salmonella typhi H', 'SINGLE', 'IMUNO-SEROLOGI'),
('TB4033', 'Widal', 'Salmonella Para typhi HA', 'SINGLE', 'IMUNO-SEROLOGI'),
('TB4034', 'Widal', 'Salmonella Para typhi HB', 'SINGLE', 'IMUNO-SEROLOGI');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `analis`
--
ALTER TABLE `analis`
  ADD PRIMARY KEY (`id_analis`);

--
-- Indexes for table `nota`
--
ALTER TABLE `nota`
  ADD PRIMARY KEY (`id_nota`);

--
-- Indexes for table `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`id_pasien`);

--
-- Indexes for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  ADD PRIMARY KEY (`kd_pemeriksaan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
