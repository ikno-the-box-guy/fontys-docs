package me.ikno.documentapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDirectoryDTO {
    private String displayName;
    private String parentId;
}
