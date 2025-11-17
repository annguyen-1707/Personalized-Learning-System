CREATE DATABASE  IF NOT EXISTS `fu-ohayo`;
USE `fu-ohayo`;

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
                          `admin_id` bigint NOT NULL AUTO_INCREMENT,
                          `password` varchar(255) DEFAULT NULL,
                          `username` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
--
-- Table structure for table `roles`
--


CREATE TABLE `roles` (
                         `role_id` int NOT NULL AUTO_INCREMENT,
                         `description` varchar(255) DEFAULT NULL,
                         `name` enum('CONTENT_MANAGER','PARENT','STAFF','SUPER_ADMIN','USER','USER_MANAGER') DEFAULT NULL,
                         PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `admin_roles`
--


CREATE TABLE `admin_roles` (
                               `admin_id` bigint NOT NULL,
                               `role_id` int NOT NULL,
                               PRIMARY KEY (`admin_id`,`role_id`),
                               KEY `FK3liyab508sfblqps0eqjhmjqk` (`role_id`),
                               CONSTRAINT `FK3liyab508sfblqps0eqjhmjqk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
                               CONSTRAINT `FKghcw89q6jebq3c6kocnobjusr` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `permissions`
--


CREATE TABLE `permissions` (
                               `permission_id` int NOT NULL AUTO_INCREMENT,
                               `description` varchar(255) DEFAULT NULL,
                               `name` enum('CONTENT_DELETE','CONTENT_READ','CONTENT_UPDATE','CONTENT_WRITE','USER_DELETE','USER_READ','USER_UPDATE','USER_WRITE') DEFAULT NULL,
                               PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `role_permissions`
--


CREATE TABLE `role_permissions` (
                                    `role_id` int NOT NULL,
                                    `permission_id` int NOT NULL,
                                    PRIMARY KEY (`role_id`,`permission_id`),
                                    KEY `FKegdk29eiy7mdtefy5c7eirr6e` (`permission_id`),
                                    CONSTRAINT `FKegdk29eiy7mdtefy5c7eirr6e` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`permission_id`),
                                    CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



--
-- Table structure for table `users`
--

CREATE TABLE `users` (
                         `user_id` bigint NOT NULL AUTO_INCREMENT,
                         `address` varchar(255) DEFAULT NULL,
                         `avatar` varchar(255) DEFAULT NULL,
                         `created_at` datetime(6) DEFAULT NULL,
                         `dob` datetime(6) DEFAULT NULL,
                         `email` varchar(255) NOT NULL,
                         `full_name` varchar(50) DEFAULT NULL,
                         `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
                         `is_deleted` bit(1) DEFAULT NULL,
                         `last_login_at` datetime(6) DEFAULT NULL,
                         `membership_level` enum('NORMAL','ONE_MONTH','ONE_YEAR','SIX_MONTHS') DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `phone` varchar(255) DEFAULT NULL,
                         `provider` enum('FACEBOOK','GITHUB','GOOGLE','LOCAL') DEFAULT NULL,
                         `status` enum('ACTIVE','BANNED','INACTIVE') NOT NULL,
                         `updated_at` datetime(6) DEFAULT NULL,
                         `role_id` int DEFAULT NULL,
                         PRIMARY KEY (`user_id`),
                         UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
                         UNIQUE KEY `UKdu5v5sr43g5bfnji4vb8hg5s3` (`phone`),
                         KEY `email_index` (`email`),
                         KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
                         CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
                            `subject_id` int NOT NULL AUTO_INCREMENT,
                            `created_at` datetime(6) DEFAULT NULL,
                            `description` varchar(255) DEFAULT NULL,
                            `is_deleted` bit(1) DEFAULT NULL,
                            `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECTED') DEFAULT NULL,
                            `subject_code` varchar(255) NOT NULL,
                            `subject_name` varchar(50) NOT NULL,
                            `thumbnail_url` varchar(255) DEFAULT NULL,
                            `updated_at` datetime(6) DEFAULT NULL,
                            PRIMARY KEY (`subject_id`),
                            UNIQUE KEY `UKqt734ivq9gq4yo4p1j1lhhk8l` (`subject_code`),
                            KEY `idx_subject_id` (`subject_id`),
                            KEY `idx_subject_name` (`subject_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `lessons`
--

CREATE TABLE `lessons` (
                           `lesson_id` int NOT NULL AUTO_INCREMENT,
                           `created_at` datetime(6) DEFAULT NULL,
                           `lesson_description` varchar(255) DEFAULT NULL,
                           `is_deleted` bit(1) DEFAULT NULL,
                           `lesson_name` varchar(255) NOT NULL,
                           `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECTED') DEFAULT NULL,
                           `thumbnail_url` varchar(255) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `video_url` varchar(255) DEFAULT NULL,
                           `subject_id` int NOT NULL,
                           PRIMARY KEY (`lesson_id`),
                           UNIQUE KEY `UK5tj43gyo7t4vetks0xnxtbht` (`lesson_name`),
                           KEY `idx_lesson_id` (`lesson_id`),
                           KEY `idx_lesson_name` (`lesson_name`),
                           KEY `idx_subject_id` (`subject_id`),
                           CONSTRAINT `FKe94a0k21xpi7glv89af90lwjv` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `lesson_exercises`
--

CREATE TABLE `lesson_exercises` (
                                    `exercise_id` int NOT NULL AUTO_INCREMENT,
                                    `created_at` datetime(6) DEFAULT NULL,
                                    `duration` bigint DEFAULT NULL,
                                    `title` varchar(255) DEFAULT NULL,
                                    `lesson_id` int NOT NULL,
                                    PRIMARY KEY (`exercise_id`),
                                    KEY `FKesm99q66lwj2786s2jry21twa` (`lesson_id`),
                                    CONSTRAINT `FKesm99q66lwj2786s2jry21twa` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`lesson_id`),
                                    CONSTRAINT `lesson_exercises_chk_1` CHECK ((`duration` >= 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `contents`
--

CREATE TABLE `contents` (
                            `content_id` bigint NOT NULL AUTO_INCREMENT,
                            `content_type` enum('Listening','Reading','Speaking') NOT NULL,
                            PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `content_listenings`
--
CREATE TABLE `content_listenings` (
                                      `content_listening_id` bigint NOT NULL AUTO_INCREMENT,
                                      `audio_file` varchar(255) DEFAULT NULL,
                                      `category` enum('ANNOUNCEMENT','CONVERSATION','DEBATE','DISCUSSION','INSTRUCTION','INTERVIEW','NEWS','PODCAST','REPORT','STORY') NOT NULL,
                                      `created_at` datetime(6) DEFAULT NULL,
                                      `is_deleted` bit(1) DEFAULT NULL,
                                      `image` varchar(255) DEFAULT NULL,
                                      `jlpt_level` enum('N1','N2','N3','N4','N5') NOT NULL,
                                      `script_jp` varchar(255) DEFAULT NULL,
                                      `script_vn` varchar(255) DEFAULT NULL,
                                      `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECT') DEFAULT NULL,
                                      `title` varchar(255) NOT NULL,
                                      `updated_at` datetime(6) DEFAULT NULL,
                                      `content_id` bigint DEFAULT NULL,
                                      PRIMARY KEY (`content_listening_id`),
                                      UNIQUE KEY `UKidqpfbvw23r597l8v5clj0ygi` (`content_id`),
                                      CONSTRAINT `FK2hmvdfp82n1jxitpn2cj5aou` FOREIGN KEY (`content_id`) REFERENCES `contents` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `exercise_questions`
--

CREATE TABLE `exercise_questions` (
                                      `exercise_question_id` int NOT NULL AUTO_INCREMENT,
                                      `created_at` datetime(6) DEFAULT NULL,
                                      `question_text` text NOT NULL,
                                      `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECT') DEFAULT NULL,
                                      `updated_at` datetime(6) DEFAULT NULL,
                                      `content_id` bigint DEFAULT NULL,
                                      `exercise_id` int DEFAULT NULL,
                                      PRIMARY KEY (`exercise_question_id`),
                                      KEY `FKmv518yky12rhradlqsx38aeso` (`content_id`),
                                      KEY `FK3bjbtgj58ausl3jlrf7rl5bn7` (`exercise_id`),
                                      CONSTRAINT `FK3bjbtgj58ausl3jlrf7rl5bn7` FOREIGN KEY (`exercise_id`) REFERENCES `lesson_exercises` (`exercise_id`),
                                      CONSTRAINT `FKmv518yky12rhradlqsx38aeso` FOREIGN KEY (`content_id`) REFERENCES `content_listenings` (`content_listening_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `answer_questions`
--

CREATE TABLE `answer_questions` (
                                    `answer_id` int NOT NULL AUTO_INCREMENT,
                                    `answer_text` varchar(255) DEFAULT NULL,
                                    `created_at` datetime(6) DEFAULT NULL,
                                    `is_correct` bit(1) DEFAULT NULL,
                                    `updated_at` datetime(6) DEFAULT NULL,
                                    `exercise_question_id` int DEFAULT NULL,
                                    PRIMARY KEY (`answer_id`),
                                    KEY `FKhdscwokbunj8adfv39hlrv2t` (`exercise_question_id`),
                                    CONSTRAINT `FKhdscwokbunj8adfv39hlrv2t` FOREIGN KEY (`exercise_question_id`) REFERENCES `exercise_questions` (`exercise_question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `content_progress`
--

CREATE TABLE `content_progress` (
                                    `progress_id` int NOT NULL AUTO_INCREMENT,
                                    `correct_answers` int DEFAULT NULL,
                                    `created_at` datetime(6) DEFAULT NULL,
                                    `progress_status` enum('COMPLETED','IN_PROGRESS','NOT_STARTED') DEFAULT NULL,
                                    `total_questions` int DEFAULT NULL,
                                    `version` int DEFAULT NULL,
                                    `content_id` bigint DEFAULT NULL,
                                    `user_id` bigint DEFAULT NULL,
                                    PRIMARY KEY (`progress_id`),
                                    KEY `FKmsrwqymu4mss7eqjk1j07k173` (`content_id`),
                                    KEY `FKih3l7e145bytat3e5d0wylpf2` (`user_id`),
                                    CONSTRAINT `FKih3l7e145bytat3e5d0wylpf2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                    CONSTRAINT `FKmsrwqymu4mss7eqjk1j07k173` FOREIGN KEY (`content_id`) REFERENCES `contents` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `grammars`
--

CREATE TABLE `grammars` (
                            `grammar_id` int NOT NULL AUTO_INCREMENT,
                            `created_at` datetime(6) DEFAULT NULL,
                            `is_deleted` bit(1) DEFAULT NULL,
                            `example` varchar(500) DEFAULT NULL,
                            `jlpt_level` enum('N1','N2','N3','N4','N5') NOT NULL,
                            `meaning` varchar(200) NOT NULL,
                            `structure` varchar(100) NOT NULL,
                            `title_jp` varchar(100) NOT NULL,
                            `updated_at` datetime(6) DEFAULT NULL,
                            `usage_description` varchar(500) DEFAULT NULL,
                            PRIMARY KEY (`grammar_id`),
                            UNIQUE KEY `UKav751iqyhshackppgwmsk4yd0` (`title_jp`),
                            KEY `title_jp_index` (`title_jp`),
                            KEY `jlpt_level_index` (`jlpt_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `vocabularies`
--

CREATE TABLE `vocabularies` (
                                `vocabulary_id` int NOT NULL AUTO_INCREMENT,
                                `created_at` datetime(6) DEFAULT NULL,
                                `is_deleted` bit(1) DEFAULT NULL,
                                `description` varchar(500) DEFAULT NULL,
                                `example` varchar(500) DEFAULT NULL,
                                `jlpt_level` enum('N1','N2','N3','N4','N5') NOT NULL,
                                `kana` varchar(50) NOT NULL,
                                `kanji` varchar(50) NOT NULL,
                                `meaning` varchar(100) NOT NULL,
                                `part_of_speech` enum('ADVERB','CONJUNCTION','INTERJECTION','I_ADJECTIVE','NA_ADJECTIVE','NOUN','PARTICLE','PRONOUN','VERB') NOT NULL,
                                `romaji` varchar(50) NOT NULL,
                                `updated_at` datetime(6) DEFAULT NULL,
                                PRIMARY KEY (`vocabulary_id`),
                                KEY `kanji_index` (`kanji`),
                                KEY `jlpt_level_index` (`jlpt_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `content_readings`
--

CREATE TABLE `content_readings` (
                                    `content_reading_id` bigint NOT NULL AUTO_INCREMENT,
                                    `audio_file` varchar(255) DEFAULT NULL,
                                    `category` enum('BUSINESS','EDUCATION','ENTERTAINMENT','ENVIRONMENT','HEALTH','POLITICS','SCIENCE','SPORTS','TECHNOLOGY','TRAVEL') NOT NULL,
                                    `created_at` datetime(6) DEFAULT NULL,
                                    `is_deleted` bit(1) DEFAULT NULL,
                                    `image` varchar(255) DEFAULT NULL,
                                    `jlpt_level` enum('N1','N2','N3','N4','N5') NOT NULL,
                                    `script_jp` varchar(255) DEFAULT NULL,
                                    `script_vn` varchar(255) DEFAULT NULL,
                                    `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECT') DEFAULT NULL,
                                    `time_new` datetime(6) NOT NULL,
                                    `title` varchar(255) NOT NULL,
                                    `updated_at` datetime(6) DEFAULT NULL,
                                    `content_id` bigint DEFAULT NULL,
                                    PRIMARY KEY (`content_reading_id`),
                                    UNIQUE KEY `UKsctgnjsy31vvrit8axna24yxw` (`content_id`),
                                    CONSTRAINT `FK8vcatwg2vcja8x2r9odvskgc4` FOREIGN KEY (`content_id`) REFERENCES `contents` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `content_reading_grammar`
--

CREATE TABLE `content_reading_grammar` (
                                           `content_reading_id` bigint NOT NULL,
                                           `grammar_id` int NOT NULL,
                                           KEY `FK8vk8hlsku3oehytdtruqifgpt` (`grammar_id`),
                                           KEY `FKriq8hegums0y74c8fnwlh4qlw` (`content_reading_id`),
                                           CONSTRAINT `FK8vk8hlsku3oehytdtruqifgpt` FOREIGN KEY (`grammar_id`) REFERENCES `grammars` (`grammar_id`),
                                           CONSTRAINT `FKriq8hegums0y74c8fnwlh4qlw` FOREIGN KEY (`content_reading_id`) REFERENCES `content_readings` (`content_reading_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `content_reading_vocabulary`
--

CREATE TABLE `content_reading_vocabulary` (
                                              `content_reading_id` bigint NOT NULL,
                                              `vocabulary_id` int NOT NULL,
                                              KEY `FKix4tckv4a0xe1niuxyfp7ce` (`vocabulary_id`),
                                              KEY `FKa9xo0p1q5ycif82yap6pkragy` (`content_reading_id`),
                                              CONSTRAINT `FKa9xo0p1q5ycif82yap6pkragy` FOREIGN KEY (`content_reading_id`) REFERENCES `content_readings` (`content_reading_id`),
                                              CONSTRAINT `FKix4tckv4a0xe1niuxyfp7ce` FOREIGN KEY (`vocabulary_id`) REFERENCES `vocabularies` (`vocabulary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `content_speakings`
--

CREATE TABLE `content_speakings` (
                                     `content_speaking_id` bigint NOT NULL AUTO_INCREMENT,
                                     `category` enum('ADVICE','GREETINGS','HOSPITAL','INVITE','MESSAGE','PERMISSIONS','PRAISE','PRESENTATION','RESTAURANTS','SHOPPING','SORRY','THANKS','TRAVEL') NOT NULL,
                                     `created_at` datetime(6) DEFAULT NULL,
                                     `is_deleted` bit(1) DEFAULT NULL,
                                     `image` varchar(255) DEFAULT NULL,
                                     `jlpt_level` enum('N1','N2','N3','N4','N5') NOT NULL,
                                     `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECT') DEFAULT NULL,
                                     `title` varchar(255) NOT NULL,
                                     `updated_at` datetime(6) DEFAULT NULL,
                                     `content_id` bigint DEFAULT NULL,
                                     PRIMARY KEY (`content_speaking_id`),
                                     UNIQUE KEY `UKcum9dhbk3o7jwh098iul0y2nj` (`content_id`),
                                     CONSTRAINT `FK1ntobp08lucae2eifo2a2vtbu` FOREIGN KEY (`content_id`) REFERENCES `contents` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `dialogues`
--

CREATE TABLE `dialogues` (
                             `dialogue_id` bigint NOT NULL AUTO_INCREMENT,
                             `answer_jp` varchar(255) DEFAULT NULL,
                             `answer_vn` varchar(255) DEFAULT NULL,
                             `created_at` datetime(6) DEFAULT NULL,
                             `question_jp` varchar(255) DEFAULT NULL,
                             `question_vn` varchar(255) DEFAULT NULL,
                             `status` enum('DRAFT','IN_ACTIVE','PUBLIC','REJECT') DEFAULT NULL,
                             `updated_at` datetime(6) DEFAULT NULL,
                             `content_speaking_id` bigint DEFAULT NULL,
                             PRIMARY KEY (`dialogue_id`),
                             KEY `FKeobapsc1k1msukn4s6yh6vxld` (`content_speaking_id`),
                             CONSTRAINT `FKeobapsc1k1msukn4s6yh6vxld` FOREIGN KEY (`content_speaking_id`) REFERENCES `content_speakings` (`content_speaking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `exercise_results`
--

CREATE TABLE `exercise_results` (
                                    `exercise_result_id` int NOT NULL AUTO_INCREMENT,
                                    `correct_answers` int DEFAULT NULL,
                                    `submission_time` datetime(6) DEFAULT NULL,
                                    `total_questions` int DEFAULT NULL,
                                    `exercise_id` int DEFAULT NULL,
                                    `user_id` bigint DEFAULT NULL,
                                    PRIMARY KEY (`exercise_result_id`),
                                    KEY `FKqai2tbcl3vappgym3qck2bk7j` (`exercise_id`),
                                    KEY `FKasrpyx26t374nb78dknkkgb9i` (`user_id`),
                                    CONSTRAINT `FKasrpyx26t374nb78dknkkgb9i` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                    CONSTRAINT `FKqai2tbcl3vappgym3qck2bk7j` FOREIGN KEY (`exercise_id`) REFERENCES `lesson_exercises` (`exercise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `favorite_list`
--

CREATE TABLE `favorite_list` (
                                 `favorite_list_id` int NOT NULL AUTO_INCREMENT,
                                 `created_at` datetime(6) DEFAULT NULL,
                                 `favorite_list_name` varchar(255) DEFAULT NULL,
                                 `is_deleted` bit(1) DEFAULT NULL,
                                 `is_public` bit(1) DEFAULT NULL,
                                 `user_id` bigint DEFAULT NULL,
                                 PRIMARY KEY (`favorite_list_id`),
                                 KEY `FKm1v0yditst9s13vjc86nht6b2` (`user_id`),
                                 CONSTRAINT `FKm1v0yditst9s13vjc86nht6b2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `favorite_list_grammar`
--

CREATE TABLE `favorite_list_grammar` (
                                         `favorite_list_id` int NOT NULL,
                                         `grammar_id` int NOT NULL,
                                         `last_reviewed_at` datetime(6) DEFAULT NULL,
                                         `status` enum('IN_PROGRESS','MASTERED','NOT_LEARNED') NOT NULL,
                                         PRIMARY KEY (`favorite_list_id`,`grammar_id`),
                                         KEY `FK9p54g4pinjs1uacamawb0up0h` (`grammar_id`),
                                         CONSTRAINT `FK71heeoyqwhnmeekqhnsehv0xw` FOREIGN KEY (`favorite_list_id`) REFERENCES `favorite_list` (`favorite_list_id`),
                                         CONSTRAINT `FK9p54g4pinjs1uacamawb0up0h` FOREIGN KEY (`grammar_id`) REFERENCES `grammars` (`grammar_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `favorite_list_vocabulary`
--

CREATE TABLE `favorite_list_vocabulary` (
                                            `favorite_list_id` int NOT NULL,
                                            `vocabulary_id` int NOT NULL,
                                            `last_reviewed_at` datetime(6) DEFAULT NULL,
                                            `status` enum('IN_PROGRESS','MASTERED','NOT_LEARNED') NOT NULL,
                                            PRIMARY KEY (`favorite_list_id`,`vocabulary_id`),
                                            KEY `FKovjlvjw6mh4ciov2mdf6ci2co` (`vocabulary_id`),
                                            CONSTRAINT `FKdeidddbe7r70blb74o3fbphmk` FOREIGN KEY (`favorite_list_id`) REFERENCES `favorite_list` (`favorite_list_id`),
                                            CONSTRAINT `FKovjlvjw6mh4ciov2mdf6ci2co` FOREIGN KEY (`vocabulary_id`) REFERENCES `vocabularies` (`vocabulary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `favorite_vocabulary`
--

CREATE TABLE `favorite_vocabulary` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `added_at` datetime(6) DEFAULT NULL,
                                       `is_deleted` bit(1) DEFAULT NULL,
                                       `is_public` bit(1) DEFAULT NULL,
                                       `name` varchar(255) DEFAULT NULL,
                                       `owner_name` varchar(255) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `feedbacks`
--


CREATE TABLE `feedbacks` (
                             `feedback_id` int NOT NULL AUTO_INCREMENT,
                             `content` varchar(255) DEFAULT NULL,
                             `created_at` datetime(6) DEFAULT NULL,
                             `rating` int DEFAULT NULL,
                             `user_id` bigint DEFAULT NULL,
                             PRIMARY KEY (`feedback_id`),
                             KEY `FK312drfl5lquu37mu4trk8jkwx` (`user_id`),
                             CONSTRAINT `FK312drfl5lquu37mu4trk8jkwx` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `grammar_progress`
--

CREATE TABLE `grammar_progress` (
                                    `progress_id` int NOT NULL AUTO_INCREMENT,
                                    `progress_status` enum('COMPLETED','IN_PROGRESS','NOT_STARTED') DEFAULT NULL,
                                    `reviewed_at` datetime(6) DEFAULT NULL,
                                    `grammar_id` int DEFAULT NULL,
                                    `user_id` bigint DEFAULT NULL,
                                    PRIMARY KEY (`progress_id`),
                                    KEY `FKj6u4t7kmnnmsl4mfeavt8rg38` (`grammar_id`),
                                    KEY `FKolu2mikk0ky2vhhtd3x75f411` (`user_id`),
                                    CONSTRAINT `FKj6u4t7kmnnmsl4mfeavt8rg38` FOREIGN KEY (`grammar_id`) REFERENCES `grammars` (`grammar_id`),
                                    CONSTRAINT `FKolu2mikk0ky2vhhtd3x75f411` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `lesson_grammar`
--

CREATE TABLE `lesson_grammar` (
                                  `lesson_id` int NOT NULL,
                                  `grammar_id` int NOT NULL,
                                  KEY `FKfbfprewh8chh4mnqe5q7uvkcq` (`grammar_id`),
                                  KEY `FKmvp8sffxc2xb8x4w3mtttshes` (`lesson_id`),
                                  CONSTRAINT `FKfbfprewh8chh4mnqe5q7uvkcq` FOREIGN KEY (`grammar_id`) REFERENCES `grammars` (`grammar_id`),
                                  CONSTRAINT `FKmvp8sffxc2xb8x4w3mtttshes` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `lesson_vocabulary`
--

CREATE TABLE `lesson_vocabulary` (
                                     `lesson_id` int NOT NULL,
                                     `vocabulary_id` int NOT NULL,
                                     KEY `FKqt9y3arrqw46fppge59aaia80` (`vocabulary_id`),
                                     KEY `FKgod1sfwnpnacj1ob2501vdf3q` (`lesson_id`),
                                     CONSTRAINT `FKgod1sfwnpnacj1ob2501vdf3q` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`lesson_id`),
                                     CONSTRAINT `FKqt9y3arrqw46fppge59aaia80` FOREIGN KEY (`vocabulary_id`) REFERENCES `vocabularies` (`vocabulary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `membership_level`
--

CREATE TABLE `membership_level` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `duration` int DEFAULT NULL,
                                    `level_name` enum('NORMAL','ONE_MONTH','ONE_YEAR','SIX_MONTHS') NOT NULL,
                                    `price` double NOT NULL,
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `UKm0yr6q07k9a5ql0nosa97lvlp` (`level_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `membership_level_of_user`
--

CREATE TABLE `membership_level_of_user` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `end_date` date NOT NULL,
                                            `start_date` date NOT NULL,
                                            `membership_level_id` bigint NOT NULL,
                                            `user_id` bigint NOT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `UKsacvrd5dchs0go84l3ckfgc2i` (`user_id`),
                                            KEY `FKb38bgm7h8y42nwpbbd8hjgf02` (`membership_level_id`),
                                            CONSTRAINT `FKb38bgm7h8y42nwpbbd8hjgf02` FOREIGN KEY (`membership_level_id`) REFERENCES `membership_level` (`id`) ON DELETE CASCADE,
                                            CONSTRAINT `FKh34lnys56v4xucxd0axmeqaei` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
                                 `notification_id` int NOT NULL AUTO_INCREMENT,
                                 `content` varchar(255) NOT NULL,
                                 `created_at` datetime(6) DEFAULT NULL,
                                 `status` bit(1) DEFAULT NULL,
                                 `status_send` bit(1) DEFAULT NULL,
                                 `title` varchar(255) NOT NULL,
                                 `type` enum('ACCEPT_STUDENT','NORMAL','PAYMENT') DEFAULT NULL,
                                 `user_id` bigint DEFAULT NULL,
                                 `user_send_id` bigint DEFAULT NULL,
                                 PRIMARY KEY (`notification_id`),
                                 KEY `FK9y21adhxn0ayjhfocscqox7bh` (`user_id`),
                                 KEY `FKjkbj22hdmpctlavsajw9tlu2x` (`user_send_id`),
                                 CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                 CONSTRAINT `FKjkbj22hdmpctlavsajw9tlu2x` FOREIGN KEY (`user_send_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `parent_students`
--

CREATE TABLE `parent_students` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `created_at` datetime(6) DEFAULT NULL,
                                   `status` enum('CONFIRM','PENDING','REJECT') DEFAULT NULL,
                                   `verification_code` varchar(255) DEFAULT NULL,
                                   `parent_id` bigint NOT NULL,
                                   `student_id` bigint DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FKmgxwo2s6a5vbuk8w5agevhcu7` (`parent_id`),
                                   KEY `FKt1ggisile454tr4at45rsmp4d` (`student_id`),
                                   CONSTRAINT `FKmgxwo2s6a5vbuk8w5agevhcu7` FOREIGN KEY (`parent_id`) REFERENCES `users` (`user_id`),
                                   CONSTRAINT `FKt1ggisile454tr4at45rsmp4d` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `amount` bigint NOT NULL,
                           `bank_code` varchar(255) DEFAULT NULL,
                           `order_id` varchar(255) NOT NULL,
                           `paid_at` datetime(6) DEFAULT NULL,
                           `status` enum('FAILED','PENDING','SUCCESS') NOT NULL,
                           `membership_level_id` bigint DEFAULT NULL,
                           `user_id` bigint NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKq88dqipjqvury0vxl6x729rgj` (`membership_level_id`),
                           KEY `FKmi2669nkjesvp7cd257fptl6f` (`user_id`),
                           CONSTRAINT `FKmi2669nkjesvp7cd257fptl6f` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                           CONSTRAINT `FKq88dqipjqvury0vxl6x729rgj` FOREIGN KEY (`membership_level_id`) REFERENCES `membership_level` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `progress_lesson`
--


CREATE TABLE `progress_lesson` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `end_date` datetime(6) DEFAULT NULL,
                                   `start_date` datetime(6) DEFAULT NULL,
                                   `status` enum('COMPLETED','IN_PROGRESS','NOT_STARTED') DEFAULT NULL,
                                   `viewed_at` datetime(6) DEFAULT NULL,
                                   `lesson_id` int NOT NULL,
                                   `user_id` bigint NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FKokiede3bxfp6ixauv0nokrwlq` (`lesson_id`),
                                   KEY `FKlsq9dqd6c5vt5n18gm1x8s0fj` (`user_id`),
                                   CONSTRAINT `FKlsq9dqd6c5vt5n18gm1x8s0fj` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                   CONSTRAINT `FKokiede3bxfp6ixauv0nokrwlq` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `progress_subjects`
--

CREATE TABLE `progress_subjects` (
                                     `progress_id` int NOT NULL AUTO_INCREMENT,
                                     `end_date` datetime(6) DEFAULT NULL,
                                     `progress_status` enum('COMPLETED','IN_PROGRESS','NOT_STARTED') DEFAULT NULL,
                                     `start_date` datetime(6) DEFAULT NULL,
                                     `viewed_at` datetime(6) DEFAULT NULL,
                                     `subject_id` int DEFAULT NULL,
                                     `user_id` bigint DEFAULT NULL,
                                     PRIMARY KEY (`progress_id`),
                                     KEY `FKmmwxijbueprjl766lq1aulauh` (`subject_id`),
                                     KEY `FKs0prvwkduqqdoosenr9f01mix` (`user_id`),
                                     CONSTRAINT `FKmmwxijbueprjl766lq1aulauh` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`),
                                     CONSTRAINT `FKs0prvwkduqqdoosenr9f01mix` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `quiz_question`
--


CREATE TABLE `quiz_question` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `question` varchar(255) DEFAULT NULL,
                                 `grammar_id` int DEFAULT NULL,
                                 `vocabulary_id` int DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `UKpap0ve7k4ftxlprrljf7at21m` (`grammar_id`),
                                 UNIQUE KEY `UKsh0m1w22u8a3eow29scpiac9s` (`vocabulary_id`),
                                 CONSTRAINT `FKddyw44gp1lkfsw76xehjqhx0e` FOREIGN KEY (`vocabulary_id`) REFERENCES `vocabularies` (`vocabulary_id`),
                                 CONSTRAINT `FKp3kanpe8ajtivk89fof6noisu` FOREIGN KEY (`grammar_id`) REFERENCES `grammars` (`grammar_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `study_reminder`
--

CREATE TABLE `study_reminder` (
                                  `study_reminder_id` int NOT NULL AUTO_INCREMENT,
                                  `created_at` datetime(6) DEFAULT NULL,
                                  `days_of_week` varchar(255) DEFAULT NULL,
                                  `is_active` bit(1) DEFAULT NULL,
                                  `note` varchar(255) NOT NULL,
                                  `time` time(6) DEFAULT NULL,
                                  `updated_at` datetime(6) DEFAULT NULL,
                                  `user_id` bigint DEFAULT NULL,
                                  PRIMARY KEY (`study_reminder_id`),
                                  KEY `FKa2xanpksnfsv8dr1qvvlcbw9p` (`user_id`),
                                  CONSTRAINT `FKa2xanpksnfsv8dr1qvvlcbw9p` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `system_logs`
--

CREATE TABLE `system_logs` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `action` varchar(255) DEFAULT NULL,
                               `details` longtext,
                               `role` enum('CONTENT_MANAGER','PARENT','STAFF','SUPER_ADMIN','USER','USER_MANAGER') DEFAULT NULL,
                               `timestamp` datetime(6) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `user_favorite_vocabulary`
--


CREATE TABLE `user_favorite_vocabulary` (
                                            `user_id` bigint NOT NULL,
                                            `favorite_vocabulary_id` int NOT NULL,
                                            PRIMARY KEY (`user_id`,`favorite_vocabulary_id`),
                                            KEY `FKah8dsg7ilmdj12wixjfkwf9l4` (`favorite_vocabulary_id`),
                                            CONSTRAINT `FK8pbh9mk1qy3krtorrvq9ujwfa` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                            CONSTRAINT `FKah8dsg7ilmdj12wixjfkwf9l4` FOREIGN KEY (`favorite_vocabulary_id`) REFERENCES `favorite_vocabulary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `user_response_questions`
--

CREATE TABLE `user_response_questions` (
                                           `user_response_question_id` int NOT NULL AUTO_INCREMENT,
                                           `created_at` datetime(6) DEFAULT NULL,
                                           `is_correct` bit(1) DEFAULT NULL,
                                           `answer_question_id` int DEFAULT NULL,
                                           `exercise_question_id` int DEFAULT NULL,
                                           `user_id` bigint DEFAULT NULL,
                                           PRIMARY KEY (`user_response_question_id`),
                                           UNIQUE KEY `UKa5l97dsjc72a9elg3qg2g6lyo` (`answer_question_id`),
                                           UNIQUE KEY `UKkxb8cyobgm5fd9gx1y33tclx1` (`exercise_question_id`),
                                           KEY `FKip1laamvr4yjd3p1llttvbn9k` (`user_id`),
                                           CONSTRAINT `FKh9h6jd5svm4on19weg8uvebgr` FOREIGN KEY (`exercise_question_id`) REFERENCES `exercise_questions` (`exercise_question_id`),
                                           CONSTRAINT `FKip1laamvr4yjd3p1llttvbn9k` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                           CONSTRAINT `FKpmhrgkw94f82jpuoxo3d3guin` FOREIGN KEY (`answer_question_id`) REFERENCES `answer_questions` (`answer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `vocabulary_favorite_vocabulary`
--

CREATE TABLE `vocabulary_favorite_vocabulary` (
                                                  `id` int NOT NULL,
                                                  `vocabulary_id` int NOT NULL,
                                                  PRIMARY KEY (`id`,`vocabulary_id`),
                                                  KEY `FKlfs8w9n6gjto3fnq928pp0gp8` (`vocabulary_id`),
                                                  CONSTRAINT `FKe42b1vmwnin1gai1qw61k0nni` FOREIGN KEY (`id`) REFERENCES `favorite_vocabulary` (`id`),
                                                  CONSTRAINT `FKlfs8w9n6gjto3fnq928pp0gp8` FOREIGN KEY (`vocabulary_id`) REFERENCES `vocabularies` (`vocabulary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `vocabulary_progress`
--

CREATE TABLE `vocabulary_progress` (
                                       `progress_id` int NOT NULL AUTO_INCREMENT,
                                       `progress_status` enum('COMPLETED','IN_PROGRESS','NOT_STARTED') DEFAULT NULL,
                                       `reviewed_at` datetime(6) DEFAULT NULL,
                                       `user_id` bigint DEFAULT NULL,
                                       `vocabulary_id` int DEFAULT NULL,
                                       PRIMARY KEY (`progress_id`),
                                       KEY `FKbolrs97l78y1xw2emqrr8kh3w` (`user_id`),
                                       KEY `FK1ssmjuafq0vhc0chtuipw1ijb` (`vocabulary_id`),
                                       CONSTRAINT `FK1ssmjuafq0vhc0chtuipw1ijb` FOREIGN KEY (`vocabulary_id`) REFERENCES `vocabularies` (`vocabulary_id`),
                                       CONSTRAINT `FKbolrs97l78y1xw2emqrr8kh3w` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- import data
INSERT INTO `fu-ohayo`.`grammars` (`is_deleted`, `example`, `jlpt_level`, `meaning`, `structure`, `title_jp`, `usage_description`) VALUES
                                                                                                                                       (0, '私は学生です。', 'N5', 'Là ~', 'A は B です', '〜です', 'Dùng để khẳng định ai đó là gì, cái gì'),
                                                                                                                                       (0, 'これは本ですか。', 'N5', 'Phải không?', 'A は B ですか', '〜ですか', 'Dùng để hỏi xác nhận một sự vật/sự việc'),
                                                                                                                                       (0, 'いいえ、違います。', 'N5', 'Không phải là ~', 'いいえ、違います', '違います', 'Câu trả lời phủ định cho câu hỏi ですか'),
                                                                                                                                       (0, '日本語を勉強します。', 'N5', 'Làm ~', 'A を V ます', '〜を〜ます', 'Cấu trúc cơ bản động từ với tân ngữ'),
                                                                                                                                       (0, '毎日テレビを見ます。', 'N5', 'Làm gì mỗi ngày', '毎日 + V ます', '毎日〜ます', 'Dùng để diễn tả thói quen'),
                                                                                                                                       (0, '水を飲みません。', 'N5', 'Không làm ~', 'A を V ません', '〜を〜ません', 'Dạng phủ định của động từ'),
                                                                                                                                       (0, '一緒に行きましょう。', 'N5', 'Hãy cùng ~', 'V ましょう', '〜ましょう', 'Dùng để rủ rê người khác'),
                                                                                                                                       (0, '図書館で勉強します。', 'N5', 'Làm gì ở đâu', 'Địa điểm + で + V', '〜で〜ます', 'Nói về hành động diễn ra tại đâu'),
                                                                                                                                       (0, '８時に起きます。', 'N5', 'Làm gì lúc mấy giờ', 'Thời gian + に + V', '〜に〜ます', 'Nói thời điểm hành động diễn ra'),
                                                                                                                                       (0, '田中さんは親切です。', 'N5', 'Tính cách, đặc điểm', 'A は Tính từ đuôi な です', '〜は〜です', 'Nói về tính cách người/vật'),

                                                                                                                                       (0, 'この本は面白いです。', 'N5', 'Đặc điểm bằng tính từ い', 'A は Adj い です', '〜は Adj い です', 'Miêu tả tính chất'),
                                                                                                                                       (0, 'それは何ですか。', 'N5', 'Cái đó là gì?', 'それ は 何 ですか', '〜は何ですか', 'Hỏi về đồ vật, thông tin'),
                                                                                                                                       (0, '本を読んで、寝ます。', 'N4', 'Làm A rồi làm B', 'V て、V', '〜て〜', 'Nối các hành động liên tiếp'),
                                                                                                                                       (0, '雨が降っています。', 'N4', 'Đang làm gì', 'V て います', '〜ています', 'Miêu tả hành động đang diễn ra'),
                                                                                                                                       (0, 'これは私が使う辞書です。', 'N3', 'Mệnh đề bổ nghĩa danh từ', 'V 普通形 + 名詞', '〜〜名詞', 'Dùng để bổ nghĩa cho danh từ'),
                                                                                                                                       (0, '東京に行ったことがあります。', 'N4', 'Đã từng ~', 'V た + ことがある', '〜たことがある', 'Dùng khi nói về kinh nghiệm'),
                                                                                                                                       (0, '行かなければなりません。', 'N4', 'Phải làm gì', 'V なければなりません', '〜なければなりません', 'Diễn tả nghĩa vụ'),
                                                                                                                                       (0, '行かなくてもいいです。', 'N4', 'Không cần làm', 'V なくてもいい', '〜なくてもいい', 'Dùng để nói không cần thiết'),
                                                                                                                                       (0, '勉強したり、本を読んだりします。', 'N3', 'Làm A, làm B, v.v.', 'V たり、V たり する', '〜たり〜たりする', 'Liệt kê không đầy đủ'),
                                                                                                                                       (0, '走るのが好きです。', 'N4', 'Thích làm gì', 'Vる + の + が + 好き', '〜のが好き', 'Dùng để nói sở thích'),

                                                                                                                                       (0, 'この問題は簡単すぎます。', 'N3', 'Quá mức', 'Adj/V + すぎる', '〜すぎる', 'Chỉ mức độ quá mức'),
                                                                                                                                       (0, '漢字が読めます。', 'N4', 'Có thể làm gì', 'V khả năng（可能形）', '〜られる', 'Khả năng thực hiện hành động'),
                                                                                                                                       (0, '走らないでください。', 'N4', 'Xin đừng làm', 'V ないで + ください', '〜ないでください', 'Yêu cầu không làm gì'),
                                                                                                                                       (0, '寝る前に、歯を磨きます。', 'N3', 'Trước khi làm gì', 'Vる + 前に', '〜前に', 'Nói về hành động xảy ra trước'),
                                                                                                                                       (0, '食べたあとで、薬を飲みます。', 'N3', 'Sau khi làm gì', 'Vた + あとで', '〜たあとで', 'Nói về hành động xảy ra sau'),
                                                                                                                                       (0, '寒くなりました。', 'N4', 'Trở nên ~', 'Adj く/に + なります', '〜く/になります', 'Diễn tả sự thay đổi trạng thái'),
                                                                                                                                       (0, '早く寝たほうがいいです。', 'N3', 'Nên làm gì', 'Vた + ほうがいい', '〜たほうがいい', 'Đưa ra lời khuyên'),
                                                                                                                                       (0, 'この部屋は静かそうです。', 'N3', 'Có vẻ ~', 'Adj そう', '〜そうです', 'Phỏng đoán dựa trên vẻ bề ngoài'),
                                                                                                                                       (0, '先生によって書かれました。', 'N2', 'Bị động bởi ai đó', 'N によって V られる', '〜によって〜られる', 'Câu bị động bởi chủ thể'),
                                                                                                                                       (0, '学生に読まれました。', 'N2', 'Bị động', 'N に + Vれる', '〜に〜れる', 'Hành động bị ai đó thực hiện'),

                                                                                                                                       (0, '友達に本をあげます。', 'N4', 'Cho ai cái gì', 'N に N を あげる', '〜に〜をあげる', 'Cho người khác một vật'),
                                                                                                                                       (0, '先生は私に本をくださいました。', 'N2', 'Người trên cho mình', 'N に N を くださる', '〜くださる', 'Thể kính ngữ của あげる'),
                                                                                                                                       (0, '父にプレゼントをもらいました。', 'N4', 'Nhận từ ai đó', 'N に N を もらう', '〜にもらう', 'Diễn tả việc nhận vật từ người khác'),
                                                                                                                                       (0, '雨が降るかもしれません。', 'N3', 'Có thể là', 'Thể thường + かもしれない', '〜かもしれません', 'Phỏng đoán không chắc chắn'),
                                                                                                                                       (0, '田中さんは来るでしょう。', 'N3', 'Chắc là ~', 'Thể thường + でしょう', '〜でしょう', 'Phỏng đoán có cơ sở'),
                                                                                                                                       (0, '来るなら、電話してください。', 'N3', 'Nếu ~ thì', 'V る / た + なら', '〜なら', 'Câu điều kiện giả định'),
                                                                                                                                       (0, '行くとき、連絡してください。', 'N3', 'Khi làm gì', 'Vる + とき', '〜とき', 'Chỉ thời điểm'),
                                                                                                                                       (0, '日本へ行くために、日本語を勉強します。', 'N3', 'Để làm gì', 'Vる + ために', '〜ために', 'Nói mục đích hành động'),
                                                                                                                                       (0, '行けるように練習します。', 'N3', 'Để có thể làm gì', 'Vる + ように', '〜ように', 'Mục tiêu năng lực'),
                                                                                                                                       (0, '走れるようになりました。', 'N3', 'Đã có thể làm gì', 'V khả năng + ようになる', '〜ようになる', 'Diễn tả sự thay đổi năng lực');

-- vocabualaries
INSERT INTO `fu-ohayo`.`vocabularies` (`is_deleted`, `description`, `example`, `jlpt_level`, `kana`, `kanji`, `meaning`, `part_of_speech`, `romaji`) VALUES
                                                                                                                                                         (0, 'Từ ohayou có nghĩa là chào buổi sáng', 'おはよう を 使って挨拶しました。', 'N5', 'おはよう', 'おはよう', 'chào buổi sáng', 'INTERJECTION', 'ohayou'),
                                                                                                                                                         (0, 'Từ tabemasu có nghĩa là ăn', 'パンを食べます。', 'N5', 'たべます', '食べます', 'ăn', 'VERB', 'tabemasu'),
                                                                                                                                                         (0, 'Từ inu có nghĩa là con chó', '犬が好きです。', 'N5', 'いぬ', '犬', 'con chó', 'NOUN', 'inu'),
                                                                                                                                                         (0, 'Từ yasui có nghĩa là rẻ', 'このりんごは安いです。', 'N5', 'やすい', '安い', 'rẻ', 'I_ADJECTIVE', 'yasui'),
                                                                                                                                                         (0, 'Từ genki có nghĩa là khỏe mạnh', '元気ですか？', 'N5', 'げんき', '元気', 'khỏe mạnh', 'NA_ADJECTIVE', 'genki'),
                                                                                                                                                         (0, 'Từ yukkuri có nghĩa là từ từ', 'ゆっくり話してください。', 'N5', 'ゆっくり', 'ゆっくり', 'từ từ', 'ADVERB', 'yukkuri'),
                                                                                                                                                         (0, 'Từ demo có nghĩa là nhưng', '行きたいですが、でも忙しいです。', 'N5', 'でも', 'でも', 'nhưng', 'CONJUNCTION', 'demo'),
                                                                                                                                                         (0, 'Từ watashi có nghĩa là tôi', '私は学生です。', 'N5', 'わたし', '私', 'tôi', 'PRONOUN', 'watashi'),
                                                                                                                                                         (0, 'Từ wa có nghĩa là chủ đề', 'これはペンです。', 'N5', 'は', 'は', 'chủ đề', 'PARTICLE', 'wa'),
                                                                                                                                                         (0, 'Từ konbanwa có nghĩa là chào buổi tối', 'こんばんは、と言いました。', 'N5', 'こんばんは', 'こんばんは', 'chào buổi tối', 'INTERJECTION', 'konbanwa'),

                                                                                                                                                         (0, 'Từ nomimasu có nghĩa là uống', '水を飲みます。', 'N5', 'のみます', '飲みます', 'uống', 'VERB', 'nomimasu'),
                                                                                                                                                         (0, 'Từ neko có nghĩa là con mèo', '猫が寝ています。', 'N5', 'ねこ', '猫', 'con mèo', 'NOUN', 'neko'),
                                                                                                                                                         (0, 'Từ atsui có nghĩa là nóng', '今日は暑いです。', 'N5', 'あつい', '暑い', 'nóng', 'I_ADJECTIVE', 'atsui'),
                                                                                                                                                         (0, 'Từ shizuka có nghĩa là yên tĩnh', '静かな部屋です。', 'N5', 'しずか', '静か', 'yên tĩnh', 'NA_ADJECTIVE', 'shizuka'),
                                                                                                                                                         (0, 'Từ zutto có nghĩa là suốt', 'ずっと待っていました。', 'N4', 'ずっと', 'ずっと', 'suốt', 'ADVERB', 'zutto'),
                                                                                                                                                         (0, 'Từ soshite có nghĩa là và sau đó', '勉強しました、そして寝ました。', 'N4', 'そして', 'そして', 'và sau đó', 'CONJUNCTION', 'soshite'),
                                                                                                                                                         (0, 'Từ kare có nghĩa là anh ấy', '彼は先生です。', 'N4', 'かれ', '彼', 'anh ấy', 'PRONOUN', 'kare'),
                                                                                                                                                         (0, 'Từ no có nghĩa là của', 'これは私の本です。', 'N5', 'の', 'の', 'của', 'PARTICLE', 'no'),
                                                                                                                                                         (0, 'Từ arigatou có nghĩa là cảm ơn', 'ありがとう、と言いました。', 'N5', 'ありがとう', 'ありがとう', 'cảm ơn', 'INTERJECTION', 'arigatou'),
                                                                                                                                                         (0, 'Từ benkyou có nghĩa là học', '日本語を勉強しています。', 'N4', 'べんきょう', '勉強', 'học', 'NOUN', 'benkyou'),

                                                                                                                                                         (0, 'Từ ikimasu có nghĩa là đi', '学校へ行きます。', 'N5', 'いきます', '行きます', 'đi', 'VERB', 'ikimasu'),
                                                                                                                                                         (0, 'Từ kuruma có nghĩa là ô tô', '車を運転します。', 'N5', 'くるま', '車', 'ô tô', 'NOUN', 'kuruma'),
                                                                                                                                                         (0, 'Từ tanoshii có nghĩa là vui', '楽しい旅行でした。', 'N5', 'たのしい', '楽しい', 'vui', 'I_ADJECTIVE', 'tanoshii'),
                                                                                                                                                         (0, 'Từ kantan có nghĩa là đơn giản', '簡単な問題です。', 'N4', 'かんたん', '簡単', 'đơn giản', 'NA_ADJECTIVE', 'kantan'),
                                                                                                                                                         (0, 'Từ yoku có nghĩa là thường xuyên', 'よくテレビを見ます。', 'N4', 'よく', 'よく', 'thường xuyên', 'ADVERB', 'yoku'),
                                                                                                                                                         (0, 'Từ dakara có nghĩa là vì vậy', '雨です、だから行きません。', 'N4', 'だから', 'だから', 'vì vậy', 'CONJUNCTION', 'dakara'),
                                                                                                                                                         (0, 'Từ kanojo có nghĩa là cô ấy', '彼女は学生です。', 'N4', 'かのじょ', '彼女', 'cô ấy', 'PRONOUN', 'kanojo'),
                                                                                                                                                         (0, 'Từ de có nghĩa là tại, bằng', 'バスで行きます。', 'N5', 'で', 'で', 'tại, bằng', 'PARTICLE', 'de'),
                                                                                                                                                         (0, 'Từ ittekimasu có nghĩa là tôi đi đây', '行ってきます、と言いました。', 'N5', 'いってきます', '行ってきます', 'tôi đi đây', 'INTERJECTION', 'ittekimasu'),
                                                                                                                                                         (0, 'Từ asagohan có nghĩa là bữa sáng', '朝ごはんを食べました。', 'N5', 'あさごはん', '朝ごはん', 'bữa sáng', 'NOUN', 'asagohan'),

                                                                                                                                                         (0, 'Từ kaerimasu có nghĩa là về', 'うちへ帰ります。', 'N5', 'かえります', '帰ります', 'về', 'VERB', 'kaerimasu'),
                                                                                                                                                         (0, 'Từ hon có nghĩa là sách', '本を読みます。', 'N5', 'ほん', '本', 'sách', 'NOUN', 'hon'),
                                                                                                                                                         (0, 'Từ tsumaranai có nghĩa là chán', 'つまらない番組です。', 'N4', 'つまらない', 'つまらない', 'chán', 'I_ADJECTIVE', 'tsumaranai'),
                                                                                                                                                         (0, 'Từ kirei có nghĩa là đẹp, sạch', '綺麗な部屋です。', 'N5', 'きれい', '綺麗', 'đẹp, sạch', 'NA_ADJECTIVE', 'kirei'),
                                                                                                                                                         (0, 'Từ motto có nghĩa là hơn nữa', 'もっと頑張りましょう。', 'N4', 'もっと', 'もっと', 'hơn nữa', 'ADVERB', 'motto'),
                                                                                                                                                         (0, 'Từ sorekara có nghĩa là sau đó', '食べました、それから寝ました。', 'N4', 'それから', 'それから', 'sau đó', 'CONJUNCTION', 'sorekara'),
                                                                                                                                                         (0, 'Từ anatatachi có nghĩa là các bạn', 'あなたたちは先生ですか？', 'N4', 'あなたたち', 'あなたたち', 'các bạn', 'PRONOUN', 'anatatachi'),
                                                                                                                                                         (0, 'Từ to có nghĩa là và, với', '友達と行きます。', 'N5', 'と', 'と', 'và, với', 'PARTICLE', 'to'),
                                                                                                                                                         (0, 'Từ itadakimasu có nghĩa là mời ăn', 'いただきます、と言って食べました。', 'N5', 'いただきます', 'いただきます', 'mời ăn', 'INTERJECTION', 'itadakimasu'),
                                                                                                                                                         (0, 'Từ hirugohan có nghĩa là bữa trưa', '昼ごはんを食べました。', 'N5', 'ひるごはん', '昼ごはん', 'bữa trưa', 'NOUN', 'hirugohan');

-- role
INSERT INTO Roles (name, description) VALUES
                                          ('USER', 'Người dùng thông thường'),
                                          ('PARENT', 'Phụ huynh'),
                                          ('CONTENT_MANAGER', 'Quản lý nội dung'),
                                          ('USER_MANAGER', 'Quản lý người dùng'),
                                          ('SUPER_ADMIN', 'Quản trị viên cấp cao'),
                                          ('STAFF', 'Nhân viên');
-- ADMIN
INSERT INTO admins (username, password) VALUES
('superadmin', '$2a$10$tlxYMRLv8RGohvhwpCmiJei.AqFQpctpYOh0eqTHjegZzb3g2GXnC'),
('staff', '$2a$10$tlxYMRLv8RGohvhwpCmiJei.AqFQpctpYOh0eqTHjegZzb3g2GXnC'),
('contentadmin', '$2a$10$tlxYMRLv8RGohvhwpCmiJei.AqFQpctpYOh0eqTHjegZzb3g2GXnC'),
('useradmin', '$2a$10$tlxYMRLv8RGohvhwpCmiJei.AqFQpctpYOh0eqTHjegZzb3g2GXnC');

INSERT INTO admin_roles(admin_id, role_id) VALUES
                                               (1,5),
                                               (3,3),
                                               (4,4),
                                               (2,6);

INSERT INTO `fu-ohayo`.`exercise_questions`
(`exercise_question_id`, `question_text`, `type`) VALUES
                                                      (31, '「こんにちは」は英語で何と言いますか？', 'contentListening'),
                                                      (32, '東京はどこにありますか？', 'exercise'),
                                                      (33, '「ありがとう」の意味は何ですか？', 'contentListening'),
                                                      (34, '「すみません」はどんな時に使いますか？', 'contentListening'),
                                                      (35, '一日は何時間ですか？', 'exercise'),
                                                      (36, '「おはよう」はいつ使いますか？', 'contentListening'),
                                                      (37, '「さようなら」は何の意味ですか？', 'contentListening'),
                                                      (38, '日曜日は週の何日目ですか？', 'exercise'),
                                                      (39, '電車はどこで乗りますか？', 'exercise'),
                                                      (40, '「いただきます」はいつ言いますか？', 'contentListening'),
                                                      (41, '一週間は何日ですか？', 'exercise'),
                                                      (42, '「こんばんわ」の意味は？', 'contentListening'),
                                                      (43, 'どこで本を借りますか？', 'exercise'),
                                                      (44, '「もしもし」は何の時に使いますか？', 'contentListening'),
                                                      (45, '「がんばって」はどういう意味ですか？', 'contentListening');
INSERT INTO `fu-ohayo`.`answer_questions`
(`answer_text`, `is_correct`, `exercise_question_id`) VALUES

-- 31
('Hello', TRUE, 31),
('Goodbye', FALSE, 31),
('Thank you', FALSE, 31),
('Yes', FALSE, 31),

-- 32
('大阪にあります', FALSE, 32),
('日本にあります', TRUE, 32),
('韓国にあります', FALSE, 32),
('ベトナムにあります', FALSE, 32),

-- 33
('Cảm ơn', TRUE, 33),
('Xin lỗi', FALSE, 33),
('Tạm biệt', FALSE, 33),
('Xin chào', FALSE, 33),

-- 34
('Khi cảm ơn', FALSE, 34),
('Khi xin lỗi hoặc gọi ai đó', TRUE, 34),
('Khi chào tạm biệt', FALSE, 34),
('Khi giới thiệu bản thân', FALSE, 34),

-- 35
('24時間', TRUE, 35),
('12時間', FALSE, 35),
('10時間', FALSE, 35),
('36時間', FALSE, 35),

-- 36
('Buổi sáng', TRUE, 36),
('Buổi tối', FALSE, 36),
('Lúc đi ngủ', FALSE, 36),
('Lúc ăn cơm', FALSE, 36),

-- 37
('Tạm biệt', TRUE, 37),
('Cảm ơn', FALSE, 37),
('Xin lỗi', FALSE, 37),
('Chúc ngủ ngon', FALSE, 37),

-- 38
('Ngày thứ 6', FALSE, 38),
('Ngày thứ 7', FALSE, 38),
('Ngày đầu tuần', TRUE, 38),
('Ngày cuối tháng', FALSE, 38),

-- 39
('Ở ga tàu', TRUE, 39),
('Ở trường học', FALSE, 39),
('Ở công viên', FALSE, 39),
('Ở hiệu sách', FALSE, 39),

-- 40
('Trước khi ăn', TRUE, 40),
('Sau khi ăn', FALSE, 40),
('Khi chào buổi sáng', FALSE, 40),
('Khi ngủ dậy', FALSE, 40),

-- 41
('7日間', TRUE, 41),
('6日間', FALSE, 41),
('5日間', FALSE, 41),
('8日間', FALSE, 41),

-- 42
('Chào buổi tối', TRUE, 42),
('Chúc ngủ ngon', FALSE, 42),
('Chào buổi sáng', FALSE, 42),
('Cảm ơn', FALSE, 42),

-- 43
('Ở thư viện', TRUE, 43),
('Ở nhà ga', FALSE, 43),
('Ở tiệm cà phê', FALSE, 43),
('Ở nhà hàng', FALSE, 43),

-- 44
('Khi gọi điện thoại', TRUE, 44),
('Khi gặp mặt trực tiếp', FALSE, 44),
('Khi ăn cơm', FALSE, 44),
('Khi học bài', FALSE, 44),

-- 45
('Cố lên!', TRUE, 45),
('Xin lỗi', FALSE, 45),
('Chào tạm biệt', FALSE, 45),
('Chúc ngủ ngon', FALSE, 45);

INSERT INTO `fu-ohayo`.`dialogues` (
    `answer_jp`, `answer_vn`, `created_at`, `question_jp`, `question_vn`, `is_deleted`
) VALUES
-- 1
('はい、少し話せます。', 'Vâng, tôi có thể nói một chút.', '2025-07-01 11:00:00', '日本語を話せますか？', 'Bạn có thể nói tiếng Nhật không?', 0),

-- 2
('会社員です。', 'Tôi là nhân viên công ty.', '2025-07-01 11:01:00', 'お仕事は何ですか？', 'Bạn làm nghề gì?', 0),

-- 3
('コーヒーをください。', 'Cho tôi một ly cà phê.', '2025-07-01 11:02:00', 'ご注文は？', 'Bạn muốn gọi món gì?', 0),

-- 4
('はい、元気です。', 'Vâng, tôi khỏe.', '2025-07-01 10:03:00', 'お元気ですか？', 'Bạn khỏe không?', 0),

-- 5
('日本語を勉強しています。', 'Tôi đang học tiếng Nhật.', '2025-07-01 10:04:00', '何を勉強していますか？', 'Bạn đang học gì?', 0),

-- 6
('いいえ、まだ行ったことがありません。', 'Chưa, tôi chưa từng đi.', '2025-07-01 10:05:00', '日本へ行ったことがありますか？', 'Bạn đã từng đến Nhật chưa?', 0),

-- 7
('カフェで友達に会います。', 'Tôi gặp bạn ở quán cà phê.', '2025-07-01 10:06:00', 'どこで友達に会いますか？', 'Bạn gặp bạn ở đâu?', 0),

-- 8
('土曜日に買い物に行きます。', 'Tôi đi mua sắm vào thứ Bảy.', '2025-07-01 10:07:00', 'いつ買い物に行きますか？', 'Khi nào bạn đi mua sắm?', 0),

-- 9
('ご飯を食べました。', 'Tôi đã ăn cơm rồi.', '2025-07-01 10:08:00', 'もうご飯を食べましたか？', 'Bạn đã ăn cơm chưa?', 0),

-- 10
('はい、日本が大好きです。', 'Vâng, tôi rất thích Nhật Bản.', '2025-07-01 10:09:00', '日本が好きですか？', 'Bạn có thích Nhật Bản không?', 0);