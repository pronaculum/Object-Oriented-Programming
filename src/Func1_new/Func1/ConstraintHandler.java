package Func1;

import GeoDL.Objectivity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 제약조건을 ID로 등록, 삭제, 조회, 일괄 처리하는 클래스입니다.
 */
public class ConstraintHandler {
    private Map<Integer, Constraint> constraintMap;
    private int nextId;

    public ConstraintHandler() {
        constraintMap = new HashMap<Integer, Constraint>();
        nextId = 1;
    }

    public int addConstraint(Constraint constraint) {
        if (constraint == null) {
            return -1;
        }

        int id = nextId;
        nextId++;

        constraintMap.put(id, constraint);
        return id;
    }

    public boolean removeConstraint(int id) {
        if (!constraintMap.containsKey(id)) {
            return false;
        }

        constraintMap.remove(id);
        return true;
    }

    public int removeConstraintsBetween(Objectivity object1, Objectivity object2) {
        Integer[] removeIds = new Integer[constraintMap.size()];
        int removeCount = 0;

        for (Integer id : constraintMap.keySet()) {
            Constraint constraint = constraintMap.get(id);
            List<Objectivity> objects = constraint.getConstrainedObjects();

            if (objects.contains(object1) && objects.contains(object2)) {
                removeIds[removeCount] = id;
                removeCount++;
            }
        }

        for (int i = 0; i < removeCount; i++) {
            constraintMap.remove(removeIds[i]);
        }

        return removeCount;
    }

    public void resolveAll() {
        for (Constraint constraint : constraintMap.values()) {
            if (!constraint.isSatisfied()) {
                constraint.resolve();
            }
        }
    }

    public void cleanUpForDeletedObject(Objectivity deletedObject) {
        Integer[] removeIds = new Integer[constraintMap.size()];
        int removeCount = 0;

        for (Integer id : constraintMap.keySet()) {
            Constraint constraint = constraintMap.get(id);
            List<Objectivity> objects = constraint.getConstrainedObjects();

            if (objects.contains(deletedObject)) {
                removeIds[removeCount] = id;
                removeCount++;
            }
        }

        for (int i = 0; i < removeCount; i++) {
            constraintMap.remove(removeIds[i]);
        }
    }

    public Constraint getConstraint(int id) {
        return constraintMap.get(id);
    }

    public int getConstraintCount() {
        return constraintMap.size();
    }

    public Constraint[] getConstraints() {
        Constraint[] result = new Constraint[constraintMap.size()];
        int index = 0;

        for (Constraint constraint : constraintMap.values()) {
            result[index] = constraint;
            index++;
        }

        return result;
    }

    public int[] getConstraintIds() {
        int[] result = new int[constraintMap.size()];
        int index = 0;

        for (Integer id : constraintMap.keySet()) {
            result[index] = id;
            index++;
        }

        return result;
    }
}
