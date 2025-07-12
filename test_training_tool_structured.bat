@echo off
echo 测试TrainingDetail和ToolDetail的结构化字段适配...

echo.
echo 1. 测试获取技术培训详情（会议ID=1）
curl -X GET "http://localhost:8088/api/conferences/1/training-detail" -H "Content-Type: application/json"

echo.
echo 2. 测试获取工具研发详情（会议ID=1）
curl -X GET "http://localhost:8088/api/conferences/1/tool-detail" -H "Content-Type: application/json"

echo.
echo 3. 测试新增技术培训详情（会议ID=1）
curl -X POST "http://localhost:8088/api/conferences/1/training-detail" -H "Content-Type: application/json" -d "{\"standardName\":\"Spring Boot高级培训\",\"standardDesc\":\"深入学习Spring Boot框架的高级特性\",\"requirements\":\"具备Java基础，了解Spring框架\",\"process\":\"理论讲解-实践操作-项目实战\",\"timeline\":\"3天培训，每天8小时\"}"

echo.
echo 4. 测试新增工具研发详情（会议ID=1）
curl -X POST "http://localhost:8088/api/conferences/1/tool-detail" -H "Content-Type: application/json" -d "{\"standardName\":\"微服务开发工具\",\"standardDesc\":\"开发企业级微服务应用的工具集\",\"requirements\":\"熟悉Java开发，了解微服务架构\",\"process\":\"需求分析-架构设计-工具开发-测试部署\",\"timeline\":\"2周开发周期\"}"

echo.
echo 5. 再次获取技术培训详情，验证结构化字段
curl -X GET "http://localhost:8088/api/conferences/1/training-detail" -H "Content-Type: application/json"

echo.
echo 6. 再次获取工具研发详情，验证结构化字段
curl -X GET "http://localhost:8088/api/conferences/1/tool-detail" -H "Content-Type: application/json"

echo.
echo 测试完成！
pause 