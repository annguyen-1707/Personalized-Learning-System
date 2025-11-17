package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.CountUserEachMembership;
import vn.fu_ohayo.dto.response.DashboardCouseraRateResponse;
import vn.fu_ohayo.dto.response.DashboardOverviewResponse;
import vn.fu_ohayo.dto.response.DashboardMonthlyUserCountResponse;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.SystemLog;
import vn.fu_ohayo.enums.*;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.DashboardAdminService;

import java.math.BigInteger;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardAdminServiceImp implements DashboardAdminService {
    private final ProgressSubjectRepository progressSubjectRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final LessonRepository lessonRepository;
    private final SystemLogRepository systemLogRepository;

    @Override
    public List<DashboardCouseraRateResponse> countCouseraRate(List<Integer> idSubjects) {
        if (idSubjects == null || idSubjects.isEmpty()) {
            throw new AppException(ErrorEnum.LIST_SUBJECT_NULL);
        }
        List<DashboardCouseraRateResponse> dashboardCouseraRateResponseList = new ArrayList<>();
        List<Subject> subjectList = subjectRepository.findAllBySubjectIdIn(idSubjects);
        for (Subject subject : subjectList) {
            int totalProgressEachSubjectComplete = progressSubjectRepository.countAllByProgressStatusAndSubject(ProgressStatus.COMPLETED, subject);
            int totalProgressEachSubject = progressSubjectRepository.countAllBySubject(subject);
            double rateComplete = totalProgressEachSubjectComplete * 100 / totalProgressEachSubject;
            dashboardCouseraRateResponseList.add(DashboardCouseraRateResponse.builder()
                    .couseraCompleteRate(rateComplete)
                    .subjectId(subject.getSubjectId())
                    .totalUserJoin(totalProgressEachSubject)
                    .subjectName(subject.getSubjectName())
                    .build());
        }
        return dashboardCouseraRateResponseList;
    }

    @Override
    public List<DashboardMonthlyUserCountResponse> countTotalUserEachMonth(int year) {
        List<Object[]> rawData = userRepository.countActiveUsersEachMonth(year);

        return rawData.stream()
                .map(row -> new DashboardMonthlyUserCountResponse(
                        ((Number) row[0]).intValue(),   // m.month
                        ((Number) row[1]).longValue()   // COUNT(*)
                ))
                .collect(Collectors.toList());

    }

    @Override
    public DashboardOverviewResponse countOverview() {
        Date dateStartOfThisMonth = Date.from(
                YearMonth.now().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
        int totalUserNow = userRepository.countAllByStatus(UserStatus.ACTIVE);
        int totalUserBeforeThisMonth = userRepository.countAllByStatusAndCreatedAtBefore(UserStatus.ACTIVE, dateStartOfThisMonth);
        int totalSubjectNow = subjectRepository.countAllByStatus(SubjectStatus.PUBLIC);
        int totalSubjectBeforeThisMonth = subjectRepository.countAllByStatusAndCreatedAtBefore(SubjectStatus.PUBLIC, dateStartOfThisMonth);
        int totalLessonNow = lessonRepository.countAllByStatus(LessonStatus.PUBLIC);
        int totalLessonBeforeThisMonth = lessonRepository.countAllByStatusAndCreatedAtBefore(LessonStatus.PUBLIC, dateStartOfThisMonth);
        int totalProgressSubjectComplete = progressSubjectRepository.countAllByProgressStatus(ProgressStatus.COMPLETED);
        long totalAllProgressSubject = progressSubjectRepository.count();
        double completionRateNow = totalProgressSubjectComplete * 100.0 / totalAllProgressSubject;
        int totalProgressSubjectCompleteBeforeThisMonth = progressSubjectRepository.countAllByProgressStatusAndEndDateBefore(ProgressStatus.COMPLETED, dateStartOfThisMonth);
        int totalAllProgressSubjectBeforeThisMonth = progressSubjectRepository.countAllByStartDateAfter(dateStartOfThisMonth);
        double completionRateBeforeThisMonth = totalProgressSubjectCompleteBeforeThisMonth * 100.0 / totalAllProgressSubjectBeforeThisMonth;
        List<CountUserEachMembership> countUserEachMembershipList = Arrays.stream(MembershipLevel.values())
                .map(level -> CountUserEachMembership.builder()
                        .membershipLevel(level)
                        .count(userRepository.countAllByStatusAndMembershipLevel(UserStatus.ACTIVE, level))
                        .build())
                .collect(Collectors.toList());
        return DashboardOverviewResponse.builder()
                .totalUserNow(totalUserNow)
                .totalUserCompareLastMonth(totalUserNow - totalUserBeforeThisMonth)
                .totalSubjectNow(totalSubjectNow)
                .totalSubjectCompareLastMonth(totalSubjectNow - totalSubjectBeforeThisMonth)
                .totalLessonNow(totalLessonNow)
                .totalLessonCompareLastMonth(totalLessonNow - totalLessonBeforeThisMonth)
                .completionRateSubjectNow(completionRateNow)
                .completionRateSubjectCompareLastMonth((completionRateNow - completionRateBeforeThisMonth) / completionRateBeforeThisMonth)
                .countUserEachMembership(countUserEachMembershipList)
                .systemLogs(new ArrayList<>())
                .build();
    }
}
