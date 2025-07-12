-- 更新会议分类数据
-- 将第一个分类从"会议研讨"改为"标准制定"

UPDATE tb_conference_category 
SET category_name = '标准制定', 
    category_desc = '行业标准制定、规范制定等'
WHERE category_id = 1;

-- 查看更新结果
SELECT * FROM tb_conference_category ORDER BY sort_order; 