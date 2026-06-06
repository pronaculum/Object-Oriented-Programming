package Func1;

import GeoDL.Objectivity;
import java.util.List;

/**
 * 모든 제약조건이 반드시 따라야 하는 공통 규격입니다.
 */
public interface Constraint {
    boolean isSatisfied();

    void resolve();

    List<Objectivity> getConstrainedObjects();

    String getConstraintType();
}
