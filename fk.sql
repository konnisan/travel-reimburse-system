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

 Date: 09/06/2026 11:40:54
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
INSERT INTO `fk_reim_apportion` VALUES ('1b10bc40753143dfbd3e852bfcd8c99c', '97dbabbdb42144039befe0a7f4dc039a', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_success_platform', 'CSSUPGRADE', '客户成功服务平台升级', 0.3000, 486.00, 2);
INSERT INTO `fk_reim_apportion` VALUES ('49512147448d45549966e7cbd70de7cd', 'a60fc8c8627b427c90589be5001b1daf', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_success_platform', 'CSSUPGRADE', '客户成功服务平台升级', 0.3000, 486.00, 2);
INSERT INTO `fk_reim_apportion` VALUES ('84bdf7e2b813481fac4f1b976b770982', 'a60fc8c8627b427c90589be5001b1daf', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_fee_control_phase2', 'FCPHASE2', '费控系统二期开发', 0.4000, 648.00, 3);
INSERT INTO `fk_reim_apportion` VALUES ('8c605772a10a4072811312557bfa7d3a', 'ba5708ea9ded400fbc84b014d2a53cb0', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_east_market', 'HDMARKET', '华东区市场推广项目', 0.3000, 486.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('a4268f2a9d0946b88b5a896fab8f9d59', '7925cdf1b2dd492bb364927fb60c2c6a', 'C001', 'COMP001', 'Demo Technology Co., Ltd.', 'P001', 'PROJ001', 'Travel Project', 1.0000, 0.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('a9e5d063035448fd830397567ce5b967', 'a60fc8c8627b427c90589be5001b1daf', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_east_market', 'HDMARKET', '华东区市场推广项目', 0.3000, 486.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('b64701497c984849929df51e8f3cd3b2', '97dbabbdb42144039befe0a7f4dc039a', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_fee_control_phase2', 'FCPHASE2', '费控系统二期开发', 0.4000, 648.00, 3);
INSERT INTO `fk_reim_apportion` VALUES ('bb489f9238a54542a48a12655187ec77', '97dbabbdb42144039befe0a7f4dc039a', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_east_market', 'HDMARKET', '华东区市场推广项目', 0.3000, 486.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('cb57b02e0c184ac481a5632116b35587', 'ba5708ea9ded400fbc84b014d2a53cb0', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_success_platform', 'CSSUPGRADE', '客户成功服务平台升级', 0.3000, 486.00, 2);
INSERT INTO `fk_reim_apportion` VALUES ('e59f87f17bc845abb02e44079423eef2', 'ba5708ea9ded400fbc84b014d2a53cb0', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_fee_control_phase2', 'FCPHASE2', '费控系统二期开发', 0.4000, 648.00, 3);
INSERT INTO `fk_reim_apportion` VALUES ('share_1779862819195_cogb1g', 'ac204bd355fc4ba8a0aabde625baebc0', '1C54557F1782E000', '0407', '胜意科技北京分公司', '12BC248B25083001', 'nonProjectRelated', '非项目类费用归集', 1.0000, 0.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('share_1780970050240_1cl26p', 'e10073cdc95b45dea82f650523030ff1', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_east_market', 'HDMARKET', '华东区市场推广项目', 0.3000, 0.00, 1);
INSERT INTO `fk_reim_apportion` VALUES ('share_1780971412011_xygxem', 'e10073cdc95b45dea82f650523030ff1', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_success_platform', 'CSSUPGRADE', '客户成功服务平台升级', 0.3000, 0.00, 2);
INSERT INTO `fk_reim_apportion` VALUES ('share_1780971412721_uxv9yb', 'e10073cdc95b45dea82f650523030ff1', '1C54557F1782E000', '0407', '胜意科技北京分公司', 'project_fee_control_phase2', 'FCPHASE2', '费控系统二期开发', 0.4000, 0.00, 3);

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
INSERT INTO `fk_reim_itinerary` VALUES ('64018f0e1f284120832ed13e1e698e7e', 'a60fc8c8627b427c90589be5001b1daf', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', '北京', '10119', '上海', '10621', '123');
INSERT INTO `fk_reim_itinerary` VALUES ('6fdc2d9711734c92b25f633717b5f8a5', '97dbabbdb42144039befe0a7f4dc039a', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', '北京', '10119', '上海', '10621', '123');
INSERT INTO `fk_reim_itinerary` VALUES ('f8dc7772cd3f4df1bfaba7cbbc32ae43', 'ba5708ea9ded400fbc84b014d2a53cb0', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', '北京', '10119', '上海', '10621', '123');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220001', 'RM202605220001', 'U1001', '1001', '张三', '2026-05-10', '2026-05-12', '武汉', 'WH', '北京', 'BJ', '武汉至北京');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220002', 'RM202605220001', 'U1001', '1001', '张三', '2026-05-12', '2026-05-12', '北京', 'BJ', '武汉', 'WH', '北京返回武汉');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220003', 'RM202605220002', 'U1002', '1002', '李四', '2026-05-15', '2026-05-16', '武汉', 'WH', '上海', 'SH', '武汉至上海');
INSERT INTO `fk_reim_itinerary` VALUES ('ITI202605220004', 'RM202605220003', 'U1003', '1003', '王五', '2026-05-18', '2026-05-21', '武汉', 'WH', '广州', 'GZ', '武汉至广州');
INSERT INTO `fk_reim_itinerary` VALUES ('trip_1779862819195_nafwdr', 'ac204bd355fc4ba8a0aabde625baebc0', '13AB3A3F72409002', '74541', '徐年年', '2026-05-06', '2026-05-07', '北京', '10119', '上海', '10621', '');
INSERT INTO `fk_reim_itinerary` VALUES ('trip_1779862855937_dbp35q', 'ac204bd355fc4ba8a0aabde625baebc0', '13AB498CC6409002', '74008', '郑雨雪', '2026-05-06', '2026-05-07', '北京', '10119', '上海', '10621', '');
INSERT INTO `fk_reim_itinerary` VALUES ('trip_1780970397078_mrxyjl', 'e10073cdc95b45dea82f650523030ff1', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', '北京', '10119', '上海', '10621', '123');

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
  `bill_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '单据状态：0草稿 1待审核 2已作废 3已完成',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报销单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_reim_main
-- ----------------------------
INSERT INTO `fk_reim_main` VALUES ('97dbabbdb42144039befe0a7f4dc039a', '2026-06-09 11:01:42', '2026-06-09', '测试', '13AB3A3F72409002', '74541', '徐年年', '13AB8D7B52A9B002', '072001', '客户成功事业部', '1C54557F1782E000', '0407', '胜意科技北京分公司', '1B5FEB7DD4396000', '10010010101', '项目出差', '123', 1620.00, 900.00, 360.00, 360.00, '测试', 'CLBX202606093222', '2', 3);
INSERT INTO `fk_reim_main` VALUES ('a60fc8c8627b427c90589be5001b1daf', '2026-06-09 11:19:14', '2026-06-09', '测试', '13AB3A3F72409002', '74541', '徐年年', '13AB8D7B52A9B002', '072001', '客户成功事业部', '1C54557F1782E000', '0407', '胜意科技北京分公司', '1B5FEB7DD4396000', '10010010101', '项目出差', '123', 1620.00, 900.00, 360.00, 360.00, '测试', 'CLBX202606096306', '3', 3);
INSERT INTO `fk_reim_main` VALUES ('ac204bd355fc4ba8a0aabde625baebc0', '2026-05-27 13:15:57', '2026-05-27', '团建', '13AB3A3F72409002', '74541', '徐年年', '13AB8D7B52A9B002', '072001', '客户成功事业部', '1C54557F1782E000', '0407', '胜意科技北京分公司', '18F0916A8C2C4000', '1001001', '员工差旅活动', '团建', 0.00, 0.00, 0.00, 0.00, '', 'CLBX202605286979', '1', 0);
INSERT INTO `fk_reim_main` VALUES ('ba5708ea9ded400fbc84b014d2a53cb0', '2026-06-09 11:00:33', '2026-06-09', '测试', '13AB3A3F72409002', '74541', '徐年年', '13AB8D7B52A9B002', '072001', '客户成功事业部', '1C54557F1782E000', '0407', '胜意科技北京分公司', '1B5FEB7DD4396000', '10010010101', '项目出差', '123', 1620.00, 900.00, 360.00, 360.00, '测试', 'CLBX202606097672', '2', 3);
INSERT INTO `fk_reim_main` VALUES ('c120d56791c243ca8bda6421e4eaa08f', '2026-05-30 12:08:23', '2026-05-30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('ca28a91d6de748458788645c149c6490', '2026-05-29 11:44:57', '2026-05-29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, '0', 0);
INSERT INTO `fk_reim_main` VALUES ('e10073cdc95b45dea82f650523030ff1', '2026-05-30 12:08:31', '2026-06-09', '测试', '13AB3A3F72409002', '74541', '徐年年', '13AB8D7B52A9B002', '072001', '客户成功事业部', '1C54557F1782E000', '0407', '胜意科技北京分公司', '1B5FEB7DD4396000', '10010010101', '项目出差', '123', 0.00, 0.00, 0.00, 0.00, '测试', 'CLBX202606092394', '2', 8);
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
INSERT INTO `fk_reim_subsidy` VALUES ('0ee110801aee408cadd23d85a3163ad6', 'a60fc8c8627b427c90589be5001b1daf', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', 9, NULL, NULL, '上海', '10621', 1620.00, 1620.00, 900.00, 360.00, 360.00, '1B5FEB7DD4396000', '10010010101', '项目出差');
INSERT INTO `fk_reim_subsidy` VALUES ('2dcd726ab4e24753bc28391740315e88', '97dbabbdb42144039befe0a7f4dc039a', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', 9, NULL, NULL, '上海', '10621', 1620.00, 1620.00, 900.00, 360.00, 360.00, '1B5FEB7DD4396000', '10010010101', '项目出差');
INSERT INTO `fk_reim_subsidy` VALUES ('eaf0b149f77e4f2da628bf68f0853e67', 'ba5708ea9ded400fbc84b014d2a53cb0', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', 9, NULL, NULL, '上海', '10621', 1620.00, 1620.00, 900.00, 360.00, 360.00, '1B5FEB7DD4396000', '10010010101', '项目出差');
INSERT INTO `fk_reim_subsidy` VALUES ('SUB202605220001', 'RM202605220001', 'U1001', '1001', '张三', '2026-05-10', '2026-05-12', 3, '武汉', 'WH', '北京', 'BJ', 450.00, 450.00, 240.00, 150.00, 60.00, 'BT001', 'TRAVEL', '差旅报销');
INSERT INTO `fk_reim_subsidy` VALUES ('SUB202605220002', 'RM202605220002', 'U1002', '1002', '李四', '2026-05-15', '2026-05-16', 2, '武汉', 'WH', '上海', 'SH', 360.00, 360.00, 180.00, 120.00, 60.00, 'BT001', 'TRAVEL', '差旅报销');
INSERT INTO `fk_reim_subsidy` VALUES ('SUB202605220003', 'RM202605220003', 'U1003', '1003', '王五', '2026-05-18', '2026-05-21', 4, '武汉', 'WH', '广州', 'GZ', 520.00, 520.00, 300.00, 160.00, 60.00, 'BT001', 'TRAVEL', '差旅报销');
INSERT INTO `fk_reim_subsidy` VALUES ('subsidy_1779953966944_cikerl', 'ac204bd355fc4ba8a0aabde625baebc0', '13AB3A3F72409002', '74541', '徐年年', '2026-05-05', '2026-05-27', 5, NULL, NULL, '北京', '10119', 0.00, 0.00, 0.00, 0.00, 0.00, '18F0916A8C2C4000', '1001001', '员工差旅活动');
INSERT INTO `fk_reim_subsidy` VALUES ('subsidy_1780970397079_biw91r', 'e10073cdc95b45dea82f650523030ff1', '13AB3A3F72409002', '74541', '徐年年', '2026-06-01', '2026-06-09', 9, NULL, NULL, '上海', '10621', 0.00, 0.00, 0.00, 0.00, 0.00, '1B5FEB7DD4396000', '10010010101', '项目出差');

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
  `city_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市类型',
  `meal_checked` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '餐费是否勾选',
  `traffic_checked` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '交通是否勾选',
  `communication_checked` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '通讯是否勾选',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_fk_subsidy_calendar_main_id`(`main_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '补助日历表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fk_subsidy_calendar
-- ----------------------------
INSERT INTO `fk_subsidy_calendar` VALUES ('006f4497965845398634bcb6b9ea894c', '2dcd726ab4e24753bc28391740315e88', '2026-06-03', '周三', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('101988ebc68540e5a68eb69f35618933', '0ee110801aee408cadd23d85a3163ad6', '2026-06-05', '周五', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('1447ca78e2ab4ecf8f6a45186199f7c4', '2dcd726ab4e24753bc28391740315e88', '2026-05-31', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('162d80cfaa8447d88139fa745a8add26', '0ee110801aee408cadd23d85a3163ad6', '2026-06-04', '周四', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('189b54c0187e4b8da934307a86a861f6', '0ee110801aee408cadd23d85a3163ad6', '2026-06-08', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('1dbb42cd1d114e32872d04a5a3aa62cf', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-02', '周二', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('2286a728d9f64168a95586916c795827', '0ee110801aee408cadd23d85a3163ad6', '2026-06-03', '周三', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('3dea0005cf7b465aad1637d764a8cb44', '2dcd726ab4e24753bc28391740315e88', '2026-06-05', '周五', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('477ea73231c94568a4ffddd538a71c4b', '0ee110801aee408cadd23d85a3163ad6', '2026-06-02', '周二', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('5076ac20b88d4c4d8ff3ff9f3c538acc', '2dcd726ab4e24753bc28391740315e88', '2026-06-08', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('5161f9fa133745ab97e32dddbd84c663', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-07', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('520e6268495d42eca3ab3f8953c411a3', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-05-31', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('63380b9b1c664f8b80c380b93372d32b', '2dcd726ab4e24753bc28391740315e88', '2026-06-04', '周四', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('6446483c043b459a9c408ba991753b05', '0ee110801aee408cadd23d85a3163ad6', '2026-06-07', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('6d873f5f8b5741df92681b9e60b75191', '2dcd726ab4e24753bc28391740315e88', '2026-06-07', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('9787752a6ca64c459b9488842091f3f5', '2dcd726ab4e24753bc28391740315e88', '2026-06-02', '周二', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('99a3f26341724575b90e53ace92f51e9', '0ee110801aee408cadd23d85a3163ad6', '2026-06-06', '周六', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('9babd654d4ee45d7affb4609a115f2d3', '0ee110801aee408cadd23d85a3163ad6', '2026-06-01', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('abe79de6fd934627b9c814c401a5ee20', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-06', '周六', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('b77350f6f6364680b496d493e72c56b2', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-05', '周五', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('c4775787aaf4484a840faa4101a9965e', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-04', '周四', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('c9611f01130040868c1cbd55be425d82', '0ee110801aee408cadd23d85a3163ad6', '2026-05-31', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', '1', '1', '1', '1');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220001', 'SUB202605220001', '2026-05-10', '星期日', '北京', 'BJ', '到达北京', 80.00, 50.00, 20.00, 80.00, 50.00, 20.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220002', 'SUB202605220001', '2026-05-11', '星期一', '北京', 'BJ', '北京出差', 80.00, 50.00, 20.00, 80.00, 50.00, 20.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220003', 'SUB202605220001', '2026-05-12', '星期二', '北京', 'BJ', '返回武汉', 80.00, 50.00, 20.00, 80.00, 50.00, 20.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220004', 'SUB202605220002', '2026-05-15', '星期五', '上海', 'SH', '到达上海', 90.00, 60.00, 30.00, 90.00, 60.00, 30.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220005', 'SUB202605220002', '2026-05-16', '星期六', '上海', 'SH', '会议结束返回', 90.00, 60.00, 30.00, 90.00, 60.00, 30.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220006', 'SUB202605220003', '2026-05-18', '星期一', '广州', 'GZ', '到达广州', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220007', 'SUB202605220003', '2026-05-19', '星期二', '广州', 'GZ', '系统部署', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220008', 'SUB202605220003', '2026-05-20', '星期三', '广州', 'GZ', '上线支持', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('CAL202605220009', 'SUB202605220003', '2026-05-21', '星期四', '广州', 'GZ', '返回武汉', 75.00, 40.00, 15.00, 75.00, 40.00, 15.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862805861_pbtioq', 'subsidy_1779953966944_cikerl', '2026-05-27', NULL, '北京', '10119', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862819195_9m1qcb', 'subsidy_1779953966944_cikerl', '2026-05-06', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862819195_bpp1dp', 'subsidy_1779953966944_cikerl', '2026-05-05', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862855937_ctddey', 'subsidy_1779953966944_cikerl', '2026-05-06', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1779862855937_oh0xq8', 'subsidy_1779953966944_cikerl', '2026-05-05', NULL, '上海', '10621', NULL, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_2wgr5m', 'subsidy_1780970397079_biw91r', '2026-06-08', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_4mypon', 'subsidy_1780970397079_biw91r', '2026-06-03', '周三', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_78uxvl', 'subsidy_1780970397079_biw91r', '2026-05-31', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_8acu5g', 'subsidy_1780970397079_biw91r', '2026-06-06', '周六', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_d8xdsm', 'subsidy_1780970397079_biw91r', '2026-06-02', '周二', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_dga6ht', 'subsidy_1780970397079_biw91r', '2026-06-05', '周五', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_lcvp25', 'subsidy_1780970397079_biw91r', '2026-06-07', '周日', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_uhd32e', 'subsidy_1780970397079_biw91r', '2026-06-01', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('calendar_1780970397079_yuxidi', 'subsidy_1780970397079_biw91r', '2026-06-04', '周四', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('e17e3a96084946038a4f4b76a474eb62', '2dcd726ab4e24753bc28391740315e88', '2026-06-01', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('e73c6bac27654151b4721fcaaddf0e69', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-08', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('e8390cef5337434086dd0eba82a30ee8', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-01', '周一', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('ea4bef7483764b17b85d95e98debfb3a', 'eaf0b149f77e4f2da628bf68f0853e67', '2026-06-03', '周三', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');
INSERT INTO `fk_subsidy_calendar` VALUES ('ec7786923a1e49faad89f4ca3bee1b47', '2dcd726ab4e24753bc28391740315e88', '2026-06-06', '周六', '上海', '10621', NULL, 100.00, 40.00, 40.00, 100.00, 40.00, 40.00, '1', NULL, '0', '0', '0');

SET FOREIGN_KEY_CHECKS = 1;
