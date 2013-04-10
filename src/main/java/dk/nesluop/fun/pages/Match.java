package dk.nesluop.fun.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.spring.jpa.JpaGridDataSource;


import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Match {

    @Inject
    private EntityManagerFactory emf;

    @Component
    private Grid matchGrid;

/*
    @SetupRender
    public void setup() {
        List<SortConstraint> sort = matchGrid.getSortModel().getSortConstraints();
        if( sort.isEmpty() ){
            sort.add( new SortConstraint( ));
        }
    }
*/

    @Property
    private dk.nesluop.fun.entities.Match matchGridRow;

    public GridDataSource getMatches() {
        return new JpaGridDataSource<dk.nesluop.fun.entities.Match>( emf.createEntityManager(), dk.nesluop.fun.entities.Match.class ) {
            @Override
            public int getAvailableRows() {
                return super.getAvailableRows();
            }

            @Override
            protected void applyAdditionalConstraints(CriteriaQuery<?> criteria, Root<dk.nesluop.fun.entities.Match> root, CriteriaBuilder builder) {
                super.applyAdditionalConstraints(criteria, root, builder);
            }

            @Override
            public Object getRowValue(int index) {
                return super.getRowValue(index);
            }

            @Override
            public Class<dk.nesluop.fun.entities.Match> getRowType() {
                return super.getRowType();
            }

            @Override
            public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
                super.prepare(startIndex, endIndex, sortConstraints);
            }
        };
    }

}
