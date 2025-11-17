package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequest {
    private Integer currentPage = 0;
    private Integer pageSize = 10;
}
