@echo off
echo 诊断测试 - 逐步检查接口...

echo.
echo 1. 测试会议分类接口
curl -X GET "http://localhost:8088/api/conferences/categories"

echo.
echo 2. 测试会议列表接口
curl -X GET "http://localhost:8088/api/conferences"

echo.
echo 3. 测试登录接口
curl -X POST "http://localhost:8088/api/admin/login" -H "Content-Type: application/json" -d "{\"username\":\"superadmin\",\"password\":\"superadmin123\"}"

echo.
echo 4. 测试标准定制详情接口
curl -X GET "http://localhost:8088/api/conferences/1/standard-detail"

echo.
echo 5. 测试技术培训详情接口
curl -X GET "http://localhost:8088/api/conferences/1/training-detail"

echo.
echo 6. 测试工具研发详情接口
curl -X GET "http://localhost:8088/api/conferences/1/tool-detail"

echo.
echo 诊断完成！
pause 