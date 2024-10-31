package me.ikno.documentapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "document")
public class DocumentModel {
    @Id
    @Size(max = 32)
    @Column(name = "document_id", nullable = false, length = 32)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_directory_id", nullable = false)
    private DirectoryModel parentDirectory;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_changed", nullable = false)
    private Instant dateChanged;

}