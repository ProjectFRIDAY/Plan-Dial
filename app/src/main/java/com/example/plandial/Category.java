package com.example.plandial;

import java.util.ArrayList;

public class Category {
    private String name;
    private final ArrayList<Dial> dials = new ArrayList<>();

    public Category(final String name) {
        // 다이얼을 가지지 않은 카테고리를 생성하는 생성자
        this(name, new ArrayList<>());
    }

    public Category(final String name, final ArrayList<Dial> dials) {
        this.name = name;

        this.addDials(dials);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDialCount() {
        return this.dials.size();
    }

    public Dial getDialByIndex(int index) {
        try {
            return this.dials.get(index);
        } catch (IndexOutOfBoundsException e) {
            assert false;
            return null;
        }
    }

    public Dial getDialByName(String name) {
        for (Dial dial : dials) {
            if (dial.getName().equals(name)) {
                return dial;
            }
        }

        return null;
    }

    public boolean addDial(Dial dialToAdd) {
        /* 이미 동일한 다이얼을 가지고 있으면 추가하지 않는다.
         * 이미 동일한 다이얼이 있다면 false, 그렇지 않으면 true를 반환한다.
         */

        for (Dial addedDial : dials) {
            if (dialToAdd.equals(addedDial)) {
                return false;
            }
        }

        this.dials.add(dialToAdd);
        return true;
    }

    public int addDials(ArrayList<Dial> dialsToAdd) {
        /* 이미 동일한 다이얼을 가지고 있으면 추가하지 않는다.
         * 추가한 다이얼의 개수를 반환한다.
         */

        boolean hasSameDial;
        int addCount = 0;

        for (Dial dialToAdd : dialsToAdd) {
            hasSameDial = false;

            for (Dial addedDial : dials) {
                if (dialToAdd.equals(addedDial)) {
                    hasSameDial = true;
                    break;
                }
            }

            if (!hasSameDial) {
                this.dials.add(dialToAdd);
                ++addCount;
            }
        }

        return addCount;
    }

    public Dial removeDialByIndex(int index) {
        try {
            return this.dials.remove(index);
        } catch (IndexOutOfBoundsException e) {
            assert false;
            return null;
        }
    }

    public boolean removeDialByObject(Dial dial) {
        return this.dials.remove(dial);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.dials.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Category) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Category other = (Category) obj;
        return this.name.equals(other.name) && this.dials.equals(other.dials);
    }
}
