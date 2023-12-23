package dal.entity;

import java.time.LocalDate;

/**
 * Represent Bill table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class Bill extends Entity {

    private Delivery delivery;
    private Boolean isDeliveryPaid;
    private long costInCents;
    private LocalDate dateOfPay;
    private User user;

    public Bill(Long id, Delivery delivery, Boolean isDeliveryPaid, long costInCents, LocalDate dateOfPay, User user) {
        super(id);
        this.delivery = delivery;
        this.isDeliveryPaid = isDeliveryPaid;
        this.costInCents = costInCents;
        this.dateOfPay = dateOfPay;
        this.user = user;
    }

    public Bill() {
    }

    public static BillBuilder builder() {
        return new BillBuilder();
    }

    public User getUser() {
        return user;
    }

    public Delivery getDelivery() {
        return this.delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setDeliveryPaid(Boolean deliveryPaid) {
        isDeliveryPaid = deliveryPaid;
    }

    public Boolean getIsDeliveryPaid() {
        return this.isDeliveryPaid;
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }

    public LocalDate getDateOfPay() {
        return this.dateOfPay;
    }

    public static class BillBuilder {
        private long id;
        private Delivery delivery;
        private Boolean isDeliveryPaid;
        private long costInCents;
        private LocalDate dateOfPay;
        private User user;

        BillBuilder() {
        }

        public BillBuilder id(long id) {
            this.id = id;
            return this;
        }

        public BillBuilder delivery(Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        public BillBuilder user(User user) {
            this.user = user;
            return this;
        }

        public BillBuilder isDeliveryPaid(Boolean isDeliveryPaid) {
            this.isDeliveryPaid = isDeliveryPaid;
            return this;
        }

        public BillBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public BillBuilder dateOfPay(LocalDate dateOfPay) {
            this.dateOfPay = dateOfPay;
            return this;
        }

        public Bill build() {
            return new Bill(id, delivery, isDeliveryPaid, costInCents, dateOfPay, user);
        }
    }
}
