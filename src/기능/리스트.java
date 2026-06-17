package 기능;
import 정보.정보;

// C 언어에서 옮겨온 코드로, 자료구조 강의의 리스트 ppt를 참조하였습니다.
// head 포인터 대신 Node 그 자체를 파라미터로 받도록 한 것과, ppt에서의 listType를 리스트 클래스로 구현한 것 외에는 C 코드와 거의 동일하게 작성했습니다.
// Java에서는 Node 그 자체를 받더라도, Call by Reference로 동작하기 때문에, C에서 포인터를 사용했던 것과 유사한 방식으로 리스트를 조작할 수 있습니다.

// 리스트의 메서드들은 C에선 main 위에서 함수로 정의되어 있었으나, Java에선 public static으로 선언함으로써 같은 효과를 낼 수 있습니다.

// 기본적으로 Node를 비롯한 필드들은 private로 캡슐화 되어 있습니다.

public class 리스트 {

    // 리스트의 필드들
    private Node head;
    private Node tail;
    private int length;

    // 리스트의 노드 클래스
    private static class Node { // private으로 선언하여 외부에서 직접 접근할 수 없도록 합니다.

        // 노드는 데이터 필드와 링크 필드로 구성됩니다.
        private 정보 data;
        private Node next;

        // 노드의 생성자
        public Node() {                         // 기본 생성자에서는 데이터와 링크를 null로 초기화합니다.
            this.data = null;
            this.next = null;
        }
        @SuppressWarnings("unused")
        public Node(정보 data) {                // 데이터가 주어진 생성자에서는 링크를 null로 초기화하고 데이터를 설정합니다.
            this();
            this.data = data;
        }
        @SuppressWarnings("unused")
        public Node(Node next) {                // 링크가 주어진 생성자에서는 데이터를 null로 초기화하고 링크를 설정합니다.
            this();
            this.next = next;
        }
        public Node(정보 data, Node next) {     // 데이터와 링크가 주어진 생성자에서는 데이터를 설정하고 링크를 설정합니다.
            this.data = data;
            this.next = next;
        }
    }


    // 리스트의 생성자
    public 리스트() {
        head = null;    // 리스트의 시작 노드
        tail = null;    // 리스트의 마지막 노드
        length = 0;     // 리스트의 길이
    }


    // 리스트의 상태를 확인하는 메소드들

    // 리스트의 가장 기초적인 메소드들
    public static boolean isEmpty(리스트 list)  { return list.length == 0; }                // 리스트가 비어있는지 확인합니다. length가 0이면 true를 반환합니다.
    public static boolean isFull(리스트 list)   { return false; }                           // 따로 한계를 두지 않으므로 항상 false를 반환합니다.
    public static Node create_node(정보 data, Node next)   { return new Node(data, next); } // 새로운 노드를 생성하여 반환합니다.
                                                                                            // java에서는 따로 malloc과 free가 필요 없으므로 생성자로 노드를 생성하고, 가비지 컬렉터가 자동으로 메모리를 관리합니다.

    // 리스트의 노드를 삽입하고 제거하는 메소드들
    public static void insert_node(리스트 list, Node before, Node new_node) {
        if (new_node == null) return;                           // new_node가 null인 경우, 삽입할 노드가 없으므로 메소드를 종료합니다.
        if (isFull(list)) return;                          // 리스트가 가득 찬 경우, 삽입할 공간이 없으므로 메소드를 종료합니다. (실제로는 항상 false를 반환하므로 이 조건은 항상 통과합니다.)
        if (isEmpty(list)) {                               // 리스트가 비어있는 경우, new_node가 head와 tail이 됩니다.
            list.head = new_node;
            list.tail = new_node;
        }
        else if (before == null) {                              // before가 null이면 첫번째 노드로 new_node를 삽입합니다.
            new_node.next = list.head;
            list.head = new_node;
        }
        else {                                                  // before가 null이 아니면 before 노드 뒤에 new_node를 삽입합니다.
            new_node.next = before.next;
            before.next = new_node;
            if (before == list.tail) list.tail = new_node;      // before 노드가 tail인 경우, tail을 new_node로 업데이트합니다.
        }
        list.length++;                                            // 리스트의 길이를 증가시킵니다.
    }
    public static void remove_node(리스트 list, Node before, Node removed) {
        if (removed == null) return;                            // removed가 null인 경우, 제거할 노드가 없으므로 메소드를 종료합니다.
        if (isEmpty(list)) return;                         // 리스트가 비어있는 경우, 제거할 노드가 없습니다.
        if (before == null) {                                   // before가 null이면 리스트의 맨 앞의 노드를 제거합니다.
            list.head = removed.next;
            if (removed == list.tail) list.tail = null;         // 리스트에 노드가 하나만 있는 경우, head와 tail이 모두 null이 됩니다.
        }
        
        else {                                                  // before가 null이 아니면 before 노드 뒤의 removed 노드를 제거합니다.
            before.next = removed.next;
            if (removed == list.tail) list.tail = before;       // removed 노드가 tail인 경우, tail을 before로 업데이트합니다.
        }
        list.length--;                                            // 리스트의 길이를 감소시킵니다.
    }

    // 리스트를 순회하는 메소드들
    public static void display(리스트 list) {                   // 리스트의 모든 노드를 순회하며 데이터를 출력하는 메소드입니다.
        Node current = list.head;
        while (current != null) {                               // 리스트의 시작 노드부터 시작하여 current가 null이 될 때까지 반복합니다.
            System.out.print("->" + current.data);
            current = current.next;
        }
        System.out.println();
    }
    public static void display_recursive(Node head) {           // display의 재귀 버전입니다.
        if (head != null) {
            System.out.print("->" + head.data);
            display_recursive(head.next);
        }
    }
    public static Node get_node_at(리스트 list, int position) { // 리스트에서 position 위치의 요소를 검색하는 메소드입니다.
        int index = 0;
        Node current = list.head;
        while (current != null && index < position) {           // 리스트의 시작 노드부터 시작하여 current가 null이 될 때까지 반복합니다.
            current = current.next;
            index++;
        }
        return current;                                         // position 위치의 노드를 반환합니다.
    }
    public static 정보 get_entry(리스트 list, int position) {   // 리스트에서 position 위치의 요소를 검색하는 메소드입니다.
        Node node = null;
        if((position < 0) || (position >= list.length)) {       // position이 유효한 범위 내에 있는지 확인합니다.
            System.out.println("위치 오류");
        }
        node = get_node_at(list, position);                     // position 위치의 노드를 가져옵니다.
        return node != null ? node.data : null;                 // 노드가 null이 아닌 경우, 노드의 데이터를 반환합니다.
    }
    public static boolean is_in_list(리스트 list, 정보 item) {  // 리스트에 item 요소가 있는지 확인하는 메소드입니다.
        Node current = list.head;
        while (current != null) {                               // 리스트의 시작 노드부터 시작하여 current가 null이 될 때까지 반복합니다.
            if (current.data.equals(item)) return true;         // equals 메소드를 사용하여 객체의 내용을 비교합니다. item이 리스트에 있으면 true를 반환합니다.
            current = current.next;
        }
        return false;                                           // 리스트에 item이 없는 경우 false를 반환합니다.
    }
    public static int get_element_index(리스트 list, 정보 item) {// 리스트에서 item 요소의 인덱스를 검색하는 메소드입니다.
        Node current = list.head;
        int position = 0;
        while (current != null) {                               // 리스트의 시작 노드부터 시작하여 current가 null이 될 때까지 반복합니다.
            if (current.data.equals(item)) return position;     // equals 메소드를 사용하여 객체의 내용을 비교합니다.
            current = current.next;
            position++;
        }
        return -1;                                              // 리스트에 item이 없는 경우 -1을 반환합니다.
    }


    // ppt에서 소개된 다른 메서드, 잘 쓰이지 않는다.
    public static 리스트 concat(리스트 list1, 리스트 list2) {   // list1과 list2를 연결하여 새로운 리스트를 반환하는 메소드입니다.
        if (isEmpty(list1)) return list2;                       // list1이 비어있는 경우, list2를 반환합니다.
        if (isEmpty(list2)) return list1;                       // list2가 비어있는 경우, list1을 반환합니다.

        리스트 new_list = new 리스트();                         // 새로운 리스트를 생성합니다.

        new_list.head = list1.head;                             // new_list의 head는 list1의 head가 됩니다.
        list1.tail.next = list2.head;                           // list1의 tail의 next는 list2의 head가 됩니다.
        new_list.tail = list2.tail;                             // new_list의 tail은 list2의 tail이 됩니다.

        new_list.length = list1.length + list2.length;          // new_list의 length는 list1과 list2의 length의 합이 됩니다.
        return new_list;                                        // 연결된 리스트를 반환합니다.
    }
    public static 리스트 reverse(리스트 list) {                 // 리스트를 역순으로 뒤집는 메소드입니다.
        Node lead, middle, trail;
        lead = list.head;
        middle = null;
        while (lead != null) {                                  // 리스트의 시작 노드부터 시작하여 lead가 null이 될 때까지 반복합니다.
            trail = middle;                                     // middle을 trail로 이동합니다.
            middle = lead;                                      // lead를 middle로 이동합니다.
            lead = lead.next;                                   // lead를 다음 노드로 이동합니다.
            middle.next = trail;                                // middle의 next를 trail로 설정하여 링크를 뒤집습니다.
        }
        list.head = middle;                                     // C에서는 새 head인 middle을 반환했지만, Java에서는 리스트 객체의 head를 middle로 업데이트하고 리스트 객체 자체를 반환합니다.
        return list;
    }

    // 리스트의 ADT를 구현하는 메소드들
    public static int get_length(리스트 list) { return list.length; }// 리스트의 길이를 반환하는 메소드입니다.
    public static 리스트 init(리스트 list) {                     // 리스트를 초기화하는 메소드입니다. 리스트의 head와 tail을 null로 설정하고 length를 0으로 초기화합니다.
        if (list == null) return new 리스트();                   // list가 null인 경우, 새로운 리스트를 생성하여 반환합니다.
        list.head = null;
        list.tail = null;
        list.length = 0;
        return list;                                            // 초기화된 리스트를 반환합니다.
    }
    public static void add(리스트 list, int position, 정보 data) {// 리스트의 position 위치에 data를 삽입하는 메소드입니다.
        Node before = null;
        if ((position >= 0) && (position <= list.length)) {     // position이 유효한 범위 내에 있는지 확인합니다.
            Node node = create_node(data, null);
            before = get_node_at(list, position);
            insert_node(list, before, node);
            list.length++;
        }
    }
    public static void delete(리스트 list, int position) {
        if (!isEmpty(list) && (position >= 0) && (position < list.length)) { // position이 유효한 범위 내에 있는지 확인합니다.
            Node before = get_node_at(list, position - 1);
            Node removed = get_node_at(list, position);
            remove_node(list, before, removed);
            list.length--;
        }
    }
    public static void clear(리스트 list) {
        while (!isEmpty(list)) {                                // 리스트가 비어있지 않은 동안 반복합니다.
            delete(list, list.length - 1);
        }
    }

    public static void add_first(리스트 list, 정보 data) { add(list, 0, data); } // 리스트의 맨 앞에 data를 삽입하는 메소드입니다.
    public static void add_last(리스트 list, 정보 data) { add(list, list.length, data); } // 리스트의 맨 뒤에 data를 삽입하는 메소드입니다.
}