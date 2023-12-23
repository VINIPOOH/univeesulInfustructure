package dto;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class UserStatisticDto {
    private String email;
    private String password;
    private String roleType;

    UserStatisticDto(String email, String password, String roleType) {
        this.email = email;
        this.password = password;
        this.roleType = roleType;
    }

    public static UserStatisticDtoBuilder builder() {
        return new UserStatisticDtoBuilder();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public static class UserStatisticDtoBuilder {
        private String email;
        private String password;
        private String roleType;

        UserStatisticDtoBuilder() {
        }

        public UserStatisticDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserStatisticDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserStatisticDtoBuilder roleType(String roleType) {
            this.roleType = roleType;
            return this;
        }

        public UserStatisticDto build() {
            return new UserStatisticDto(email, password, roleType);
        }

        public String toString() {
            return "UserStatisticDto.UserStatisticDtoBuilder(email=" + this.email + ", password=" + this.password + ", roleType=" + this.roleType + ")";
        }
    }
}
