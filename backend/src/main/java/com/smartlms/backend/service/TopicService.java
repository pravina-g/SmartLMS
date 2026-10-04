package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.Topic;
import com.smartlms.backend.repository.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic saveTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public List<Topic> getTopicsByLessonId(Long lessonId) {
        return topicRepository.findByLessonId(lessonId);
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }
}