package org.example.statistics.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

public class UserDTO {

    @Getter @Setter
    private long id;
    @Getter @Setter
    private String username;
    @Getter
    private LocalDate registrationDate;

    public UserDTO(long id, String username) {
        this.id = id;
        this.username = username;
        this.registrationDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                '}';
    }
}
