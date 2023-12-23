package dto;

import java.util.Objects;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class PriceAndTimeOnDeliveryDto {
    private final int timeOnWayInHours;
    private long costInCents;

    PriceAndTimeOnDeliveryDto(long costInCents, int timeOnWayInHours) {
        this.costInCents = costInCents;
        this.timeOnWayInHours = timeOnWayInHours;
    }

    public static PriceAndTimeOnDeliveryDtoBuilder builder() {
        return new PriceAndTimeOnDeliveryDtoBuilder();
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }

    public int getTimeOnWayInHours() {
        return this.timeOnWayInHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceAndTimeOnDeliveryDto that = (PriceAndTimeOnDeliveryDto) o;
        return costInCents == that.costInCents &&
                timeOnWayInHours == that.timeOnWayInHours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(costInCents, timeOnWayInHours);
    }

    public static class PriceAndTimeOnDeliveryDtoBuilder {
        private long costInCents;
        private int timeOnWayInHours;

        PriceAndTimeOnDeliveryDtoBuilder() {
        }

        public PriceAndTimeOnDeliveryDtoBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public PriceAndTimeOnDeliveryDtoBuilder timeOnWayInHours(int timeOnWayInHours) {
            this.timeOnWayInHours = timeOnWayInHours;
            return this;
        }

        public PriceAndTimeOnDeliveryDto build() {
            return new PriceAndTimeOnDeliveryDto(costInCents, timeOnWayInHours);
        }
    }
}
