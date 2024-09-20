package me.ikno.userapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "user_schema")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Size(max = 320)
    @NotNull
    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Size(max = 72)
    @NotNull
    @Column(name = "password", nullable = false, length = 72)
    private String password;

    @NotNull
    @Column(name = "root_directory_id", nullable = false)
    private Integer rootDirectoryId;

}