ALTER TABLE fk_reim_itinerary
    MODIFY COLUMN departure_date DATE NULL COMMENT '出发日期',
    MODIFY COLUMN arrival_date DATE NULL COMMENT '到达日期',
    MODIFY COLUMN departure_city VARCHAR(100) NULL COMMENT '出发城市',
    MODIFY COLUMN arriving_city VARCHAR(100) NULL COMMENT '到达城市',
    MODIFY COLUMN itinerary_instructions VARCHAR(500) NULL COMMENT '行程说明';

ALTER TABLE fk_reim_main
    MODIFY COLUMN creation_time DATETIME NULL COMMENT '创建时间',
    MODIFY COLUMN bill_date DATE NULL COMMENT '单据日期',
    MODIFY COLUMN reimbursement_title VARCHAR(500) NULL COMMENT '报销标题',
    MODIFY COLUMN reim_department_name VARCHAR(100) NULL COMMENT '报销部门名称',
    MODIFY COLUMN reim_company_name VARCHAR(100) NULL COMMENT '费用归属公司名称',
    MODIFY COLUMN business_type_name VARCHAR(100) NULL COMMENT '业务类型名称',
    MODIFY COLUMN business_trip_reason VARCHAR(500) NULL COMMENT '出差事由',
    MODIFY COLUMN subsidy_total DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '补助总金额',
    MODIFY COLUMN meal_allowance DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '餐费补助',
    MODIFY COLUMN transportation_allowance DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '交通补助',
    MODIFY COLUMN phone_allowance DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '通讯补助',
    MODIFY COLUMN remarks VARCHAR(1000) NULL COMMENT '备注信息';

ALTER TABLE fk_reim_subsidy
    MODIFY COLUMN departure_date DATE NULL COMMENT '出发日期',
    MODIFY COLUMN arrival_date DATE NULL COMMENT '到达日期',
    MODIFY COLUMN subsidy_days INT NULL DEFAULT 0 COMMENT '补助天数',
    MODIFY COLUMN departure_city VARCHAR(100) NULL COMMENT '出发城市',
    MODIFY COLUMN arriving_city VARCHAR(100) NULL COMMENT '到达城市',
    MODIFY COLUMN application_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '申请金额',
    MODIFY COLUMN subsidy_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '补助金额',
    MODIFY COLUMN meal_allowance DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '餐费补助',
    MODIFY COLUMN transportation_allowance DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '交通补助',
    MODIFY COLUMN phone_allowance DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '通讯补助',
    MODIFY COLUMN business_type_name VARCHAR(100) NULL COMMENT '业务类型名称';

ALTER TABLE fk_subsidy_calendar
    MODIFY COLUMN travel_date DATE NOT NULL COMMENT '出差日期',
    MODIFY COLUMN subsidized_cities VARCHAR(100) NULL COMMENT '补助城市',
    MODIFY COLUMN standard_meal_expenses_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '餐费标准金额',
    MODIFY COLUMN standard_traffic_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '交通标准金额',
    MODIFY COLUMN standard_communication_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '通讯标准金额',
    MODIFY COLUMN meal_expenses_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '餐费金额',
    MODIFY COLUMN traffic_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '交通金额',
    MODIFY COLUMN communication_amount DECIMAL(18, 2) NULL DEFAULT 0.00 COMMENT '通讯金额';
