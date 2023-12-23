package dal.dto;

/**
 * Dto for transport info about how many time need to deliver {@link dal.entity.Delivery}
 * and рow much it will be cost
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class DeliveryCostAndTimeDto {
    private final int timeOnWayInHours;
    private long costInCents;

    DeliveryCostAndTimeDto(long costInCents, int timeOnWayInHours) {
        this.costInCents = costInCents;
        this.timeOnWayInHours = timeOnWayInHours;
    }

    public static DeliveryCostAndTimeDtoBuilder builder() {
        return new DeliveryCostAndTimeDtoBuilder();
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

    public static class DeliveryCostAndTimeDtoBuilder {
        private long costInCents;
        private int timeOnWayInHours;

        DeliveryCostAndTimeDtoBuilder() {
        }

        public DeliveryCostAndTimeDtoBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public DeliveryCostAndTimeDtoBuilder timeOnWayInHours(int timeOnWayInHours) {
            this.timeOnWayInHours = timeOnWayInHours;
            return this;
        }

        public DeliveryCostAndTimeDto build() {
            return new DeliveryCostAndTimeDto(costInCents, timeOnWayInHours);
        }

    }
}
