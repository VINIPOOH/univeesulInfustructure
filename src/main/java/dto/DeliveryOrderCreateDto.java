package dto;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class DeliveryOrderCreateDto {
    private final int deliveryWeight;
    private final String addresseeEmail;
    private long localitySandID;
    private long localityGetID;

    DeliveryOrderCreateDto(int deliveryWeight, long localitySandID, long localityGetID, String addresseeEmail) {
        this.deliveryWeight = deliveryWeight;
        this.localitySandID = localitySandID;
        this.localityGetID = localityGetID;
        this.addresseeEmail = addresseeEmail;
    }

    public static DeliveryOrderCreateDtoBuilder builder() {
        return new DeliveryOrderCreateDtoBuilder();
    }

    @Override
    public String toString() {
        return "DeliveryOrderCreateDto{" +
                "deliveryWeight=" + deliveryWeight +
                ", localitySandID=" + localitySandID +
                ", localityGetID=" + localityGetID +
                ", addresseeEmail='" + addresseeEmail + '\'' +
                '}';
    }

    public int getDeliveryWeight() {
        return this.deliveryWeight;
    }

    public long getLocalitySandID() {
        return this.localitySandID;
    }

    public void setLocalitySandID(long localitySandID) {
        this.localitySandID = localitySandID;
    }

    public long getLocalityGetID() {
        return this.localityGetID;
    }

    public void setLocalityGetID(long localityGetID) {
        this.localityGetID = localityGetID;
    }

    public String getAddresseeEmail() {
        return this.addresseeEmail;
    }

    public static class DeliveryOrderCreateDtoBuilder {
        private int deliveryWeight;
        private long localitySandID;
        private long localityGetID;
        private String addresseeEmail;

        DeliveryOrderCreateDtoBuilder() {
        }

        public DeliveryOrderCreateDtoBuilder deliveryWeight(int deliveryWeight) {
            this.deliveryWeight = deliveryWeight;
            return this;
        }

        public DeliveryOrderCreateDtoBuilder localitySandID(long localitySandID) {
            this.localitySandID = localitySandID;
            return this;
        }

        public DeliveryOrderCreateDtoBuilder localityGetID(long localityGetID) {
            this.localityGetID = localityGetID;
            return this;
        }

        public DeliveryOrderCreateDtoBuilder addresseeEmail(String addresseeEmail) {
            this.addresseeEmail = addresseeEmail;
            return this;
        }

        public DeliveryOrderCreateDto build() {
            return new DeliveryOrderCreateDto(deliveryWeight, localitySandID, localityGetID, addresseeEmail);
        }
    }
}
