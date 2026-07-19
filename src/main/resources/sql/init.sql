-- ============================================


-- BMI体质评估与预测系统 数据库初始化脚本（增强版）
-- 使用前请先安装MySQL，然后在Navicat中执行此脚本
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS bmi_health_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE bmi_health_db;

-- 用户表
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `gender` VARCHAR(10) DEFAULT '男' COMMENT '性别',
    `age` INT DEFAULT 0 COMMENT '年龄',
    `height` DOUBLE DEFAULT 0 COMMENT '身高(cm)',
    `weight` DOUBLE DEFAULT 0 COMMENT '体重(kg)',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `role` VARCHAR(20) DEFAULT 'user' COMMENT '角色: admin/user',
    `status` INT DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 食谱分类表
DROP TABLE IF EXISTS `recipe_categories`;
CREATE TABLE `recipe_categories` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(200) COMMENT '分类描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱分类表';

-- 食谱信息表
DROP TABLE IF EXISTS `recipes`;
CREATE TABLE `recipes` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '食谱ID',
    `category_id` INT NOT NULL COMMENT '分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '食谱名称',
    `description` TEXT COMMENT '食谱描述',
    `calories` DOUBLE DEFAULT 0 COMMENT '热量(千卡)',
    `protein` DOUBLE DEFAULT 0 COMMENT '蛋白质(g)',
    `fat` DOUBLE DEFAULT 0 COMMENT '脂肪(g)',
    `carbs` DOUBLE DEFAULT 0 COMMENT '碳水化合物(g)',
    `fiber` DOUBLE DEFAULT 0 COMMENT '膳食纤维(g)',
    `cooking_method` VARCHAR(50) COMMENT '烹饪方式',
    `cooking_time` INT DEFAULT 0 COMMENT '烹饪时间(分钟)',
    `difficulty` VARCHAR(20) DEFAULT '简单' COMMENT '难度: 简单/中等/困难',
    `suitable_bmi` VARCHAR(50) COMMENT '适合BMI范围',
    `image_url` VARCHAR(200) COMMENT '图片路径',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`category_id`) REFERENCES `recipe_categories`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱信息表';

-- 材料信息表
DROP TABLE IF EXISTS `ingredients`;
CREATE TABLE `ingredients` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '材料ID',
    `name` VARCHAR(50) NOT NULL COMMENT '材料名称',
    `description` VARCHAR(200) COMMENT '材料描述',
    `calories` DOUBLE DEFAULT 0 COMMENT '热量(千卡/100g)',
    `unit` VARCHAR(20) DEFAULT 'g' COMMENT '单位',
    `category` VARCHAR(50) COMMENT '材料分类(蔬菜/肉类/谷物等)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='材料信息表';

-- 食谱材料关联表
DROP TABLE IF EXISTS `recipe_ingredients`;
CREATE TABLE `recipe_ingredients` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `recipe_id` INT NOT NULL COMMENT '食谱ID',
    `ingredient_id` INT NOT NULL COMMENT '材料ID',
    `amount` VARCHAR(50) COMMENT '用量',
    FOREIGN KEY (`recipe_id`) REFERENCES `recipes`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱材料关联表';

-- 食谱健康安排表
DROP TABLE IF EXISTS `recipe_schedules`;
CREATE TABLE `recipe_schedules` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '安排ID',
    `recipe_id` INT NOT NULL COMMENT '食谱ID',
    `user_id` INT COMMENT '用户ID(为空则通用)',
    `meal_type` VARCHAR(20) COMMENT '餐次: 早餐/午餐/晚餐/加餐',
    `schedule_date` DATE COMMENT '安排日期',
    `period_start` DATE COMMENT '周期开始',
    `period_end` DATE COMMENT '周期结束',
    `target_bmi_range` VARCHAR(50) COMMENT '目标BMI范围',
    `notes` VARCHAR(200) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`recipe_id`) REFERENCES `recipes`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱健康安排表';

-- 收藏表
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `recipe_id` INT NOT NULL COMMENT '食谱ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE KEY `uk_user_recipe` (`user_id`, `recipe_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`recipe_id`) REFERENCES `recipes`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 论坛帖子表（增强：增加分类字段）
DROP TABLE IF EXISTS `forum_posts`;
CREATE TABLE `forum_posts` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    `user_id` INT NOT NULL COMMENT '发帖用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `category` VARCHAR(50) DEFAULT '综合讨论' COMMENT '帖子分类: BMI健康讨论/减脂经验/增肌分享/营养知识/综合讨论',
    `views` INT DEFAULT 0 COMMENT '浏览量',
    `likes` INT DEFAULT 0 COMMENT '点赞数',
    `is_hot` INT DEFAULT 0 COMMENT '是否热门: 1是 0否',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- 论坛回复表
DROP TABLE IF EXISTS `forum_replies`;
CREATE TABLE `forum_replies` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '回复ID',
    `post_id` INT NOT NULL COMMENT '帖子ID',
    `user_id` INT NOT NULL COMMENT '回复用户ID',
    `content` TEXT NOT NULL COMMENT '回复内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`post_id`) REFERENCES `forum_posts`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛回复表';

-- 帖子点赞表
DROP TABLE IF EXISTS `post_likes`;
CREATE TABLE `post_likes` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    `post_id` INT NOT NULL COMMENT '帖子ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
    FOREIGN KEY (`post_id`) REFERENCES `forum_posts`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞表';

-- BMI记录表
DROP TABLE IF EXISTS `bmi_records`;
CREATE TABLE `bmi_records` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `height` DOUBLE NOT NULL COMMENT '身高(cm)',
    `weight` DOUBLE NOT NULL COMMENT '体重(kg)',
    `bmi` DOUBLE NOT NULL COMMENT 'BMI值',
    `assessment` VARCHAR(50) COMMENT '评估结果',
    `recommendation` TEXT COMMENT '膳食建议',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BMI记录表';

-- 健康知识库表
DROP TABLE IF EXISTS `health_tips`;
CREATE TABLE `health_tips` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '知识ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `category` VARCHAR(50) DEFAULT 'BMI知识' COMMENT '分类: BMI知识/营养知识/运动知识/心理健康/饮食误区',
    `author` VARCHAR(50) DEFAULT '系统管理员' COMMENT '作者',
    `views` INT DEFAULT 0 COMMENT '浏览量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康知识库表';

-- 膳食摄入记录表
DROP TABLE IF EXISTS `diet_records`;
CREATE TABLE `diet_records` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `record_date` DATE NOT NULL COMMENT '摄入日期',
    `carbs` DOUBLE DEFAULT 0 COMMENT '碳水化合物(g)',
    `fat` DOUBLE DEFAULT 0 COMMENT '脂肪(g)',
    `protein` DOUBLE DEFAULT 0 COMMENT '蛋白质(g)',
    `calories` DOUBLE DEFAULT 0 COMMENT '热量(大卡)',
    `fiber` DOUBLE DEFAULT 0 COMMENT '膳食纤维(g)',
    `cholesterol` DOUBLE DEFAULT 0 COMMENT '胆固醇(mmol)',
    `water` DOUBLE DEFAULT 0 COMMENT '饮水量(ml)',
    `note` VARCHAR(200) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    UNIQUE KEY `uk_user_date` (`user_id`, `record_date`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='膳食摄入记录表';

-- 运动推荐表
DROP TABLE IF EXISTS `exercise_recommendations`;
CREATE TABLE `exercise_recommendations` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '推荐ID',
    `name` VARCHAR(100) NOT NULL COMMENT '运动名称',
    `description` TEXT COMMENT '运动描述',
    `duration` INT DEFAULT 30 COMMENT '建议时长(分钟)',
    `frequency` VARCHAR(50) DEFAULT '每周3次' COMMENT '建议频率',
    `calories_burned` DOUBLE DEFAULT 0 COMMENT '消耗热量(千卡/小时)',
    `suitable_bmi` VARCHAR(50) COMMENT '适合BMI范围',
    `difficulty` VARCHAR(20) DEFAULT '中等' COMMENT '难度: 简单/中等/困难',
    `category` VARCHAR(50) DEFAULT '有氧运动' COMMENT '运动分类: 有氧运动/力量训练/柔韧拉伸/综合训练',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动推荐表';

-- ============================================
-- 初始数据
-- ============================================

-- 管理员账户 (用户名: admin, 密码: admin123)
INSERT INTO `users` (`username`, `password`, `real_name`, `gender`, `role`, `status`) VALUES
('admin', 'admin123', '系统管理员', '男', 'admin', 1);

-- 测试用户
INSERT INTO `users` (`username`, `password`, `real_name`, `gender`, `age`, `height`, `weight`, `phone`, `role`, `status`) VALUES
('user1', '123456', '张三', '男', 25, 175, 70, '13800138000', 'user', 1),
('user2', '123456', '李四', '女', 22, 160, 55, '13900139000', 'user', 1),
('user3', '123456', '王五', '男', 35, 180, 95, '15000150000', 'user', 1),
('user4', '123456', '赵敏', '女', 28, 165, 48, '13700137000', 'user', 1);

-- 食谱分类（扩展到8大类）
INSERT INTO `recipe_categories` (`name`, `description`) VALUES
('低脂食谱', '适合超重和肥胖人群的低脂饮食方案'),
('高蛋白食谱', '适合需要增肌或补充蛋白质的人群'),
('均衡膳食', '营养均衡的日常饮食推荐'),
('素食食谱', '以植物性食材为主的饮食方案'),
('养生汤品', '各类养生保健汤品推荐'),
('减脂瘦身', '专为减脂瘦身设计的低卡高纤维食谱'),
('运动营养', '适合运动人群的营养补充食谱'),
('糖尿病饮食', '适合糖尿病患者及控糖需求的食谱');

-- 食谱信息（扩展到30+条）
INSERT INTO `recipes` (`category_id`, `name`, `description`, `calories`, `protein`, `fat`, `carbs`, `fiber`, `cooking_method`, `cooking_time`, `difficulty`, `suitable_bmi`) VALUES
-- 低脂食谱
(1, '清蒸鸡胸肉', '低脂高蛋白的鸡胸肉，简单清蒸保留原味，是健身减脂的首选', 165, 31, 3.6, 0, 0, '蒸', 25, '简单', '18.5-24.9'),
(1, '蔬菜沙拉', '多种新鲜蔬菜搭配橄榄油醋汁，低卡健康，富含维生素', 120, 5, 3, 15, 5, '拌', 10, '简单', '25-29.9'),
(1, '低脂酸奶水果杯', '低脂酸奶搭配新鲜水果，清爽可口，适合早餐或加餐', 150, 8, 2, 20, 3, '拌', 5, '简单', '25-29.9'),
-- 高蛋白食谱
(2, '牛肉西兰花', '高蛋白牛肉搭配营养丰富的西兰花，增肌必备', 280, 35, 12, 10, 4, '炒', 20, '中等', '18.5-24.9'),
(2, '鸡蛋牛奶套餐', '优质蛋白组合，适合早餐，开启活力一天', 250, 20, 12, 15, 0, '煮', 15, '简单', '<18.5'),
(2, '三文鱼配芦笋', '富含Omega-3的三文鱼搭配芦笋，促进肌肉修复', 320, 28, 15, 8, 3, '煎', 25, '中等', '18.5-24.9'),
(2, '蛋白质奶昔', '蛋白粉+牛奶+香蕉，运动后快速补充蛋白质', 220, 25, 5, 25, 2, '搅拌', 5, '简单', '18.5-24.9'),
-- 均衡膳食
(3, '杂粮米饭', '多种谷物混合，营养均衡，富含膳食纤维', 200, 6, 2, 40, 5, '煮', 40, '简单', '18.5-24.9'),
(3, '番茄炖牛腩', '家常炖菜，营养美味，番茄提供丰富维生素C', 350, 25, 15, 20, 4, '炖', 90, '中等', '18.5-24.9'),
(3, '鸡胸肉蔬菜卷', '鸡胸肉搭配多种蔬菜制成卷饼，营养均衡', 280, 22, 8, 25, 4, '煎', 30, '中等', '18.5-24.9'),
-- 素食食谱
(4, '豆腐蔬菜煲', '素食者的蛋白质来源，搭配多种蔬菜', 180, 15, 8, 12, 4, '煲', 30, '简单', '18.5-24.9'),
(4, '素炒时蔬', '当季蔬菜素炒，清淡健康，低卡高纤维', 100, 4, 3, 12, 5, '炒', 15, '简单', '25-29.9'),
(4, '藜麦蔬菜沙拉', '藜麦搭配烤蔬菜，植物蛋白与纤维的完美组合', 220, 12, 6, 28, 6, '拌', 20, '简单', '18.5-24.9'),
-- 养生汤品
(5, '银耳莲子汤', '滋阴润肺的养生甜汤，美容养颜', 150, 3, 1, 30, 2, '煮', 60, '简单', '18.5-24.9'),
(5, '冬瓜排骨汤', '利水消肿，清淡可口，适合水肿体质', 200, 15, 8, 10, 2, '煮', 45, '简单', '25-29.9'),
(5, '当归羊肉汤', '温补气血的养生汤品，适合体质虚弱者', 280, 20, 12, 8, 1, '煮', 60, '中等', '<18.5'),
(5, '山药枸杞粥', '健脾养胃，滋补肝肾的养生粥品', 180, 5, 2, 30, 4, '煮', 40, '简单', '18.5-24.9'),
-- 减脂瘦身
(6, '代餐蛋白棒', '低卡高蛋白代餐，适合减脂期替代主食', 180, 20, 3, 12, 3, '无需烹饪', 0, '简单', '25-29.9'),
(6, '魔芋凉皮', '几乎零卡的魔芋制品，减脂期零食替代', 30, 1, 0, 5, 4, '拌', 5, '简单', '25-29.9'),
(6, '水煮虾仁配蔬菜', '极低脂肪高蛋白的水煮虾仁，搭配西兰花', 140, 25, 2, 5, 3, '煮', 15, '简单', '>=30'),
(6, '燕麦酸奶碗', '燕麦+低脂酸奶+蓝莓，健康减脂早餐', 180, 10, 3, 22, 5, '拌', 5, '简单', '25-29.9'),
-- 运动营养
(7, '能量香蕉燕麦饼', '运动前补充能量的健康饼', 280, 8, 5, 40, 4, '烤', 30, '中等', '18.5-24.9'),
(7, '增肌牛肉饭', '高蛋白牛肉搭配糙米，运动后恢复必备', 450, 40, 18, 35, 4, '炒', 25, '中等', '18.5-24.9'),
(7, '运动后恢复饮品', '蛋白粉+蜂蜜+牛奶的恢复饮品', 200, 22, 4, 18, 0, '搅拌', 5, '简单', '18.5-24.9'),
-- 糖尿病饮食
(8, '苦瓜炒鸡蛋', '苦瓜有降糖作用，搭配鸡蛋营养丰富', 160, 12, 8, 8, 3, '炒', 15, '简单', '18.5-24.9'),
(8, '荞麦面条', '低GI的荞麦面条，适合控糖人群', 220, 8, 2, 35, 6, '煮', 15, '简单', '18.5-24.9'),
(8, '蒸南瓜配核桃', '南瓜低GI，核桃提供健康脂肪', 180, 5, 8, 18, 4, '蒸', 20, '简单', '25-29.9');

-- 材料信息（扩展到20+条）
INSERT INTO `ingredients` (`name`, `description`, `calories`, `unit`, `category`) VALUES
('鸡胸肉', '低脂高蛋白肉类，健身减脂首选', 133, 'g', '肉类'),
('牛肉', '富含蛋白质和铁元素，增肌推荐', 125, 'g', '肉类'),
('三文鱼', '富含Omega-3脂肪酸，促进恢复', 208, 'g', '鱼类'),
('虾仁', '低脂高蛋白海鲜', 87, 'g', '海鲜'),
('西兰花', '富含维生素C和膳食纤维', 34, 'g', '蔬菜'),
('番茄', '富含番茄红素和维生素C', 18, 'g', '蔬菜'),
('芦笋', '富含叶酸和维生素K', 20, 'g', '蔬菜'),
('苦瓜', '具有降糖作用的蔬菜', 19, 'g', '蔬菜'),
('南瓜', '低GI食物，适合控糖', 22, 'g', '蔬菜'),
('鸡蛋', '优质蛋白质来源', 144, 'g', '蛋类'),
('牛奶', '富含钙质和蛋白质', 54, 'ml', '乳制品'),
('低脂酸奶', '低脂高蛋白酸奶', 45, 'ml', '乳制品'),
('豆腐', '植物蛋白优质来源', 81, 'g', '豆制品'),
('藜麦', '高蛋白谷物，素食者优选', 368, 'g', '谷物'),
('杂粮', '多种谷物混合，富含膳食纤维', 340, 'g', '谷物'),
('荞麦', '低GI谷物，适合控糖', 342, 'g', '谷物'),
('燕麦', '高纤维低GI早餐优选', 389, 'g', '谷物'),
('魔芋', '几乎零卡高纤维食材', 7, 'g', '特殊食材'),
('银耳', '滋阴润肺的食材', 200, 'g', '干货'),
('莲子', '养心安神的食材', 344, 'g', '干货'),
('当归', '补血活血的中药材', 275, 'g', '中药材'),
('山药', '健脾养胃的食材', 57, 'g', '蔬菜'),
('核桃', '富含健康脂肪和蛋白质', 654, 'g', '坚果'),
('香蕉', '运动能量补充水果', 89, 'g', '水果'),
('蓝莓', '富含抗氧化物的水果', 57, 'g', '水果'),
('冬瓜', '低热量利水蔬菜', 11, 'g', '蔬菜'),
('排骨', '富含蛋白质和钙质', 278, 'g', '肉类'),
('生菜', '低热量绿叶蔬菜', 13, 'g', '蔬菜'),
('黄瓜', '清热利水低热量', 15, 'g', '蔬菜'),
('胡萝卜', '富含胡萝卜素', 37, 'g', '蔬菜');

-- 论坛帖子（增加分类和更多内容）
INSERT INTO `forum_posts` (`user_id`, `title`, `content`, `category`, `views`, `likes`, `is_hot`) VALUES
(2, '分享我的减脂餐食谱', '大家好，我通过控制饮食成功减脂，分享一下我的食谱：早餐燕麦牛奶，午餐鸡胸肉沙拉，晚餐蔬菜汤。坚持三个月BMI从27降到了23！关键是要坚持，偶尔可以放纵一天，但第二天一定要恢复正常饮食。', '减脂经验', 128, 15, 1),
(3, '增肌期饮食建议', '增肌期需要摄入充足的蛋白质，建议每天每公斤体重摄入1.5-2g蛋白质。推荐牛肉、鸡胸肉、鸡蛋和牛奶。另外碳水也不能少，糙米和燕麦是不错的选择。训练后30分钟内补充蛋白粉效果最佳。', '增肌分享', 86, 12, 0),
(2, 'BMI从30降到24的历程', '一年前我BMI高达30，属于肥胖范围。经过一年的努力：每天跑步30分钟+控制饮食+坚持记录体重。现在BMI24，进入正常范围！分享几个关键点：1.循序渐进不要急；2.饮食比运动更重要；3.找个伙伴一起坚持。', '减脂经验', 256, 38, 1),
(4, '偏瘦如何健康增重', '作为BMI只有17.5的偏瘦女生，分享增重心得：不是暴饮暴食，而是规律地增加热量摄入。每天多加两次健康加餐（坚果+酸奶），配合力量训练增加肌肉量。两个月体重增加了3kg！', '增肌分享', 67, 8, 0),
(1, 'BMI指数详解：你知道你的体质指数吗', 'BMI(Body Mass Index)是衡量人体胖瘦程度的重要指标。计算公式：BMI = 体重(kg) / 身高(m)²。中国标准：偏瘦<18.5，正常18.5-24，超重24-28，肥胖>=28。建议每个人定期计算自己的BMI，了解身体状况。', 'BMI健康讨论', 198, 25, 1),
(3, '运动前后怎么吃效果最好', '运动前1-2小时：适量碳水（燕麦、香蕉）提供能量；运动后30分钟：蛋白质+碳水（蛋白粉+水果）促进恢复；长时间运动中：补充电解质和水。切记不要空腹运动也不要刚吃饱就运动！', '营养知识', 145, 18, 0),
(4, '减脂期常见的饮食误区', '误区1：完全不吃脂肪→健康脂肪(坚果、鱼油)其实有助于减脂；误区2：只吃蔬菜→蛋白质不足会掉肌肉；误区3：不吃主食→碳水是身体主要能量来源；误区4：节食→基础代谢会降低反而更难减。', '营养知识', 312, 45, 1),
(2, '每天30分钟运动计划分享', '分享我的每日运动计划：周一跑步30分钟；周三力量训练(深蹲+俯卧撑+引体向上)；周五游泳40分钟；周日瑜伽放松。坚持这个计划3个月，体重下降了5kg，BMI从26降到了23.5！', '增肌分享', 98, 14, 0);

-- 论坛回复
INSERT INTO `forum_replies` (`post_id`, `user_id`, `content`) VALUES
(1, 3, '感谢分享！我也要试试这个食谱方案'),
(1, 2, '坚持最重要，加油！'),
(2, 2, '学习了，蛋白质摄入确实很重要'),
(3, 4, '太励志了！我也BMI29了，我要开始行动'),
(3, 1, '一年时间很合理，循序渐进才是王道'),
(5, 2, '感谢科普，终于搞明白了BMI的计算方法'),
(6, 4, '原来运动后30分钟补充蛋白质这么重要！'),
(7, 2, '这些误区我都踩过！尤其是节食那一条，基础代谢真的会降'),
(7, 3, '健康脂肪确实很重要，我每天吃一把坚果'),
(8, 4, '这个运动计划很实用，我也想试试游泳');

-- 帖子点赞（初始数据）
INSERT INTO `post_likes` (`post_id`, `user_id`) VALUES
(1, 3), (1, 4),
(3, 2), (3, 4),
(5, 2), (5, 3), (5, 4),
(7, 2), (7, 1), (7, 3);

-- BMI记录
INSERT INTO `bmi_records` (`user_id`, `height`, `weight`, `bmi`, `assessment`, `recommendation`) VALUES
(2, 175, 70, 22.86, '正常', '保持当前饮食和运动习惯，注意营养均衡'),
(3, 160, 55, 21.48, '正常', '体重正常，建议保持规律饮食和适量运动'),
(4, 180, 95, 29.30, '偏胖', '建议增加运动量，减少高热量食物摄入，每日至少30分钟有氧运动'),
(5, 165, 48, 17.61, '偏瘦', '建议增加蛋白质和碳水摄入，适当增加餐次，配合力量训练增肌');

-- 健康知识库（丰富内容）
INSERT INTO `health_tips` (`title`, `content`, `category`, `author`, `views`) VALUES
('什么是BMI？如何正确计算', 'BMI（Body Mass Index，体质指数）是衡量人体胖瘦程度的重要指标。计算公式：BMI = 体重(kg) ÷ 身高(m)²。例如：身高1.75m，体重70kg，BMI = 70 ÷ 1.75² = 22.86。中国成人BMI标准：偏瘦<18.5，正常18.5-24，超重24-28，肥胖>=28。', 'BMI知识', '系统管理员', 156),
('BMI正常范围的饮食建议', 'BMI在18.5-24属于正常范围，建议：1.保持均衡饮食，每日三餐规律；2.适当摄入蛋白质(每公斤体重0.8-1g)；3.多吃蔬菜水果保证纤维摄入；4.适量运动每周150分钟中等强度；5.定期体检监测BMI变化。', 'BMI知识', '系统管理员', 98),
('BMI偏高（超重/肥胖）怎么办', 'BMI>=24为超重，>=28为肥胖。应对策略：1.控制总热量摄入，每天减少300-500千卡；2.减少精制碳水和高脂食物；3.增加蛋白质和纤维摄入；4.每周至少150分钟有氧运动（跑步、游泳等）；5.避免久坐，每小时活动5分钟；6.循序渐进，每周减重0.5-1kg为宜。', 'BMI知识', '系统管理员', 215),
('BMI偏低（偏瘦）如何增重', 'BMI<18.5属于偏瘦。增重建议：1.增加热量摄入，每天比消耗多300-500千卡；2.增加蛋白质摄入(每公斤体重1.2-1.5g)；3.多吃坚果、牛奶、鸡蛋等高营养密度食物；4.配合力量训练增加肌肉量；5.增加餐次，每天3正餐+2加餐；6.避免暴饮暴食，健康增重才是目标。', 'BMI知识', '系统管理员', 87),
('蛋白质摄入指南', '蛋白质是人体必需营养素。推荐摄入量：普通成人每公斤体重0.8-1g；减脂期1-1.2g；增肌期1.5-2g。优质蛋白来源：鸡胸肉(31g/100g)、牛肉(26g/100g)、鸡蛋(13g/个)、牛奶(3.2g/100ml)、豆腐(8g/100g)。', '营养知识', '系统管理员', 134),
('碳水化合物的真相', '碳水化合物不是"坏人"！它是身体最主要的能量来源。关键在于选择好的碳水：1.优质碳水：糙米、燕麦、藜麦、红薯（低GI、高纤维）；2.劣质碳水：白米饭、白面包、甜饮料（高GI、低纤维）；3.减脂期不要完全断碳水，选择低GI碳水更健康。', '营养知识', '系统管理员', 189),
('脂肪也有好坏之分', '脂肪是必需营养素，但需要区分好坏：1.好脂肪：不饱和脂肪酸（坚果、鱼油、橄榄油）→有助于心血管健康；2.坏脂肪：反式脂肪（油炸食品、加工零食）→增加心血管风险；3.饱和脂肪（红肉、黄油）→适量摄入即可。减脂不是不吃脂肪，而是选择好脂肪！', '营养知识', '系统管理员', 112),
('每日饮水指南', '水是生命之源！每日推荐饮水量：成人每天1500-2000ml（约8杯）。运动时额外补充500-1000ml。晨起一杯水促进代谢；饭前一杯水帮助控制食欲；运动前后及时补水；避免用饮料代替白水。', '营养知识', '系统管理员', 76),
('有氧运动与减脂的关系', '有氧运动是减脂的最佳方式！原理：1.有氧运动时身体主要消耗脂肪供能；2.推荐运动：跑步、游泳、骑行、跳绳；3.最佳时长：30-45分钟/次；4.最佳频率：每周3-5次；5.心率区间：最大心率的60-70%（燃脂区间）。', '运动知识', '系统管理员', 145),
('力量训练入门指南', '力量训练不仅增肌，还能提高基础代谢率！入门建议：1.每周2-3次，每次45-60分钟；2.基础动作：深蹲、硬拉、卧推、俯卧撑；3.渐进式增加重量；4.组数3-4组，每组8-12次；5.训练后补充蛋白质促进恢复。', '运动知识', '系统管理员', 108),
('压力与BMI的关系', '心理压力会影响体重！1.压力→皮质醇升高→食欲增加→体重上升；2.压力→情绪性进食→高热量食物摄入增加；3.应对方法：冥想、瑜伽、运动、社交；4.建立健康的压力管理机制对维持正常BMI至关重要。', '心理健康', '系统管理员', 65),
('常见的饮食误区大盘点', '误区1：不吃早餐减肥→反而增加午餐暴食风险；误区2：零碳水饮食→身体缺乏能量来源；误区3：水果代替正餐→蛋白质严重不足；误区4：只做运动不控制饮食→效果甚微；误区5：减肥药→损害健康且效果不持久。', '饮食误区', '系统管理员', 234);

-- 膳食摄入记录 (给测试用户2和3插入最近7天的历史样本, 不包含今天,
-- 以便初次使用膳食功能的用户能正常保存今日记录)
INSERT INTO `diet_records` (`user_id`, `record_date`, `carbs`, `fat`, `protein`, `calories`, `fiber`, `cholesterol`, `water`, `note`) VALUES
(2, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 280, 65, 75, 2050, 22, 1.8, 1700, '正常饮食'),
(2, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 310, 70, 82, 2200, 25, 2.0, 1850, '训练日'),
(2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 260, 58, 70, 1900, 20, 1.6, 1600, '休息日'),
(2, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 295, 66, 78, 2120, 24, 1.9, 1750, '正常'),
(2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 270, 62, 73, 1980, 21, 1.7, 1800, '正常'),
(2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 305, 72, 85, 2250, 27, 2.1, 1900, '训练日'),
(2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 285, 64, 76, 2080, 23, 1.8, 1850, '正常'),
(3, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 380, 110, 95, 2900, 18, 3.2, 1200, '高脂日'),
(3, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 360, 105, 92, 2800, 17, 3.0, 1300, '高脂'),
(3, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 350, 100, 90, 2750, 16, 2.9, 1100, '高脂'),
(3, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 370, 108, 94, 2850, 18, 3.1, 1250, '高脂'),
(3, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 355, 102, 91, 2780, 17, 3.0, 1150, '高脂'),
(3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 365, 106, 93, 2820, 18, 3.1, 1200, '高脂'),
(3, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 375, 109, 96, 2880, 19, 3.2, 1300, '高脂');

-- 运动推荐（丰富的运动方案）
INSERT INTO `exercise_recommendations` (`name`, `description`, `duration`, `frequency`, `calories_burned`, `suitable_bmi`, `difficulty`, `category`) VALUES
('慢跑', '最简单有效的有氧运动，适合大多数人。建议从快走过渡到慢跑，循序渐进增加时长。', 30, '每周3-4次', 350, '25-29.9', '简单', '有氧运动'),
('游泳', '全身性有氧运动，对关节压力小，特别适合体重较大的人群。能同时锻炼心肺和肌肉。', 40, '每周2-3次', 400, '>=30', '中等', '有氧运动'),
('骑自行车', '低冲击有氧运动，适合膝盖不好的超重人群。户外骑行还能放松心情。', 45, '每周3次', 300, '25-29.9', '简单', '有氧运动'),
('跳绳', '高效燃脂运动，10分钟跳绳≈30分钟慢跑的热量消耗。适合时间有限的人群。', 15, '每周4-5次', 450, '18.5-24.9', '中等', '有氧运动'),
('瑜伽', '柔韧性训练+心理放松，适合BMI正常人群维持体型，也适合压力大的都市人群。', 60, '每周2-3次', 150, '18.5-24.9', '简单', '柔韧拉伸'),
('深蹲', '下肢力量训练之王，能有效提高基础代谢率。建议从徒手深蹲开始，逐步增加负重。', 20, '每周3次', 250, '18.5-24.9', '中等', '力量训练'),
('俯卧撑', '上肢和核心力量训练的经典动作。可以从跪姿俯卧撑开始，逐步过渡到标准俯卧撑。', 15, '每周3-4次', 200, '18.5-24.9', '中等', '力量训练'),
('平板支撑', '核心稳定性训练，加强腹部和背部肌肉。从30秒开始，逐步增加到2分钟。', 10, '每周3-4次', 120, '18.5-24.9', '简单', '力量训练'),
('HIIT间歇训练', '高强度间歇训练，短时间高效燃脂。30秒高强度+30秒休息，循环8-10组。', 20, '每周2-3次', 500, '25-29.9', '困难', '综合训练'),
('快走', '最安全最低冲击的有氧运动，适合所有人群。饭后30分钟快走有助于消化和控制血糖。', 40, '每周5次', 180, '>=30', '简单', '有氧运动'),
('哑铃训练', '上肢力量训练，增肌塑形。建议从轻重量开始，逐步增加。每个动作3组×12次。', 30, '每周3次', 220, '<18.5', '中等', '力量训练'),
('太极拳', '中国传统养生运动，动作缓慢柔和，特别适合中老年人和体质虚弱者。', 30, '每周3-5次', 120, '>=30', '简单', '柔韧拉伸'),
('椭圆机', '低冲击有氧运动，保护膝关节的同时有效燃脂。适合健身房人群。', 30, '每周3次', 300, '25-29.9', '简单', '有氧运动'),
('拉伸运动', '运动前后的必要环节，防止受伤，促进恢复。每个部位拉伸15-30秒。', 15, '每次运动前后', 50, '18.5-24.9', '简单', '柔韧拉伸');

SET FOREIGN_KEY_CHECKS = 1;
