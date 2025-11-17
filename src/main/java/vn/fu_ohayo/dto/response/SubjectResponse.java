package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.SubjectStatus;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectResponse {

    private int subjectId;

    private String subjectName;

    private String subjectCode;

    private String description;

    private SubjectStatus status;

    private int countUsers;

    private Date createdAt;

    private Date updatedAt;

    private int countLessons;

    private String thumbnailUrl;

    public SubjectResponse(int subjectId, String subjectCode, String subjectName,
                           String description,SubjectStatus status, long countUsers,
                           Date updatedAt, int countLessons,
                           String thumbnailUrl, Date createdAt) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.description = description;
        this.status = status;
        this.countUsers = (int) countUsers;
        this.updatedAt = updatedAt;
        this.countLessons = countLessons;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

}
