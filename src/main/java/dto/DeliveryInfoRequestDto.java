package dto;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class DeliveryInfoRequestDto {

    private final int deliveryWeight;

    private long localitySandID;

    private long localityGetID;

    DeliveryInfoRequestDto(int deliveryWeight, long localitySandID, long localityGetID) {
        this.deliveryWeight = deliveryWeight;
        this.localitySandID = localitySandID;
        this.localityGetID = localityGetID;
    }

    public static DeliveryInfoRequestDtoBuilder builder() {
        return new DeliveryInfoRequestDtoBuilder();
    }

    @Override
    public String toString() {
        return "DeliveryInfoRequestDto{" +
                "deliveryWeight=" + deliveryWeight +
                ", localitySandID=" + localitySandID +
                ", localityGetID=" + localityGetID +
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


    public static class DeliveryInfoRequestDtoBuilder {
        private int deliveryWeight;
        private long localitySandID;
        private long localityGetID;

        DeliveryInfoRequestDtoBuilder() {
        }

        public DeliveryInfoRequestDtoBuilder deliveryWeight(int deliveryWeight) {
            this.deliveryWeight = deliveryWeight;
            return this;
        }

        public DeliveryInfoRequestDtoBuilder localitySandID(long localitySandID) {
            this.localitySandID = localitySandID;
            return this;
        }

        public DeliveryInfoRequestDtoBuilder localityGetID(long localityGetID) {
            this.localityGetID = localityGetID;
            return this;
        }

        public DeliveryInfoRequestDto build() {
            return new DeliveryInfoRequestDto(deliveryWeight, localitySandID, localityGetID);
        }

        public String toString() {
            return "DeliveryInfoRequestDto.DeliveryInfoRequestDtoBuilder(deliveryWeight=" + this.deliveryWeight + ", localitySandID=" + this.localitySandID + ", localityGetID=" + this.localityGetID + ")";
        }
    }
}
