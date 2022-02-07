package com.example.plandial;

import java.util.ArrayList;

public class Category {
    private String name;
    private final ArrayList<AlertDial> alertDials = new ArrayList<>();

    public Category(final String name) {
        // 다이얼을 가지지 않은 카테고리를 생성하는 생성자
        this(name, new ArrayList<>());
    }

    public Category(final String name, final ArrayList<AlertDial> alertDials) {
        this.name = name;

        this.addDials(alertDials);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDialCount() {
        return this.alertDials.size();
    }

    public AlertDial getDialByIndex(int index) {
        try {
            return this.alertDials.get(index);
        } catch (IndexOutOfBoundsException e) {
            assert false;
            return null;
        }
    }

    public AlertDial getDialByName(String name) {
        for (AlertDial alertDial : alertDials) {
            if (alertDial.getName().equals(name)) {
                return alertDial;
            }
        }

        return null;
    }

    public boolean addDial(AlertDial alertDialToAdd) {
        /* 이미 동일한 다이얼을 가지고 있으면 추가하지 않는다.
         * 이미 동일한 다이얼이 있다면 false, 그렇지 않으면 true를 반환한다.
         */

        for (AlertDial addedAlertDial : alertDials) {
            if (alertDialToAdd.equals(addedAlertDial)) {
                return false;
            }
        }

        this.alertDials.add(alertDialToAdd);
        return true;
    }

    public int addDials(ArrayList<AlertDial> dialsToAdd) {
        /* 이미 동일한 다이얼을 가지고 있으면 추가하지 않는다.
         * 추가한 다이얼의 개수를 반환한다.
         */

        boolean hasSameDial;
        int addCount = 0;

        for (AlertDial alertDialToAdd : dialsToAdd) {
            hasSameDial = false;

            for (AlertDial addedAlertDial : alertDials) {
                if (alertDialToAdd.equals(addedAlertDial)) {
                    hasSameDial = true;
                    break;
                }
            }

            if (!hasSameDial) {
                this.alertDials.add(alertDialToAdd);
                ++addCount;
            }
        }

        return addCount;
    }

    public AlertDial removeDialByIndex(int index) {
        try {
            return this.alertDials.remove(index);
        } catch (IndexOutOfBoundsException e) {
            assert false;
            return null;
        }
    }

    public boolean removeDialByObject(AlertDial alertDial) {
        return this.alertDials.remove(alertDial);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.alertDials.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Category) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Category other = (Category) obj;
        return this.name.equals(other.name) && this.alertDials.equals(other.alertDials);
    }
}
