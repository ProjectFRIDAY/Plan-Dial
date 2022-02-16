package com.friday.plandial;

public class Period {
    private final UnitOfTime unitOfTime;
    private final int times;  // '시간'이 아닌 '배'의 의미임. 즉, period = times * unitOfTime

    public Period(final UnitOfTime unitOfTime, final int times) {
        assert times > 0;
        assert unitOfTime.getSeconds() <= Long.MAX_VALUE / (times * (long) UnitOfTime.MILLIS_PER_SECOND);

        this.unitOfTime = unitOfTime;
        this.times = times;
    }

    public long getPeriodInSeconds() {
        return (long) unitOfTime.getSeconds() * times;
    }

    public long getPeriodInMillis() {
        return (long) unitOfTime.getSeconds() * UnitOfTime.MILLIS_PER_SECOND * times;
    }

    public int getTimes() {
        return times;
    }

    public UnitOfTime getUnit() {
        return unitOfTime;
    }

    @Override
    public int hashCode() {
        // { unitOfTime, times } 에서 { HOUR, 24 } 와 { DAY, 1 } 은 다름.
        int result = 17;
        result = 31 * result + unitOfTime.getSeconds();
        result = 31 * result + times;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Period) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Period other = (Period) obj;
        return this.unitOfTime == other.unitOfTime && this.times == other.times;
    }

}
