package me.ikno.documentapi.bodies.directory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDirectoryBody {
    private String displayName;
    private Integer parentId;
}
