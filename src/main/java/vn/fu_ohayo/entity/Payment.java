package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người dùng thực hiện thanh toán
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Số tiền thanh toán (đơn vị: VNĐ)
    @Column(nullable = false)
    private Long amount;

    // Trạng thái thanh toán (e.g. SUCCESS, FAILED, PENDING)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    // Mã đơn hàng (nếu bạn dùng vnp_TxnRef)
    @Column(name = "order_id", nullable = false)
    private String orderId;

    // Thời gian thanh toán
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // Tên ngân hàng (vnp_BankCode)
    private String bankCode;

    // Mã gói người dùng đã mua (nếu muốn lưu thêm)
    @ManyToOne
    @JoinColumn(name = "membership_level_id")
    private MembershipLevel membershipLevel;
    @PrePersist
    public void prePersist() {
        if (this.paidAt == null) {
            this.paidAt = LocalDateTime.now();
        }
    }
}
