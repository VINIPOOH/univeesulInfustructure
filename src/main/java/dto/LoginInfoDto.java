package dto;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class LoginInfoDto {
    private final String username;
    private String password;

    public LoginInfoDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static LoginInfoDtoBuilder builder() {
        return new LoginInfoDtoBuilder();
    }

    @Override
    public String toString() {
        return "LoginInfoDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static class LoginInfoDtoBuilder {
        private String username;
        private String password;

        LoginInfoDtoBuilder() {
        }

        public LoginInfoDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginInfoDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginInfoDto build() {
            return new LoginInfoDto(username, password);
        }

    }
}
