-- Database: aplikasi
CREATE DATABASE IF NOT EXISTS aplikasi;
USE aplikasi;

CREATE TABLE claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lob VARCHAR(255) NOT NULL,
    penyebab_klaim VARCHAR(255),
    jumlah_nasabah INT,
    beban_klaim DECIMAL(20, 2)
);

select * from claims;
truncate claims;

-- Database: penampungan
CREATE DATABASE IF NOT EXISTS penampungan;
USE penampungan;

CREATE TABLE claim_summary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lob VARCHAR(255) NOT NULL,
    penyebab_klaim VARCHAR(255),
    periode DATE,
    beban_klaim DECIMAL(20, 2)
);
select * from claim_summary;
truncate claim_summary;
drop table claim_summary;
-- Table for logging
CREATE TABLE activity_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_type VARCHAR(255),
    description TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
drop table activity_log;
select * from activity_log;
truncate activity_log;