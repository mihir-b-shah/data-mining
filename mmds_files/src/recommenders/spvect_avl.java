package recommenders;

import java.util.ArrayDeque;
import java.util.HashSet;

/*
avoids autoboxing
also using an avl tree to optimize
since we're not doing much add/remove
its all itevalion and random access
also memory is more important here yeet 
thats why im not using a hashtable
*/

public class spvect_avl implements Comparable<spvect_avl> {

    private int key;
    private int size;
    private float val;
    private int h;
    private spvect_avl left;
    private spvect_avl right;

    public spvect_avl() {
    }

    private spvect_avl(int s, float r) {
        key = s;
        val = r;
        h = 1;
        size = 1;
    }
    
    @Override
    public int hashCode() {
        return key;
    }
    
    @Override
    public String toString() {
        return String.format("<%d, %.1f>", key, val);
    }
    
    @Override
    public boolean equals(Object oth) {
        if(oth instanceof spvect_avl) {
            return oth.hashCode() == hashCode();
        } else return false;
    }
    
    @Override
    public int compareTo(spvect_avl oth) {
        return key - oth.get_key();
    }

    private int get_key() {
        return key;
    }
    
    private class queue_item {
        final spvect_avl bt;
        final int h;
        final int x;
        
        public queue_item(spvect_avl bt, int h, int x) {
            this.bt = bt;
            this.h = h;
            this.x = x;
        }
        
        @Override
        public String toString() {
            return String.format("%1$s %2$d %3$d", 
                    bt.get_key(), h, x);
        }
    }
    
    public String print_tree(int cw) {
        StringBuilder sb = new StringBuilder();
        ArrayDeque<queue_item> queue = new ArrayDeque<>();

        queue.offer(new queue_item(this,0,0));
        
        int curr_height = -1;
        int curr_justif = 0;
        
        while (!queue.isEmpty()) {
            queue_item obj = queue.poll(); 
            
            if(curr_height != obj.h) {
                sb.append('\n');
                curr_height = obj.h;
                curr_justif = 0;
            }
            
            int count = obj.bt.toString().length() >> 1;
            int amt = convert(cw, obj.h, obj.x);
            for(int i = 0; i<amt-curr_justif-count; ++i) {
                sb.append(' ');
            }
            
            sb.append(obj.bt.toString());
            curr_justif = amt;
            
            if(obj.bt.has_left()) {
                queue.offer(new queue_item((spvect_avl) obj.bt.get_left(),
                        obj.h+1,obj.x << 1));
            }
            
            if(obj.bt.has_right()) {
                queue.offer(new queue_item((spvect_avl) obj.bt.get_right(),
                        obj.h+1,(obj.x << 1)+1));
            }
        }
        
        return sb.toString();
    }
    
    private int convert(int w, int h, int j) {
        return (int) (w*((j << 1)+1f)/(1 << (h+1)));
    }
    
    private float get_val() {
        return val;
    }

    private int get_height() {
        return h;
    }

    private void set_height(int h1) {
        h = h1;
    }

    private spvect_avl copy_constr() {
        return new spvect_avl(key, val);
    }

    private spvect_avl get_left() {
        return left;
    }

    private spvect_avl get_right() {
        return right;
    }
    
    private boolean has_left() {
        return left != null;
    }

    private boolean has_right() {
        return right != null;
    }
    
    private void set_left(spvect_avl l) {
        left = l;
    }

    private void set_right(spvect_avl r) {
        right = r;
    }

    public float sum() {
        return val + (has_left() ?  left.sum() : 0)
                + (has_right() ? right.sum() : 0);
    }

    public static float dot(spvect_avl sv1, spvect_avl sv2) {
        float sum = 0;

        ArrayDeque<spvect_avl> deque_1 = new ArrayDeque<>();
        ArrayDeque<spvect_avl> deque_2 = new ArrayDeque<>();
        
        final HashSet<spvect_avl> set_1 = new HashSet<>();
        final HashSet<spvect_avl> set_2 = new HashSet<>();

        deque_1.push(sv1);
        deque_2.push(sv2);
        set_1.add(sv1);
        set_2.add(sv2);

        while (!deque_1.isEmpty() && !deque_2.isEmpty()) {
            int cto = deque_1.peek().compareTo(deque_2.peek());
            if (cto == 0) {
                spvect_avl t1 = deque_1.poll();
                spvect_avl t2 = deque_2.poll();
                if(t1.has_left()) {
                    deque_1.offer(t1.get_left());
                }
                if(t1.has_right()) {
                    deque_1.offer(t1.get_right());
                }
                if(t2.has_left()) {
                    deque_2.offer(t2.get_left());
                }
                if(t2.has_right()) {
                    deque_2.offer(t2.get_right());
                }
                sum += t1.get_val() * t2.get_val();
            } else if (cto > 0) {
                spvect_avl t = deque_1.pop();
                if(t.has_left() && !set_1.contains(t.get_left())) {
                    deque_1.push(t.get_left());
                    set_1.add(t.get_left());
                } else {
                    spvect_avl temp = deque_2.poll();
                    if(temp.has_left() && !set_2.contains(temp.get_left())) {
                        deque_2.push(temp.get_left());
                    }
                    if(temp.has_right() && !set_2.contains(temp.get_right())) {
                        deque_2.push(temp.get_right());
                    }
                }
                deque_1.offer(t);
                if(t.has_right() && !set_1.contains(t.get_right())) {
                    deque_1.offer(t.get_right());
                    set_1.add(t.get_right());
                }
            } else {
                spvect_avl t = deque_2.pop();
                if(t.has_left() && !set_2.contains(t.get_left())) {
                    deque_2.push(t.get_left());
                    set_2.add(t.get_left());
                } else {
                    spvect_avl temp = deque_1.poll();
                    if(temp.has_left() && !set_1.contains(temp.get_left())) {
                        deque_1.push(temp.get_left());
                    }
                    if(temp.has_right() && !set_1.contains(temp.get_right())) {
                        deque_1.push(temp.get_right());
                    }
                }
                deque_2.offer(t);
                if(t.has_right() && !set_2.contains(t.get_right())) {
                    deque_2.offer(t.get_right());
                    set_2.add(t.get_right());
                }
            }
        }

        return sum;
    }

    private void norm_recur(float avg) {
        val -= avg;
        if (has_left()) {
            left.norm_recur(avg);
        }
        if (has_right()) {
            right.norm_recur(avg);
        }
    }

    public void normalize() {
        float avg = sum() / size;
        norm_recur(avg);
    }

    private int get_balance() {
        return (has_left() ? left.get_height() : 0)
                - (has_right() ? right.get_height() : 0);
    }

    public float get(int s) {
        spvect_avl ref = this;
        while (ref != null) {
            int cto = ref.get_key()-s;
            if (cto == 0) {
                return ref.get_val();
            } else if (cto > 0) {
                ref = ref.get_right();
            } else {
                ref = ref.get_left();
            }
        }

        return Float.NaN;
    }

    private void left_rotate() {
        spvect_avl aux = copy_constr();
        aux.set_left(right.get_left());
        aux.set_right(left);
        left = aux;
        key = right.get_key();
        val = right.get_val();
        right = right.get_right();

        h = 1 + Math.max(left.get_height(), right.get_height());
        left.set_height(1 + Math.max(left.has_left() ? 
                left.get_left().get_height() : 0,
                left.has_right() ? left.get_right().get_height() : 0));
    }

    private void right_rotate() {
        spvect_avl aux = copy_constr();
        aux.set_height(h);
        aux.set_left(left.get_right());
        aux.set_right(right);
        right = aux;
        key = left.get_key();
        val = left.get_val();
        left = left.get_left();

        h = 1 + Math.max(left.get_height(), right.get_height());
        right.set_height(1 + Math.max(right.has_left() ? 
                right.get_left().get_height() : 0,
                right.has_right() ? right.get_right().get_height() : 0));
    }

    public void add(int s, float r) {
        if (size == 0) {
            key = s;
            val = r;
            ++size;
            h = 1;
            return;
        }

        int cto = s-key;
        if (cto == 0) {
            return;
        } else if (cto > 0) {
            if (has_right()) {
                right.add(s, r);
            } else {
                right = new spvect_avl(s, r);
                ++size;
            }

            h = 1 + Math.max(has_right() ? right.get_height() : 0,
                    right.has_left() ? left.get_height() : 0);

        } else {
            if (has_left()) {
                left.add(s, r);
            } else {
                left = new spvect_avl(s, r);
                ++size;
            }

            h = 1 + Math.max(has_left() ? left.get_height() : 0,
                    left.has_right() ? right.get_height() : 0);
        }

        if (Math.abs(get_balance()) == 2) {
            // case 1: single rotation, left-left
            if (has_left() && left.has_left()
                    && left.get_left().get_key() == s) {
                right_rotate();
            } // case 2: single rotation, right-right
            else if (has_right() && right.has_right()
                    && right.get_right().get_key() == s) {
                left_rotate();
            } // case 3: double rotation, left-right
            else if (has_left() && left.has_right()
                    && left.get_right().get_key() == s) {
                left.left_rotate();
                right_rotate();
            } // case 4: double rotation, right-left
            else if (has_right() && right.has_left()
                    && right.get_left().get_key() == s) {
                right.right_rotate();
                left_rotate();
            }
        }
    }
}
