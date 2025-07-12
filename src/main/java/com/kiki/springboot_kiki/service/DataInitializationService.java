package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.Conference;
import com.kiki.springboot_kiki.Pojo.ConferenceCategory;
import com.kiki.springboot_kiki.Pojo.IndustryNews;
import com.kiki.springboot_kiki.Pojo.User;
import com.kiki.springboot_kiki.repository.ConferenceCategoryRepository;
import com.kiki.springboot_kiki.repository.ConferenceRepository;
import com.kiki.springboot_kiki.repository.IndustryNewsRepository;
import com.kiki.springboot_kiki.repository.UserRepository;
import com.kiki.springboot_kiki.util.PasswordUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private ConferenceCategoryRepository conferenceCategoryRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private IndustryNewsRepository industryNewsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化会议分类数据
        initializeConferenceCategories();
        
        // 初始化示例会议数据
        initializeSampleConferences();
        
        // 初始化示例行业动态数据
        initializeSampleNews();
    }

    private void initializeConferenceCategories() {
        // 检查是否已有分类数据
        Iterable<ConferenceCategory> existingCategories = conferenceCategoryRepository.findAll();
        
        if (!existingCategories.iterator().hasNext()) {
            // 如果没有数据，创建新的分类
            createConferenceCategories();
        } else {
            // 如果有数据，更新分类名称（特别是将"会议研讨"改为"标准制定"）
            updateConferenceCategories(existingCategories);
        }
        
        System.out.println("会议分类数据初始化完成");
    }
    
    private void createConferenceCategories() {
        ConferenceCategory category1 = new ConferenceCategory();
        category1.setCategoryName("标准制定");
        category1.setCategoryDesc("行业标准制定、规范制定等");
        category1.setSortOrder(1);
        category1.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        conferenceCategoryRepository.save(category1);

        ConferenceCategory category2 = new ConferenceCategory();
        category2.setCategoryName("标准定制");
        category2.setCategoryDesc("行业标准制定、规范制定等");
        category2.setSortOrder(2);
        category2.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        conferenceCategoryRepository.save(category2);

        ConferenceCategory category3 = new ConferenceCategory();
        category3.setCategoryName("技术培训");
        category3.setCategoryDesc("技术培训、技能提升课程等");
        category3.setSortOrder(3);
        category3.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        conferenceCategoryRepository.save(category3);

        ConferenceCategory category4 = new ConferenceCategory();
        category4.setCategoryName("工具研发");
        category4.setCategoryDesc("软件开发、工具开发等");
        category4.setSortOrder(4);
        category4.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        conferenceCategoryRepository.save(category4);

        ConferenceCategory category5 = new ConferenceCategory();
        category5.setCategoryName("公益行动");
        category5.setCategoryDesc("公益活动、志愿服务等");
        category5.setSortOrder(5);
        category5.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        conferenceCategoryRepository.save(category5);
    }
    
    private void updateConferenceCategories(Iterable<ConferenceCategory> existingCategories) {
        // 更新现有分类的名称，特别是将"会议研讨"改为"标准制定"
        for (ConferenceCategory category : existingCategories) {
            if ("会议研讨".equals(category.getCategoryName())) {
                category.setCategoryName("标准制定");
                category.setCategoryDesc("行业标准制定、规范制定等");
                conferenceCategoryRepository.save(category);
                System.out.println("已将分类'会议研讨'更新为'标准制定'");
            }
        }
    }

    private void initializeSampleConferences() {
        // 检查是否已有数据，避免重复插入
        if (conferenceRepository.count() == 0) {
            // 获取分类ID
            Iterable<ConferenceCategory> categories = conferenceCategoryRepository.findAll();
            ConferenceCategory[] categoryArray = new ConferenceCategory[5];
            int index = 0;
            for (ConferenceCategory category : categories) {
                categoryArray[index++] = category;
            }

            // 创建示例会议1
            Conference conference1 = new Conference();
            conference1.setCategoryId(categoryArray[0].getCategoryId()); // 会议研讨
            conference1.setTitle("2024年春季技术研讨会");
            conference1.setDescription("探讨最新技术发展趋势，分享行业经验，包括人工智能、云计算、大数据等热门技术话题。");
            conference1.setStartTime(LocalDateTime.of(2024, 3, 15, 9, 0));
            conference1.setEndTime(LocalDateTime.of(2024, 3, 15, 17, 0));
            conference1.setLocation("北京市朝阳区科技大厦");
            conference1.setOrganizer("技术协会");
            conference1.setMaxParticipants(100);
            conference1.setCurrentParticipants(0);
            conference1.setStatus(1);
            conference1.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
            conferenceRepository.save(conference1);

            // 创建示例会议2
            Conference conference2 = new Conference();
            conference2.setCategoryId(categoryArray[1].getCategoryId()); // 标准定制
            conference2.setTitle("行业标准制定会议");
            conference2.setDescription("制定行业技术标准，规范行业发展，推动行业标准化建设。");
            conference2.setStartTime(LocalDateTime.of(2024, 4, 20, 10, 0));
            conference2.setEndTime(LocalDateTime.of(2024, 4, 20, 16, 0));
            conference2.setLocation("上海市浦东新区会议中心");
            conference2.setOrganizer("标准委员会");
            conference2.setMaxParticipants(50);
            conference2.setCurrentParticipants(0);
            conference2.setStatus(1);
            conference2.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
            conferenceRepository.save(conference2);

            // 创建示例会议3
            Conference conference3 = new Conference();
            conference3.setCategoryId(categoryArray[2].getCategoryId()); // 技术培训
            conference3.setTitle("Java开发技能培训");
            conference3.setDescription("Java高级开发技能培训课程，包括Spring Boot、微服务架构、性能优化等内容。");
            conference3.setStartTime(LocalDateTime.of(2024, 5, 10, 9, 0));
            conference3.setEndTime(LocalDateTime.of(2024, 5, 12, 17, 0));
            conference3.setLocation("深圳市南山区培训中心");
            conference3.setOrganizer("技术培训学院");
            conference3.setMaxParticipants(30);
            conference3.setCurrentParticipants(0);
            conference3.setStatus(1);
            conference3.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
            conferenceRepository.save(conference3);

            System.out.println("示例会议数据初始化完成");
        }
    }

    private void initializeSampleNews() {
        // 检查是否已有数据，避免重复插入
        if (industryNewsRepository.count() == 0) {
            // 创建示例动态1
            IndustryNews news1 = new IndustryNews();
            news1.setTitle("人工智能技术发展新趋势");
            news1.setSummary("探讨AI技术的最新发展方向和应用前景");
            news1.setContent("人工智能技术正在快速发展，从机器学习到深度学习，再到现在的生成式AI，技术不断突破。ChatGPT的出现标志着AI技术进入了一个新的阶段，各种大语言模型如雨后春笋般涌现。未来，AI将在医疗、教育、金融、制造等各个领域发挥重要作用。");
            news1.setAuthor("技术专家");
            news1.setPublishTime(LocalDateTime.of(2024, 1, 15, 10, 0));
            news1.setViewCount(0);
            news1.setStatus(1);
            news1.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
            industryNewsRepository.save(news1);

            // 创建示例动态2
            IndustryNews news2 = new IndustryNews();
            news2.setTitle("云计算行业分析报告");
            news2.setSummary("2024年云计算市场发展趋势分析");
            news2.setContent("随着数字化转型的深入推进，云计算市场需求持续增长，企业上云成为必然趋势。公有云、私有云、混合云等多种部署模式并存，为企业提供了灵活的选择。云原生技术、边缘计算、多云管理等成为行业热点。预计到2025年，全球云计算市场规模将达到数千亿美元。");
            news2.setAuthor("行业分析师");
            news2.setPublishTime(LocalDateTime.of(2024, 1, 20, 14, 30));
            news2.setViewCount(0);
            news2.setStatus(1);
            news2.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
            industryNewsRepository.save(news2);

            // 创建示例动态3
            IndustryNews news3 = new IndustryNews();
            news3.setTitle("移动端开发技术革新");
            news3.setSummary("移动端开发技术的最新发展动态");
            news3.setContent("移动端开发技术正在经历重大变革，从原生开发到跨平台开发，再到现在的鸿蒙开发。鸿蒙系统的推出为移动端开发带来了新的机遇和挑战。HarmonyOS的分布式能力、一次开发多端部署等特性，为开发者提供了全新的开发体验。同时，Flutter、React Native等跨平台框架也在不断发展。");
            news3.setAuthor("移动开发专家");
            news3.setPublishTime(LocalDateTime.of(2024, 1, 25, 16, 0));
            news3.setViewCount(0);
            news3.setStatus(1);
            news3.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
            industryNewsRepository.save(news3);

            System.out.println("示例行业动态数据初始化完成");
        }
    }

    /**
     * 初始化超级管理员
     */
    @PostConstruct
    public void initializeSuperAdmin() {
        // 检查是否已存在超级管理员
        List<User> superAdmins = userRepository.findByRole(User.RoleType.SUPER_ADMIN);
        if (superAdmins.isEmpty()) {
            // 创建超级管理员
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(PasswordUtil.encode("superadmin123"));
            superAdmin.setEmail("admin@system.com");
            superAdmin.setPhone("13800000000");
            superAdmin.setRealName("系统管理员");
            superAdmin.setCompany("系统管理");
            superAdmin.setRole(User.RoleType.SUPER_ADMIN);
            superAdmin.setCreateTime(LocalDateTime.now());
            superAdmin.setUpdateTime(LocalDateTime.now());
            
            userRepository.save(superAdmin);
            System.out.println("超级管理员账号创建成功: superadmin / superadmin123");
        } else {
            System.out.println("超级管理员已存在，无需创建");
        }
        
        // 更新行业动态的图片链接
        updateNewsCoverImages();
    }
    
    /**
     * 更新行业动态的封面图片链接
     */
    private void updateNewsCoverImages() {
        try {
            // 获取所有行业动态
            Iterable<IndustryNews> allNews = industryNewsRepository.findAll();
            boolean hasUpdates = false;
            
            for (IndustryNews news : allNews) {
                // 强制更新所有新闻的封面图片链接
                String coverImageUrl = getCoverImageUrlByNewsId(news.getNewsId());
                news.setCoverImage(coverImageUrl);
                industryNewsRepository.save(news);
                hasUpdates = true;
                System.out.println("更新新闻ID " + news.getNewsId() + " 的封面图片: " + coverImageUrl);
            }
            
            if (hasUpdates) {
                System.out.println("行业动态封面图片更新完成");
            } else {
                System.out.println("所有行业动态已有合适的封面图片");
            }
        } catch (Exception e) {
            System.err.println("更新行业动态封面图片时出错: " + e.getMessage());
        }
    }
    
    /**
     * 根据新闻ID获取合适的封面图片URL
     */
    private String getCoverImageUrlByNewsId(Integer newsId) {
        // 根据新闻ID返回不同的图片链接 - 使用Unsplash高质量图片资源
        switch (newsId) {
            case 1:
                return "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&h=300&fit=crop"; // 人工智能相关图片
            case 2:
                return "https://images.unsplash.com/photo-1519125323398-675f0ddb6308?w=400&h=300&fit=crop"; // 云计算相关图片
            case 3:
                return "https://images.unsplash.com/photo-1465101046530-73398c7f28ca?w=400&h=300&fit=crop"; // 移动开发相关图片
            default:
                return "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&h=300&fit=crop"; // 默认图片
        }
    }
} 