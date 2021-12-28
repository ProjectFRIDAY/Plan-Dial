package com.example.plandial;

public class Period {
    private final UnitOfTime unitOfTime;
    private final int times;  // '시간'이 아닌 '배'의 의미임. 즉, period = times * unitOfTime

    private Period(final UnitOfTime unitOfTime, final int times) {
        this.unitOfTime = unitOfTime;
        this.times = times;
    }

    public static Period createPeriodOrNull(final UnitOfTime unitOfTime, final int times) {
        /*
         * 다음의 경우에 유효하지 않은 객체 생성으로 판단하고 null을 반환함
         * 1) times가 0 이하인 경우
         * 2) getPeriodInMillis 함수 호출 시 오버플로우가 발생하는 경우
         */

        if (times <= 0 || unitOfTime.getSeconds() > Long.MAX_VALUE / (times * (long) UnitOfTime.MILLIS_PER_SECOND)) {
            return null;
        }

        return new Period(unitOfTime, times);
    }

    public long getPeriodInSeconds() {
        return (long) unitOfTime.getSeconds() * times;
    }

    public long getPeriodInMillis() {
        return (long) unitOfTime.getSeconds() * UnitOfTime.MILLIS_PER_SECOND * times;
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

        if (obj == null || !(obj instanceof Period) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Period other = (Period) obj;
        return this.unitOfTime == other.unitOfTime && this.times == other.times;
    }

}
