@echo off
echo 更新会议分类数据...

mysql -h localhost -P 3306 -u root -phwq springboot_kiki < update_category.sql

echo 更新完成！
pause 