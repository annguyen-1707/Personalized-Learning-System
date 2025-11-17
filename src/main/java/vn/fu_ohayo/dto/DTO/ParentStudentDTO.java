package vn.fu_ohayo.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentStudentDTO {
    private Integer id;
    private SimpleUserDTO user;
}