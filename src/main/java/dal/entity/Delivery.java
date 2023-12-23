package dal.entity;

import java.time.LocalDate;

/**
 * Represent Delivery table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class Delivery extends Entity {
    private LocalDate arrivalDate;
    private Way way;
    private User addressee;
    private Boolean isPackageReceived;
    private Boolean isDeliveryPaid;
    private int weight;
    private long costInCents;
    private Bill bill;

    public Delivery(Long id, LocalDate arrivalDate, Way way, User addressee, Boolean isPackageReceived, Boolean isDeliveryPaid, int weight, long costInCents, Bill bill) {
        super(id);

        this.arrivalDate = arrivalDate;
        this.way = way;
        this.addressee = addressee;
        this.isPackageReceived = isPackageReceived;
        this.isDeliveryPaid = isDeliveryPaid;
        this.weight = weight;
        this.costInCents = costInCents;
        this.bill = bill;
    }

    public Delivery() {
    }

    public static DeliveryBuilder builder() {
        return new DeliveryBuilder();
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public LocalDate getArrivalDate() {
        return this.arrivalDate;
    }

    public Way getWay() {
        return this.way;
    }

    public void setWay(Way way) {
        this.way = way;
    }

    public User getAddressee() {
        return this.addressee;
    }

    public Boolean getIsPackageReceived() {
        return this.isPackageReceived;
    }

    public Boolean getIsDeliveryPaid() {
        return this.isDeliveryPaid;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }


    public static class DeliveryBuilder {
        private long id;
        private LocalDate arrivalDate;
        private Way way;
        private User addressee;

        private Boolean isPackageReceived;
        private Boolean isDeliveryPaid;
        private int weight;
        private long costInCents;
        private Bill bill;

        DeliveryBuilder() {
        }

        public DeliveryBuilder id(long id) {
            this.id = id;
            return this;
        }

        public DeliveryBuilder arrivalDate(LocalDate arrivalDate) {
            this.arrivalDate = arrivalDate;
            return this;
        }

        public DeliveryBuilder way(Way way) {
            this.way = way;
            return this;
        }

        public DeliveryBuilder bill(Bill bill) {
            this.bill = bill;
            return this;
        }

        public DeliveryBuilder addressee(User addressee) {
            this.addressee = addressee;
            return this;
        }

        public DeliveryBuilder isPackageReceived(Boolean isPackageReceived) {
            this.isPackageReceived = isPackageReceived;
            return this;
        }

        public DeliveryBuilder isDeliveryPaid(Boolean isDeliveryPaid) {
            this.isDeliveryPaid = isDeliveryPaid;
            return this;
        }

        public DeliveryBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public DeliveryBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public Delivery build() {
            return new Delivery(id, arrivalDate, way, addressee, isPackageReceived, isDeliveryPaid, weight, costInCents, bill);
        }
    }
}
