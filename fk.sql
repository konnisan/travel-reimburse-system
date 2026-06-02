/*
 Navicat Premium Dump SQL

 Source Server         : sky
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : fk

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 02/06/2026 23:07:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fk_reim_apportion
-- ----------------------------
DROP TABLE IF EXISTS `fk_reim_apportion`;
CREATE TABLE `fk_reim_apportion`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Primary key',
  `main_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Travel reimburse main id',
  `reim_company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Company id',
  `reim_company_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Company number',
  `reim_company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Company name',
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Project id',
  `project_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Project number',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Project name',
  `apportion_ratio` decimal(18, 4) NULL DEFAULT NULL COMMENT 'Apportion ratio',
  `apportion_amount` decimal(18, 2) NULL DEFAULT NULL COMMENT 'Apportion amount',
  `row_no` int(11) NULL DEFAULT NULL COMMENT 'Row number',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_fk_reim_apportion_main_id`(`main_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Travel reimburse cost apportion' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_reim_apportion
-- ----------------------------
INSERT INTO `fk_reim_apportion` VALUES ('a4268f2a9d0946b88b5a896fab8f9d59', '7925cdf1b2dd492bb364927fb60c2c6a', 'C001', 'COMP001', 'Demo Technology Co., Ltd.', 'P001', 'PROJ001', 'Travel Project', 1.0000, 0.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('share_1779862819195_cogb1g', 'ac204bd355fc4ba8a0aabde625baebc0', '1C54557F1782E000', '0407', '胜意科技北京分公司', '12BC248B25083001', 'nonProjectRelated', '非项目类费用归集', 1.0000, 0.00, 1);

-- ----------------------------
-- Table structure for fk_reim_itinerary
-- ----------------------------
DROP TABLE IF EXISTS `fk_reim_itinerary`;
CREATE TABLE `fk_reim_itinerary`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `main_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主表ID，关联报销单主表主键ID',
  `traveler_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出行人ID',
  `traveler_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出行人工号',
  `traveler_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出行人',
  `departure_date` date NULL DEFAULT NULL COMMENT '出发日期',
  `arrival_date` date NULL DEFAULT NULL COMMENT '到达日期',
  `departure_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市',
  `departure_city_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市编号',
  `arriving_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市',
  `arriving_city_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市编号',
  `itinerary_instructions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '行程说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_fk_reim_itinerary_main_id`(`main_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '补录行程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_reim_itinerary
-- ----------------------------
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220001', 'RM202605220001', 'U1001', '1001', '张三', '2026-05-10', '2026-05-12', '武汉', 'WH', '北京', 'BJ', '武汉至北京');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220002', 'RM202605220001', 'U1001', '1001', '张三', '2026-05-12', '2026-05-12', '北京', 'BJ', '武汉', 'WH', '北京返回武汉');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220003', 'RM202605220002', 'U1002', '1002', '李四', '2026-05-15', '2026-05-16', '武汉', 'WH', '上海', 'SH', '武汉至上海');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220004', 'RM202605220003', 'U1003', '1003', '王五', '2026-05-18', '2026-05-21', '武汉', 'WH', '广州', 'GZ', '武汉至广州');
INSERT INTO `fk_reim_itinerary` VALUES ('trip_1779862819195_nafwdr', 'ac204bd355fc4ba8a0aabde625baebc0', '13AB3A3F72409002', '74541', '徐年年', '2026-05-06', '2026-05-07', '北京', '10119', '上海', '10621', '');
INSERT INTO `fk_reim_itinerary` VALUES ('trip_1779862855937_dbp35q', 'ac204bd355fc4ba8a0aabde625baebc0', '13AB498CC6409002', '74008', '郑雨雪', '2026-05-06', '2026-05-07', '北京', '10119', '上海', '10621', '');

-- ----------------------------
-- Table structure for fk_reim_main
-- ----------------------------
DROP TABLE IF EXISTS `fk_reim_main`;
CREATE TABLE `fk_reim_main`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `creation_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `bill_date` date NULL DEFAULT NULL COMMENT '单据日期',
  `reimbursement_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销标题',
  `reimburser_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销人ID',
  `reimburser_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销人工号',
  `reimburser_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销人姓名',
  `reim_department_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销部门ID',
  `reim_department_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销部门编号',
  `reim_department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销部门名称',
  `reim_company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用归属公司ID',
  `reim_company_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用归属公司编号',
  `reim_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用归属公司名称',
  `business_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型ID',
  `business_type_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型编号',
  `business_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型名称',
  `business_trip_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出差事由',
  `subsidy_total` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '补助总金额',
  `meal_allowance` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '餐费补助',
  `transportation_allowance` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '交通补助',
  `phone_allowance` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '通讯补助',
  `remarks` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `reim_bill_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销单号',
  `bill_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '单据状态：0草稿 1已完成 2已作废',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报销单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_reim_main
-- ----------------------------
INSERT INTO `fk_reim_main` VALUES ('ac204bd355fc4ba8a0aabde625baebc0', '2026-05-27 13:15:57', '2026-05-27', '团建', '13AB3A3F72409002', '74541', '徐年年', '13AB8D7B52A9B002', '072001', '客户成功事业部', '1C54557F1782E000', '0407', '胜意科技北京分公司', '18F0916A8C2C4000', '1001001', '员工差旅活动', '团建', 0.00, 0.00, 0.00, 0.00, '', 'CLBX202605286979', '1', 0);
INSERT INTO `fk_reim_main` VALUES ('c120d56791c243ca8bda6421e4eaa08f', '2026-05-30 12:08:23', '2026-05-30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('ca28a91d6de748458788645c149c6490', '2026-05-29 11:44:57', '2026-05-29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('e10073cdc95b45dea82f650523030ff1', '2026-05-30 12:08:31', '2026-05-30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('RM202605220001', '2026-05-22 09:30:00', '2026-05-22', '张三北京出差报销', 'U1001', '1001', '张三', 'D001', 'DEP001', '研发部', 'C001', 'COM001', '武汉科技有限公司', 'BT001', 'TRAVEL', '差旅报销', '项目需求调研', 450.00, 240.00, 150.00, 60.00, '北京客户现场调研', NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('RM202605220002', '2026-05-22 10:15:00', '2026-05-22', '李四上海出差报销', 'U1002', '1002', '李四', 'D002', 'DEP002', '市场部', 'C001', 'COM001', '武汉科技有限公司', 'BT001', 'TRAVEL', '差旅报销', '参加行业会议', 360.00, 180.00, 120.00, 60.00, '上海行业展会', NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('RM202605220003', '2026-05-22 11:00:00', '2026-05-22', '王五广州出差报销', 'U1003', '1003', '王五', 'D003', 'DEP003', '实施部', 'C002', 'COM002', '湖北信息技术有限公司', 'BT001', 'TRAVEL', '差旅报销', '系统上线支持', 520.00, 300.00, 160.00, 60.00, '广州项目上线', NULL, '0', 0);

-- ----------------------------
-- Table structure for fk_reim_subsidy
-- ----------------------------
DROP TABLE IF EXISTS `fk_reim_subsidy`;
CREATE TABLE `fk_reim_subsidy`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `main_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主表ID，关联报销单主表主键ID',
  `traveler_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出行人ID',
  `traveler_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出行人工号',
  `traveler_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出行人',
  `departure_date` date NULL DEFAULT NULL COMMENT '出发日期',
  `arrival_date` date NULL DEFAULT NULL COMMENT '到达日期',
  `subsidy_days` int(11) NULL DEFAULT 0 COMMENT '补助天数',
  `departure_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市',
  `departure_city_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市编号',
  `arriving_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市',
  `arriving_city_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市编号',
  `application_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '申请金额',
  `subsidy_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '补助金额',
  `meal_allowance` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '餐费补助',
  `transportation_allowance` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '交通补助',
  `phone_allowance` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '通讯补助',
  `business_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型ID',
  `business_type_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型编号',
  `business_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_fk_reim_subsidy_main_id`(`main_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '补助信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_reim_subsidy
-- ----------------------------
INSERT INTO `fk_reim_subsidy` VALUES ('SUB202605220001', 'RM202605220001', 'U1001', '1001', '张三', '2026-05-10', '2026-05-12', 3, '武汉', 'WH', '北京', 'BJ', 450.00, 450.00, 240.00, 150.00, 60.00, 'BT001', 'TRAVEL', '差旅报销');
INSERT INTO `fk_reim_subsidy` VALUES ('SUB202605220002', 'RM202605220002', 'U1002', '1002', '李四', '2026-05-15', '2026-05-16', 2, '武汉', 'WH', '上海', 'SH', 360.00, 360.00, 180.00, 120.00, 60.00, 'BT001', 'TRAVEL', '差旅报销');
INSERT INTO `fk_reim_subsidy` VALUES ('SUB202605220003', 'RM202605220003', 'U1003', '1003', '王五', '2026-05-18', '2026-05-21', 4, '武汉', 'WH', '广州', 'GZ', 520.00, 520.00, 300.00, 160.00, 60.00, 'BT001', 'TRAVEL', '差旅报销');
INSERT INTO `fk_reim_subsidy` VALUES ('subsidy_1779953966944_cikerl', 'ac204bd355fc4ba8a0aabde625baebc0', '13AB3A3F72409002', '74541', '徐年年', '2026-05-05', '2026-05-27', 5, NULL, NULL, '北京', '10119', 0.00, 0.00, 0.00, 0.00, 0.00, '18F0916A8C2C4000', '1001001', '员工差旅活动');

-- ----------------------------
-- Table structure for fk_subsidy_calendar
-- ----------------------------
DROP TABLE IF EXISTS `fk_subsidy_calendar`;
CREATE TABLE `fk_subsidy_calendar`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `main_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主表ID，关联补助信息表主键ID',
  `travel_date` date NOT NULL COMMENT '出差日期',
  `travel_date_week` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出差日期星期',
  `subsidized_cities` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '补助城市',
  `subsidized_city_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '补助城市编号',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `standard_meal_expenses_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '餐费标准金额',
  `standard_traffic_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '交通标准金额',
  `standard_communication_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '通讯标准金额',
  `meal_expenses_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '餐费金额',
  `traffic_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '交通金额',
  `communication_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '通讯金额',
  `is_reimbursed` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否报销，控制复选框是否选中',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_fk_subsidy_calendar_main_id`(`main_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '补助日历表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_subsidy_calendar
-- ----------------------------
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220001', 'SUB202605220001', '2026-05-10', '星期日', '北京', 'BJ', '到达北京', 80.00, 50.00, 20.00, 80.00, 50.00, 20.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220002', 'SUB202605220001', '2026-05-11', '星期一', '北京', 'BJ', '北京出差', 80.00, 50.00, 20.00, 80.00, 50.00, 20.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220003', 'SUB202605220001', '2026-05-12', '星期二', '北京', 'BJ', '返回武汉', 80.00, 50.00, 20.00, 80.00, 50.00, 20.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220004', 'SUB202605220002', '2026-05-15', '星期五', '上海', 'SH', '到达上海', 90.00, 60.00, 30.00, 90.00, 60.00, 30.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220005', 'SUB202605220002', '2026-05-16', '星期六', '上海', 'SH', '会议结束返回', 90.00, 60.00, 30.00, 90.00, 60.00, 30.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220006', 'SUB202605220003', '2026-05-18', '星期一', '广州', 'GZ', '到达广州', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220007', 'SUB202605220003', '2026-05-19', '星期二', '广州', 'GZ', '系统部署', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220008', 'SUB202605220003', '2026-05-20', '星期三', '广州', 'GZ', '上线支持', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220009', 'SUB202605220003', '2026-05-21', '星期四', '广州', 'GZ', '返回武汉', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862805861_pbtioq', 'subsidy_1779953966944_cikerl', '2026-05-27', NULL, '北京', '10119', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862819195_9m1qcb', 'subsidy_1779953966944_cikerl', '2026-05-06', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862819195_bpp1dp', 'subsidy_1779953966944_cikerl', '2026-05-05', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862855937_ctddey', 'subsidy_1779953966944_cikerl', '2026-05-06', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862855937_oh0xq8', 'subsidy_1779953966944_cikerl', '2026-05-05', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1');

SET FOREIGN_KEY_CHECKS = 1;
