package me.ikno.documentapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "directory")
public class DirectoryModel {
    @Id
    @Size(max = 32)
    @Column(name = "directory_id", nullable = false, length = 16)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    // TODO: Flatten directory hierarchy
    @NotNull
    @Size(max = 32)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "parent_directory_id", nullable = false)
    private String parentId;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
}