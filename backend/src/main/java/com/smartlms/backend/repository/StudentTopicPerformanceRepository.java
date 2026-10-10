
package com.smartlms.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.StudentTopicPerformance;

public interface StudentTopicPerformanceRepository
        extends JpaRepository<StudentTopicPerformance, Long> {

    Optional<StudentTopicPerformance> findByStudentIdAndTopicId(
            Long studentId, Long topicId);

    List<StudentTopicPerformance> findByStudentId(Long studentId);
}

