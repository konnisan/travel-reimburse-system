CREATE TABLE IF NOT EXISTS fk_reim_apportion (
    id VARCHAR(64) NOT NULL COMMENT 'Primary key',
    main_id VARCHAR(64) NOT NULL COMMENT 'Travel reimburse main id',
    reim_company_id VARCHAR(64) NULL COMMENT 'Company id',
    reim_company_no VARCHAR(64) NULL COMMENT 'Company number',
    reim_company_name VARCHAR(255) NULL COMMENT 'Company name',
    project_id VARCHAR(64) NULL COMMENT 'Project id',
    project_no VARCHAR(64) NULL COMMENT 'Project number',
    project_name VARCHAR(255) NULL COMMENT 'Project name',
    apportion_ratio DECIMAL(18,4) NULL COMMENT 'Apportion ratio',
    apportion_amount DECIMAL(18,2) NULL COMMENT 'Apportion amount',
    row_no INT NULL COMMENT 'Row number',
    PRIMARY KEY (id),
    INDEX idx_fk_reim_apportion_main_id (main_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Travel reimburse cost apportion';
