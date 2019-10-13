
public class link_list {

    private node end;
    private node ptr;
    private final node st;

    public link_list() {
        st = new node();
        ptr = st;
        end = st;
    }

    public void add(int dat) {
        end.set_data(dat);
        end.set_ptr(new node());
        end = end.get_ptr();
    }

    public void add_at(int dat) {
        node aux = new node();
        aux.set_data(dat);
        node pptr = ptr.get_ptr();
        aux.set_ptr(pptr);
        ptr.set_ptr(pptr);
    }

    public void move_ptr() {
        ptr = ptr.get_ptr();
    }

    public void set_st() {
        ptr = st;
    }

    private class node {

        private node ptr;
        private int data;

        public node() {
        }

        public void set_data(int dat) {
            data = dat;
        }

        public node get_ptr() {
            return ptr;
        }

        public void set_ptr(node o) {
            ptr = o;
        }
    }
}
