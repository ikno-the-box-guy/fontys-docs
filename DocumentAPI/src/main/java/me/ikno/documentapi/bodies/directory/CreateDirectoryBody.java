package me.ikno.documentapi.bodies.directory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDirectoryBody {
    private String displayName;
    private Integer parentId;
}
