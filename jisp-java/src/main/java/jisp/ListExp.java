package jisp;

import java.util.List;

public class ListExp extends JispExp {

    private final List<JispExp> list;

    public ListExp(List<JispExp> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "<ListExp: " + list.toString() + ">";
    }
}
