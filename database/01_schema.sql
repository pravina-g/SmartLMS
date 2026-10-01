CREATE DATABASE smart_lms;
USE smart_lms;
CREATE TABLE roles(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE users(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE user_roles(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE categories(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);
CREATE TABLE courses(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    instructor_id BIGINT NOT NULL,
    category_id BIGINT,
    duration_hours DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_courses_instructor FOREIGN KEY (instructor_id) REFERENCES users(id),
    CONSTRAINT fk_courses_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE TABLE course_modules(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    module_order INT NOT NULL,
    CONSTRAINT fk_modules_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE lessons(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    video_url VARCHAR(500),
    lesson_order INT NOT NULL,
    CONSTRAINT fk_lessons_module FOREIGN KEY (module_id) REFERENCES course_modules(id) ON DELETE CASCADE
);
CREATE TABLE topics(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lesson_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    CONSTRAINT fk_topics_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

CREATE TABLE enrollments(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id),
    CONSTRAINT fk_enrollments_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_enrollments_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE lesson_progress(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    CONSTRAINT uq_student_lesson UNIQUE (student_id, lesson_id),
    CONSTRAINT fk_progress_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_progress_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
CREATE TABLE quizzes(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    title VARCHAR(800) NOT NULL,
    description TEXT,
    pass_percentage DECIMAL(5,2) NOT NULL DEFAULT 50.00,
    CONSTRAINT fk_quizzes_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE questions(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    marks DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    CONSTRAINT fk_questions_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
CREATE TABLE question_options(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_options_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);
CREATE TABLE question_topics(
    question_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    PRIMARY KEY (question_id, topic_id),
    CONSTRAINT fk_question_topics_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT fk_question_topics_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
);
CREATE TABLE quiz_attempts(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attempts_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_attempts_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
CREATE TABLE quiz_answers(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_answers_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_option FOREIGN KEY (selected_option_id) REFERENCES question_options(id) ON DELETE SET NULL
);
CREATE TABLE student_topic_performance(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    total_questions INT NOT NULL DEFAULT 0,
    correct_answers INT NOT NULL DEFAULT 0,
    score_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    performance_status VARCHAR(30) NOT NULL DEFAULT 'WEAK',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_topic UNIQUE (student_id, topic_id),
    CONSTRAINT fk_performance_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_performance_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
);
CREATE TABLE recommendations(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    lesson TEXT NOT NULL,
    reason TEXT NOT NULL,
    recommendation_test TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_recommendations_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendations_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendations_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
CREATE TABLE certificates(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    certificate_number VARCHAR(100) NOT NULL UNIQUE,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_certificates_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_certificates_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT uq_student_certificate UNIQUE (student_id, course_id)
);