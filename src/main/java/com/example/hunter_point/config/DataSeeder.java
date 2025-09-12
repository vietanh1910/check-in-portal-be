package com.example.hunter_point.config;

import com.example.hunter_point.entity.Role;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.Voucher;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.entity.enums.VoucherStatus;
import com.example.hunter_point.repository.RoleRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays; // <-- Quan trọng: Thêm import này

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // --- SEED ROLES ---
        if (roleRepository.count() == 0) {
            System.out.println("Seeding roles...");
            roleRepository.save(new Role(ERole.ADMIN));
            roleRepository.save(new Role(ERole.USER));
            roleRepository.save(new Role(ERole.ALLOCATOR));
            System.out.println("Roles seeded successfully.");
        }

        // --- SEED USERS ---
        if (userRepository.count() == 0) {
            System.out.println("Seeding users...");
            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(ERole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();
            userRepository.save(admin);

            User user = User.builder()
                    .email("user-test@gmail.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(ERole.USER)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .points(100)
                    .build();
            userRepository.save(user);

            User allocator = User.builder()
                    .email("allocator-test@gmail.com")
                    .password(passwordEncoder.encode("allocator123"))
                    .role(ERole.ALLOCATOR)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .points(5000)
                    .build();
            userRepository.save(allocator);
            System.out.println("Users seeded successfully.");
        }

        // --- SEED VOUCHERS ---
        if (voucherRepository.count() == 0) {
            System.out.println("Seeding vouchers...");
            Voucher v1 = Voucher.builder()
                    .title("Giảm 10%")
                    .description("Voucher giảm 10% cho đơn hàng từ 200k")
                    .imageUrl("https://i.imgur.com/g45RE2s.png") // <-- Thêm URL ảnh
                    .discountType("percent")
                    .discountValue(10)
                    .minOrderValue(200000)
                    .maxDiscount(50000)
                    .pointCost(50)
                    .quantity(100)
                    .claimed(0)
                    .startDate(LocalDateTime.now().minusDays(1))
                    .endDate(LocalDateTime.now().plusMonths(6))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v2 = Voucher.builder()
                    .title("Giảm 50k")
                    .description("Voucher giảm trực tiếp 50k cho đơn hàng từ 300k")
                    .imageUrl("https://i.imgur.com/s4a2G3b.png") // <-- Thêm URL ảnh
                    .discountType("fixed")
                    .discountValue(50000)
                    .minOrderValue(300000)
                    .maxDiscount(50000)
                    .pointCost(30)
                    .quantity(200)
                    .claimed(0)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusMonths(3))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v3 = Voucher.builder()
                    .title("Voucher Tết")
                    .description("Giảm 20% cho đơn hàng từ 500k")
                    .imageUrl("https://i.imgur.com/K7wzVmn.png") // <-- Thêm URL ảnh
                    .discountType("percent")
                    .discountValue(20)
                    .minOrderValue(500000)
                    .maxDiscount(200000)
                    .pointCost(80)
                    .quantity(50)
                    .claimed(0)
                    .startDate(LocalDateTime.now().plusDays(5))
                    .endDate(LocalDateTime.now().plusMonths(1))
                    .status(VoucherStatus.INACTIVE)
                    .isPublished(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v4 = Voucher.builder()
                    .title("Flash Sale 30%")
                    .description("Voucher giảm 30% cho mọi đơn hàng, tối đa 100k")
                    .imageUrl("https://i.imgur.com/jV7fGzS.png") // <-- Thêm URL ảnh
                    .discountType("percent")
                    .discountValue(30)
                    .minOrderValue(100000)
                    .maxDiscount(100000)
                    .pointCost(60)
                    .quantity(150)
                    .claimed(0)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(7))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v5 = Voucher.builder()
                    .title("Miễn phí vận chuyển")
                    .description("Voucher miễn phí vận chuyển cho đơn từ 100k")
                    .imageUrl("https://i.imgur.com/lO2q0jM.png") // <-- Thêm URL ảnh
                    .discountType("fixed")
                    .discountValue(30000)
                    .minOrderValue(100000)
                    .maxDiscount(30000)
                    .pointCost(20)
                    .quantity(500)
                    .claimed(0)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusMonths(2))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v6 = Voucher.builder()
                    .title("Summer Sale 15%")
                    .description("Voucher giảm 15% cho đơn hàng từ 400k")
                    .imageUrl("https://i.imgur.com/Y8k2f5h.png") // <-- Thêm URL ảnh
                    .discountType("percent")
                    .discountValue(15)
                    .minOrderValue(400000)
                    .maxDiscount(80000)
                    .pointCost(70)
                    .quantity(120)
                    .claimed(0)
                    .startDate(LocalDateTime.now().plusDays(2))
                    .endDate(LocalDateTime.now().plusMonths(2))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v7 = Voucher.builder()
                    .title("Black Friday 70k")
                    .description("Giảm 70k cho đơn từ 500k")
                    .imageUrl("https://i.imgur.com/t3gqU3o.png") // <-- Thêm URL ảnh
                    .discountType("fixed")
                    .discountValue(70000)
                    .minOrderValue(500000)
                    .maxDiscount(70000)
                    .pointCost(90)
                    .quantity(80)
                    .claimed(0)
                    .startDate(LocalDateTime.now().plusMonths(2))
                    .endDate(LocalDateTime.now().plusMonths(3))
                    .status(VoucherStatus.INACTIVE)
                    .isPublished(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v8 = Voucher.builder()
                    .title("VIP 25%")
                    .description("Voucher dành cho khách hàng VIP, giảm 25% đơn từ 1 triệu")
                    .imageUrl("https://i.imgur.com/bXj3v1d.png") // <-- Thêm URL ảnh
                    .discountType("percent")
                    .discountValue(25)
                    .minOrderValue(1000000)
                    .maxDiscount(300000)
                    .pointCost(200)
                    .quantity(20)
                    .claimed(0)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusMonths(12))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v9 = Voucher.builder()
                    .title("Giảm 100k")
                    .description("Giảm 100k cho đơn hàng từ 700k")
                    .imageUrl("https://i.imgur.com/LpW3f7j.png") // <-- Thêm URL ảnh
                    .discountType("fixed")
                    .discountValue(100000)
                    .minOrderValue(700000)
                    .maxDiscount(100000)
                    .pointCost(120)
                    .quantity(90)
                    .claimed(0)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusMonths(4))
                    .status(VoucherStatus.ACTIVE)
                    .isPublished(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Voucher v10 = Voucher.builder()
                    .title("End Year Sale 50%")
                    .description("Giảm 50% cho đơn hàng từ 2 triệu, tối đa 500k")
                    .imageUrl("https://i.imgur.com/n7g2wR5.png") // <-- Thêm URL ảnh
                    .discountType("percent")
                    .discountValue(50)
                    .minOrderValue(2000000)
                    .maxDiscount(500000)
                    .pointCost(300)
                    .quantity(40)
                    .claimed(0)
                    .startDate(LocalDateTime.now().plusMonths(5))
                    .endDate(LocalDateTime.now().plusMonths(6))
                    .status(VoucherStatus.INACTIVE)
                    .isPublished(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Tối ưu: Sử dụng saveAll để lưu tất cả voucher trong một lần, hiệu quả hơn
            voucherRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10));
            System.out.println("Seeded 10 sample vouchers successfully.");
        }
    }
}