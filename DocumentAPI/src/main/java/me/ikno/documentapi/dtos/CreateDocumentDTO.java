package me.ikno.documentapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocumentDTO {
    private String displayName;
    private String parentId;
}
