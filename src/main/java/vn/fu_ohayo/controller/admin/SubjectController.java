package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@Slf4j(topic = "SubjectController")
public class SubjectController {
    
    private final SubjectService subjectService;
    
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ApiResponse<Page<SubjectResponse>> getAllSubjectsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        
        return ApiResponse.<Page<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all subjects successfully")
                .data(subjectService.getAllSubjectsForAdmin(page, size))
                .build();
    }

    @GetMapping("/all-courses")
    public ApiResponse<Page<SubjectResponse>> getAllSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        log.info(email);
        return ApiResponse.<Page<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all subjects successfully")
                .data(subjectService.getAllActiveSubjects(page, size, email))
                .build();
    }

    @GetMapping("/client-all-courses")
    public ApiResponse<Page<SubjectResponse>> getAllPublicSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return ApiResponse.<Page<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all subjects successfully")
                .data(subjectService.getAllPublicSubjects(page, size))
                .build();
    }



    @GetMapping("/students")
    public  ApiResponse<Page<ProgressSubjectResponse>> getAllByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ){
        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        Page<ProgressSubjectResponse> subjectResponses = subjectService.getAllByUserId(page, size, email);
        return ApiResponse.<Page<ProgressSubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched subjects by user ID successfully")
                .data(subjectResponses)
                .build();
    }

    @PostMapping
    public ApiResponse<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return ApiResponse.<SubjectResponse>builder()
                .code("201")
                .status("success")
                .message("Subject created successfully")
                .data(subjectService.createSubject(subjectRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable("id") int id,
                                                      @Valid @RequestBody SubjectRequest subjectRequest) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Subject updated successfully")
                .data(subjectService.updateSubject(id, subjectRequest))
                .build();
    }
    @PatchMapping("/accept/{id}")
    public ApiResponse<SubjectResponse> acceptSubject(@PathVariable("id") int id) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Subject accepted successfully")
                .data(subjectService.acceptSubject(id))
                .build();
    }

    @PatchMapping("/reject/{id}")
    public ApiResponse<SubjectResponse> rejectSubject(@PathVariable("id") int id) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Subject rejected successfully")
                .data(subjectService.rejectSubject(id))
                .build();
    }

    @PatchMapping("/inactive/{id}")
    public ApiResponse<SubjectResponse> inactiveSubject(@PathVariable("id") int id) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Subject inactive successfully")
                .data(subjectService.inactiveSubject(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSubject(@PathVariable("id") int id) {
        subjectService.deleteSubject(id);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Subject deleted successfully")
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<List<SubjectStatus>> getSubjectStatus() {
        List<SubjectStatus> statuses = List.of(SubjectStatus.values());
        return ApiResponse.<List<SubjectStatus>>builder()
                .code("200")
                .status("success")
                .message("Fetched lesson statuses successfully")
                .data(statuses)
                .build();
    }

    @GetMapping("/{id}")   //@GetMapping("/{id:\\d+}")
    public ApiResponse<SubjectResponse> getSubjectById(@PathVariable("id") int id) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Fetched subject successfully")
                .data(subjectService.getSubjectById(id))
                .build();
    }

    @GetMapping("/getListAllSubjectActive")
    public ApiResponse<List<SubjectResponse>> getListAllSubjectsByStatus() {
        return ApiResponse.<List<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched list all subject successfully")
                .data(subjectService.getAllListActiveSubjectsByStatus())
                .build();
    }

    @GetMapping("/getListAllSubject")
    public ApiResponse<List<SubjectResponse>> getListAllSubjects() {
        return ApiResponse.<List<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched list all subject successfully")
                .data(subjectService.getAllListActiveSubjectsByStatus())
                .build();
    }

}
