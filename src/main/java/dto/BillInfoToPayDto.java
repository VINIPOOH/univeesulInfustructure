package dto;

import java.util.Objects;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class BillInfoToPayDto {
    private long billId;
    private long price;
    private long deliveryId;
    private int weight;
    private String addreeseeEmail;
    private String localitySandName;
    private String localityGetName;

    BillInfoToPayDto(long billId, long price, long deliveryId, int weight, String addreeseeEmail, String localitySandName, String localityGetName) {
        this.billId = billId;
        this.price = price;
        this.deliveryId = deliveryId;
        this.weight = weight;
        this.addreeseeEmail = addreeseeEmail;
        this.localitySandName = localitySandName;
        this.localityGetName = localityGetName;
    }

    public static BillInfoToPayDtoBuilder builder() {
        return new BillInfoToPayDtoBuilder();
    }

    public long getBillId() {
        return this.billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public long getPrice() {
        return this.price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getDeliveryId() {
        return this.deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAddreeseeEmail() {
        return this.addreeseeEmail;
    }

    public void setAddreeseeEmail(String addreeseeEmail) {
        this.addreeseeEmail = addreeseeEmail;
    }

    public String getLocalitySandName() {
        return this.localitySandName;
    }

    public void setLocalitySandName(String localitySandName) {
        this.localitySandName = localitySandName;
    }

    public String getLocalityGetName() {
        return this.localityGetName;
    }

    public void setLocalityGetName(String localityGetName) {
        this.localityGetName = localityGetName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillInfoToPayDto that = (BillInfoToPayDto) o;
        return billId == that.billId &&
                price == that.price &&
                deliveryId == that.deliveryId &&
                weight == that.weight &&
                addreeseeEmail.equals(that.addreeseeEmail) &&
                localitySandName.equals(that.localitySandName) &&
                localityGetName.equals(that.localityGetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, price, deliveryId, weight, addreeseeEmail, localitySandName, localityGetName);
    }

    public static class BillInfoToPayDtoBuilder {
        private long billId;
        private long price;
        private long deliveryId;
        private int weight;
        private String addreeseeEmail;
        private String localitySandName;
        private String localityGetName;

        BillInfoToPayDtoBuilder() {
        }

        public BillInfoToPayDtoBuilder billId(long billId) {
            this.billId = billId;
            return this;
        }

        public BillInfoToPayDtoBuilder price(long price) {
            this.price = price;
            return this;
        }

        public BillInfoToPayDtoBuilder deliveryId(long deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }

        public BillInfoToPayDtoBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public BillInfoToPayDtoBuilder addreeseeEmail(String addreeserEmail) {
            this.addreeseeEmail = addreeserEmail;
            return this;
        }

        public BillInfoToPayDtoBuilder localitySandName(String localitySandName) {
            this.localitySandName = localitySandName;
            return this;
        }

        public BillInfoToPayDtoBuilder localityGetName(String localityGetName) {
            this.localityGetName = localityGetName;
            return this;
        }

        public BillInfoToPayDto build() {
            return new BillInfoToPayDto(billId, price, deliveryId, weight, addreeseeEmail, localitySandName, localityGetName);
        }

        public String toString() {
            return "BillInfoToPayDto.BillInfoToPayDtoBuilder(billId=" + this.billId + ", price=" + this.price + ", deliveryId=" + this.deliveryId + ", weight=" + this.weight + ", addreeserEmail=" + this.addreeseeEmail + ", localitySandName=" + this.localitySandName + ", localityGetName=" + this.localityGetName + ")";
        }
    }
}
