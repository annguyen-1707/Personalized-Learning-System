//package vn.fu_ohayo.aspect;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import vn.fu_ohayo.entity.SystemLog;
//import vn.fu_ohayo.enums.RoleEnum;
//import vn.fu_ohayo.repository.SystemLogRepository;
//
//import java.time.LocalDateTime;
//import java.util.Collection;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j(topic = "SystemLogAspect")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class SystemLogAspect {
//    SystemLogRepository systemLogRepository;
//
//    @Pointcut("execution(* vn.fu_ohayo.controller..*(..)) || execution(* vn.fu_ohayo.service..*(..))")
//    public void appPackagePointcut() {}
//
//    @AfterReturning(pointcut = "appPackagePointcut()", returning = "result")
//    public void logAfterMethod(JoinPoint joinPoint, Object result) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            log.info("Logging action after method execution: {}", authentication.getAuthorities());
//        } else {
//            log.warn("Authentication is null in SecurityContext during logging.");
//        }
//        RoleEnum role = null;
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//            String roleName = authorities.stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .findFirst()
//                    .orElse(null);
//            log.info("Role name extracted: {}", roleName);
//
//            if (roleName != null) {
//                roleName = roleName.replace("ROLE_", "");
//                try {
//                    role = RoleEnum.valueOf(roleName);
//                } catch (IllegalArgumentException e) {
//                    role = null;
//                }
//            }
//        }
//
//        String methodName = joinPoint.getSignature().toShortString();
//
//        methodName = methodName.replace("Controller", "");
//        methodName = methodName.replace("Service", "");
//        methodName = methodName.replace("Imp", "");
//        methodName = methodName.replaceAll("\\(\\.\\.\\)", "");
//
//        String action = methodName.replace(".", " ");
//
//        if (action.equals("SystemLog searchSystemLog")) {
//            return;
//        }
//
//        String details = String.format("%s action performed by '%s' with result: %s",
//                action, role, result != null ? result.toString() : "null");
//
//        SystemLog systemLog = SystemLog.builder()
//                .action(action)
//                .details(details)
//                .role(role)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        systemLogRepository.save(systemLog);
//
//        log.info("Logged action: {}, details: {}, role: {}", action, details, role);
//    }
//
//}
//
//
