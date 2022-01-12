package DTO;

import domain.User;
import lombok.Data;

@Data
public class DTO_User {
    private String message;
    private User data;
    private boolean status;
}
