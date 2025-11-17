package vn.fu_ohayo.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GrammarResponse {

    private int grammarId;

    private String titleJp;

    private String structure;

    private String meaning;

    private String usage;

    private String example;

    private JlptLevel jlptLevel;

//    @JsonIgnore
//    private LessonResponse lesson;

    private Date updateAt;

    private boolean isDeleted;

}
