package GeoDL;

/**
 * Information 타입의 데이터를 담는 단일 연결 리스트(Singly-Linked List).
 *
 * 역할:
 *   - Line이 두 Point 참조를 순서대로 보관하는 데 사용된다 (pointList).
 *   - Point가 자신이 참여하는 관계 목록을 역참조로 보관하는 데 사용된다 (relationList).
 *   - 모든 연산이 정적(static) 메서드 형태 — GeoList 인스턴스를 매개변수로 전달하는 C 스타일 ADT 구조.
 *
 * 버그 수정 이력 (기존 한글 리스트.java 대비):
 *   - add():    position 0 삽입 시 head 처리 오류 수정. insertNode() 내부에서 이미 length++를
 *               수행하므로 중복 증가 제거.
 *   - delete(): removeNode() 내부에서 이미 length--를 수행하므로 중복 감소 제거.
 *
 * 캡슐화:
 *   - Node 클래스는 private static inner class — 외부에서 직접 노드를 조작할 수 없다.
 *   - getNodeAt(), createNode()는 package-private — 같은 패키지의 GeoList 자신만 사용.
 */
public class GeoList {

    private Node head;   // 연결 리스트의 첫 번째 노드
    private Node tail;   // 연결 리스트의 마지막 노드 (addLast 최적화에 활용)
    private int length;  // 현재 저장된 노드 수

    /** 내부 노드 구조 — 외부 직접 접근 차단 (private static) */
    private static class Node {
        private Information data;
        private Node next;

        Node() {
            this.data = null;
            this.next = null;
        }
        @SuppressWarnings("unused")
        Node(Information data) {
            this();
            this.data = data;
        }
        @SuppressWarnings("unused")
        Node(Node next) {
            this();
            this.next = next;
        }
        Node(Information data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    /** 빈 연결 리스트를 생성한다. */
    public GeoList() {
        head = null;
        tail = null;
        length = 0;
    }

    // ── 상태 조회 ──────────────────────────────────────────────────────────────

    public static boolean isEmpty(GeoList list)   { return list.length == 0; }
    /** 배열 기반이 아닌 연결 리스트이므로 항상 false를 반환 (용량 무제한). */
    public static boolean isFull(GeoList list)    { return false; }
    public static int     getLength(GeoList list)  { return list.length; }

    // ── 저수준 노드 연산 (package-private) ─────────────────────────────────────

    /** 새 노드를 생성한다. package-private — 내부 연산 전용. */
    static Node createNode(Information data, Node next) { return new Node(data, next); }

    /**
     * before 노드 뒤에 newNode를 삽입한다.
     * before == null이면 리스트의 맨 앞(head 앞)에 삽입.
     * 삽입 완료 후 length를 1 증가시킨다.
     */
    public static void insertNode(GeoList list, Node before, Node newNode) {
        if (newNode == null) return;
        if (isFull(list)) return;
        if (isEmpty(list)) {
            // 리스트가 비어 있으면 head와 tail 모두 newNode로 설정
            list.head = newNode;
            list.tail = newNode;
        } else if (before == null) {
            // head 앞에 삽입 (새 head가 됨)
            newNode.next = list.head;
            list.head = newNode;
        } else {
            // before 뒤에 삽입
            newNode.next = before.next;
            before.next = newNode;
            if (before == list.tail) list.tail = newNode; // tail 갱신
        }
        list.length++;
    }

    /**
     * removed 노드를 리스트에서 제거한다.
     * before == null이면 head 노드를 제거.
     * 제거 완료 후 length를 1 감소시킨다.
     */
    public static void removeNode(GeoList list, Node before, Node removed) {
        if (removed == null) return;
        if (isEmpty(list)) return;
        if (before == null) {
            // head 노드 제거
            list.head = removed.next;
            if (removed == list.tail) list.tail = null; // 단일 노드였던 경우
        } else {
            before.next = removed.next;
            if (removed == list.tail) list.tail = before; // tail 갱신
        }
        list.length--;
    }

    // ── 탐색 및 출력 ───────────────────────────────────────────────────────────

    /** 리스트의 모든 요소를 "->" 형식으로 콘솔에 출력한다. */
    public static void display(GeoList list) {
        Node current = list.head;
        while (current != null) {
            System.out.print("->" + current.data);
            current = current.next;
        }
        System.out.println();
    }

    /** 재귀 방식으로 리스트를 출력한다. */
    public static void displayRecursive(Node head) {
        if (head != null) {
            System.out.print("->" + head.data);
            displayRecursive(head.next);
        }
    }

    /**
     * 0-기반 인덱스로 해당 위치의 노드를 반환한다.
     * package-private — add(), delete() 내부 로직 전용.
     */
    static Node getNodeAt(GeoList list, int position) {
        int index = 0;
        Node current = list.head;
        while (current != null && index < position) {
            current = current.next;
            index++;
        }
        return current;
    }

    /**
     * 0-기반 인덱스로 해당 위치의 데이터(Information)를 반환한다.
     * 범위를 벗어나면 null을 반환하고 경고 메시지를 출력한다.
     */
    public static Information getEntry(GeoList list, int position) {
        if ((position < 0) || (position >= list.length)) {
            System.out.println("Position out of bounds");
            return null;
        }
        Node node = getNodeAt(list, position);
        return node != null ? node.data : null;
    }

    /** 주어진 item과 같은(equals) 데이터가 리스트에 있는지 확인한다. */
    public static boolean isInList(GeoList list, Information item) {
        Node current = list.head;
        while (current != null) {
            if (current.data.equals(item)) return true;
            current = current.next;
        }
        return false;
    }

    /** 주어진 item의 0-기반 인덱스를 반환한다. 없으면 -1. */
    public static int getElementIndex(GeoList list, Information item) {
        Node current = list.head;
        int position = 0;
        while (current != null) {
            if (current.data.equals(item)) return position;
            current = current.next;
            position++;
        }
        return -1;
    }

    // ── ADT 고수준 연산 ────────────────────────────────────────────────────────

    /** 리스트를 초기 빈 상태로 리셋한다. null이면 새 인스턴스를 반환. */
    public static GeoList init(GeoList list) {
        if (list == null) return new GeoList();
        list.head = null;
        list.tail = null;
        list.length = 0;
        return list;
    }

    /**
     * 0-기반 position에 data를 삽입한다.
     * 수정 이력: position 0에서 head 앞 삽입이 올바르게 동작하도록 수정.
     *            insertNode()가 이미 length++를 수행하므로 중복 증가 없음.
     */
    public static void add(GeoList list, int position, Information data) {
        if (position < 0 || position > list.length) return;
        Node node = createNode(data, null);
        if (position == 0) {
            insertNode(list, null, node); // head 앞에 삽입
        } else {
            Node before = getNodeAt(list, position - 1);
            insertNode(list, before, node);
        }
        // insertNode가 이미 length++를 수행함 — 여기서 추가 증가 없음
    }

    /**
     * 0-기반 position의 노드를 제거한다.
     * 수정 이력: removeNode()가 이미 length--를 수행하므로 중복 감소 없음.
     */
    public static void delete(GeoList list, int position) {
        if (isEmpty(list) || position < 0 || position >= list.length) return;
        Node before  = (position == 0) ? null : getNodeAt(list, position - 1);
        Node removed = getNodeAt(list, position);
        removeNode(list, before, removed);
        // removeNode가 이미 length--를 수행함 — 여기서 추가 감소 없음
    }

    /** 리스트의 모든 요소를 제거하여 빈 상태로 만든다. */
    public static void clear(GeoList list) {
        while (!isEmpty(list)) {
            delete(list, list.length - 1);
        }
    }

    /** 리스트의 맨 앞에 데이터를 삽입한다. */
    public static void addFirst(GeoList list, Information data) { add(list, 0, data); }

    /** 리스트의 맨 뒤에 데이터를 삽입한다. */
    public static void addLast(GeoList list, Information data)  { add(list, list.length, data); }

    /**
     * 두 리스트를 연결하여 새 리스트를 반환한다.
     * 주의: 원본 리스트의 내부 포인터가 변경되므로 원본을 이후에 사용하면 안 된다.
     */
    public static GeoList concat(GeoList list1, GeoList list2) {
        if (isEmpty(list1)) return list2;
        if (isEmpty(list2)) return list1;
        GeoList newList = new GeoList();
        newList.head = list1.head;
        list1.tail.next = list2.head;
        newList.tail = list2.tail;
        newList.length = list1.length + list2.length;
        return newList;
    }

    /**
     * 리스트를 역순으로 뒤집어 반환한다.
     * 3-포인터 순회 알고리즘(lead, middle, trail)을 사용하여 O(N) 복잡도로 뒤집는다.
     */
    public static GeoList reverse(GeoList list) {
        Node lead = list.head, middle = null, trail;
        while (lead != null) {
            trail  = middle;
            middle = lead;
            lead   = lead.next;
            middle.next = trail;
        }
        list.head = middle;
        return list;
    }
}
