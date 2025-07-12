package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.UserBehavior;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBehaviorRepository extends CrudRepository<UserBehavior, Integer> {
    @Query("SELECT COUNT(ub) FROM UserBehavior ub WHERE ub.targetType = 'news' AND ub.targetId = :newsId AND ub.behaviorType = :behaviorType")
    Long countByNewsIdAndBehaviorType(@Param("newsId") Integer newsId, @Param("behaviorType") String behaviorType);

    @Query("SELECT FUNCTION('DATE_FORMAT', ub.createdTime, '%Y-%m-%d') as day, COUNT(ub) as cnt FROM UserBehavior ub WHERE (:behaviorType IS NULL OR ub.behaviorType = :behaviorType) AND (:startDate IS NULL OR ub.createdTime >= :startDate) AND (:endDate IS NULL OR ub.createdTime <= :endDate) GROUP BY day ORDER BY day")
    java.util.List<Object[]> countBehaviorTrendByDay(@Param("behaviorType") String behaviorType, @Param("startDate") java.sql.Timestamp startDate, @Param("endDate") java.sql.Timestamp endDate);
    
    /**
     * 根据用户ID删除用户行为记录
     */
    void deleteByUserId(Integer userId);
    
    /**
     * 根据用户ID查找用户行为记录
     */
    java.util.List<UserBehavior> findByUserId(Integer userId);
} 