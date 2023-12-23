package dal.entity;

import java.util.List;

/**
 * Represent User table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class User extends Entity {
    private String email;
    private RoleType roleType;
    private String password;
    private Long userMoneyInCents;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private List<Delivery> waysWhereThisUserIsSend;
    private List<Delivery> waysWhereThisUserIsGet;

    public User(Long id, String email, RoleType roleType, String password, Long userMoneyInCents, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, List<Delivery> waysWhereThisUserIsSend, List<Delivery> waysWhereThisUserIsGet) {
        super(id);
        this.email = email;
        this.roleType = roleType;
        this.password = password;
        this.userMoneyInCents = userMoneyInCents;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.waysWhereThisUserIsSend = waysWhereThisUserIsSend;
        this.waysWhereThisUserIsGet = waysWhereThisUserIsGet;
    }

    public User() {
    }


    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserMoneyInCents() {
        return this.userMoneyInCents;
    }

    public void setUserMoneyInCents(Long userMoneyInCents) {
        this.userMoneyInCents = userMoneyInCents;
    }

    public static class UserBuilder {
        private long id;
        private String email;
        private RoleType roleType;
        private String password;
        private Long userMoneyInCents;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;
        private List<Delivery> waysWhereThisUserIsSend;
        private List<Delivery> waysWhereThisUserIsGet;

        UserBuilder() {
        }

        public UserBuilder id(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder roleType(RoleType roleType) {
            this.roleType = roleType;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder userMoneyInCents(Long userMoneyInCents) {
            this.userMoneyInCents = userMoneyInCents;
            return this;
        }

        public UserBuilder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public UserBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public UserBuilder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder waysWhereThisUserIsSend(List<Delivery> waysWhereThisUserIsSend) {
            this.waysWhereThisUserIsSend = waysWhereThisUserIsSend;
            return this;
        }

        public UserBuilder waysWhereThisUserIsGet(List<Delivery> waysWhereThisUserIsGet) {
            this.waysWhereThisUserIsGet = waysWhereThisUserIsGet;
            return this;
        }

        public User build() {

            return new User(id, email, roleType, password, userMoneyInCents, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, waysWhereThisUserIsSend, waysWhereThisUserIsGet);
        }

    }
}
