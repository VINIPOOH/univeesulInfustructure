package dto;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class RegistrationInfoDto {
    private final String username;
    private final String passwordRepeat;
    private String password;

    public RegistrationInfoDto(String username, String password, String passwordRepeat) {
        this.username = username;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
    }

    public static RegistrationInfoDtoBuilder builder() {
        return new RegistrationInfoDtoBuilder();
    }

    @Override
    public String toString() {
        return "RegistrationInfoDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordRepeat='" + passwordRepeat + '\'' +
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

    public String getPasswordRepeat() {
        return this.passwordRepeat;
    }

    public static class RegistrationInfoDtoBuilder {
        private String username;
        private String password;
        private String passwordRepeat;

        RegistrationInfoDtoBuilder() {
        }

        public RegistrationInfoDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public RegistrationInfoDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegistrationInfoDtoBuilder passwordRepeat(String passwordRepeat) {
            this.passwordRepeat = passwordRepeat;
            return this;
        }

        public RegistrationInfoDto build() {
            return new RegistrationInfoDto(username, password, passwordRepeat);
        }

    }
}
