package me.ikno.documentapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDirectoryDTO {
    private String displayName;
    private Integer parentId;
}
