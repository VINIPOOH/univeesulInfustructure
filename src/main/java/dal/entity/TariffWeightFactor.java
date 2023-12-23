package dal.entity;

import java.util.List;

/**
 * Represent TariffWeightFactor table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class TariffWeightFactor extends Entity {
    private final int minWeightRange;
    private final int maxWeightRange;
    private final int overPayOnKilometer;
    private final List<Way> waysWhereUsed;

    public TariffWeightFactor(Long id, int minWeightRange, int maxWeightRange, int overPayOnKilometer, List<Way> waysWhereUsed) {
        super(id);
        this.minWeightRange = minWeightRange;
        this.maxWeightRange = maxWeightRange;
        this.overPayOnKilometer = overPayOnKilometer;
        this.waysWhereUsed = waysWhereUsed;
    }

    public static TariffWeightFactorBuilder builder() {
        return new TariffWeightFactorBuilder();
    }

    public int getMinWeightRange() {
        return this.minWeightRange;
    }

    public int getMaxWeightRange() {
        return this.maxWeightRange;
    }

    public int getOverPayOnKilometer() {
        return this.overPayOnKilometer;
    }

    public List<Way> getWaysWhereUsed() {
        return this.waysWhereUsed;
    }


    public static class TariffWeightFactorBuilder {
        private long id;
        private int minWeightRange;
        private int maxWeightRange;
        private int overPayOnKilometer;
        private List<Way> waysWhereUsed;

        TariffWeightFactorBuilder() {
        }

        public TariffWeightFactorBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TariffWeightFactorBuilder minWeightRange(int minWeightRange) {
            this.minWeightRange = minWeightRange;
            return this;
        }

        public TariffWeightFactorBuilder maxWeightRange(int maxWeightRange) {
            this.maxWeightRange = maxWeightRange;
            return this;
        }

        public TariffWeightFactorBuilder overPayOnKilometer(int overPayOnKilometer) {
            this.overPayOnKilometer = overPayOnKilometer;
            return this;
        }

        public TariffWeightFactorBuilder waysWhereUsed(List<Way> waysWhereUsed) {
            this.waysWhereUsed = waysWhereUsed;
            return this;
        }

        public TariffWeightFactor build() {
            return new TariffWeightFactor(id, minWeightRange, maxWeightRange, overPayOnKilometer, waysWhereUsed);
        }

    }
}
