//package vn.fu_ohayo.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class PasswordForgotToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//    private String token;
//    private LocalDateTime expiryTime;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//}
