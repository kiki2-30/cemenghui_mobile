@echo off
echo 测试TrainingDetail和ToolDetail的结构化字段适配...

echo.
echo 1. 登录获取token
curl -X POST "http://localhost:8088/api/admin/login" -H "Content-Type: application/json" -d "{\"username\":\"superadmin\",\"password\":\"superadmin123\"}"

echo.
echo 2. 测试获取技术培训详情（会议ID=1）
curl -X GET "http://localhost:8088/api/conferences/1/training-detail" -H "Content-Type: application/json"

echo.
echo 3. 测试获取工具研发详情（会议ID=1）
curl -X GET "http://localhost:8088/api/conferences/1/tool-detail" -H "Content-Type: application/json"

echo.
echo 测试完成！
pause 