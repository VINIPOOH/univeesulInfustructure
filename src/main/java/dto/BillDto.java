package dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Dto which introduce {@link dal.entity.Bill}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class BillDto {
    private final Boolean isDeliveryPaid;
    private final LocalDate dateOfPay;
    private long deliveryId;
    private long id;
    private long costInCents;

    BillDto(long deliveryId, long id, Boolean isDeliveryPaid, long costInCents, LocalDate dateOfPay) {
        this.deliveryId = deliveryId;
        this.id = id;
        this.isDeliveryPaid = isDeliveryPaid;
        this.costInCents = costInCents;
        this.dateOfPay = dateOfPay;
    }

    public static BillDtoBuilder builder() {
        return new BillDtoBuilder();
    }

    public LocalDate getDateOfPay() {
        return dateOfPay;
    }

    public long getDeliveryId() {
        return this.deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillDto billDto = (BillDto) o;
        return deliveryId == billDto.deliveryId &&
                id == billDto.id &&
                costInCents == billDto.costInCents &&
                Objects.equals(isDeliveryPaid, billDto.isDeliveryPaid) &&
                Objects.equals(dateOfPay, billDto.dateOfPay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId, id, isDeliveryPaid, costInCents, dateOfPay);
    }

    public static class BillDtoBuilder {
        private long deliveryId;
        private long id;
        private Boolean isDeliveryPaid;
        private long costInCents;
        private LocalDate dateOfPay;

        BillDtoBuilder() {
        }

        public BillDtoBuilder deliveryId(long deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }

        public BillDtoBuilder id(long id) {
            this.id = id;
            return this;
        }

        public BillDtoBuilder isDeliveryPaid(Boolean isDeliveryPaid) {
            this.isDeliveryPaid = isDeliveryPaid;
            return this;
        }

        public BillDtoBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public BillDtoBuilder dateOfPay(LocalDate dateOfPay) {
            this.dateOfPay = dateOfPay;
            return this;
        }

        public BillDto build() {
            return new BillDto(deliveryId, id, isDeliveryPaid, costInCents, dateOfPay);
        }
    }
}
