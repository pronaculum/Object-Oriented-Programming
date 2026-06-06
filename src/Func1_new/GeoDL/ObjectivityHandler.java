package GeoDL;

/**
 * 생성된 기하 객체를 저장하고 삭제하는 관리 클래스입니다.
 */
public class ObjectivityHandler {
    private Objectivity[] objects;
    private int objectCount;
    private int nextId;

    public ObjectivityHandler() {
        this(100);
    }

    public ObjectivityHandler(int capacity) {
        if (capacity <= 0) {
            capacity = 100;
        }

        objects = new Objectivity[capacity];
        objectCount = 0;
        nextId = 1;
    }

    public boolean addObject(Objectivity object) {
        if (object == null) {
            return false;
        }

        if (objectCount >= objects.length) {
            return false;
        }

        object.setId(nextId);
        nextId++;

        objects[objectCount] = object;
        objectCount++;

        return true;
    }

    public boolean removeObject(Objectivity object) {
        int index = findObjectIndex(object);

        if (index == -1) {
            return false;
        }

        for (int i = index; i < objectCount - 1; i++) {
            objects[i] = objects[i + 1];
        }

        objects[objectCount - 1] = null;
        objectCount--;

        return true;
    }

    public boolean containsObject(Objectivity object) {
        return findObjectIndex(object) != -1;
    }

    public int findObjectIndex(Objectivity object) {
        if (object == null) {
            return -1;
        }

        for (int i = 0; i < objectCount; i++) {
            if (objects[i] == object) {
                return i;
            }
        }

        return -1;
    }

    public Objectivity findObjectById(int id) {
        for (int i = 0; i < objectCount; i++) {
            if (objects[i].getId() == id) {
                return objects[i];
            }
        }

        return null;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public int getCapacity() {
        return objects.length;
    }

    public Objectivity getObject(int index) {
        if (index < 0 || index >= objectCount) {
            return null;
        }

        return objects[index];
    }

    public Objectivity[] getObjects() {
        Objectivity[] result = new Objectivity[objectCount];

        for (int i = 0; i < objectCount; i++) {
            result[i] = objects[i];
        }

        return result;
    }
}
