package dto;

import java.util.Objects;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class LocaliseLocalityDto {
    private Long id;
    private String name;

    public LocaliseLocalityDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public static LocaliseLocalityDtoBuilder builder() {
        return new LocaliseLocalityDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocaliseLocalityDto that = (LocaliseLocalityDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class LocaliseLocalityDtoBuilder {
        private Long id;
        private String name;

        LocaliseLocalityDtoBuilder() {
        }

        public LocaliseLocalityDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LocaliseLocalityDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LocaliseLocalityDto build() {
            return new LocaliseLocalityDto(id, name);
        }


    }
}
