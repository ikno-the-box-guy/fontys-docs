package me.ikno.documentapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "directory", schema = "document_schema")
public class DirectoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "directory_id", nullable = false)
    private Integer id;

    @Setter
    @NotNull
    @Size(max = 255)
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Setter
    @Column(name = "parent_directory_id", nullable = false)
    private Integer parentId;

    @Setter
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
}
