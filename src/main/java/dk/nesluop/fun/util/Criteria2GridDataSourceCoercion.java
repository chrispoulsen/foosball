package dk.nesluop.fun.util;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.services.Coercion;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

public class Criteria2GridDataSourceCoercion implements Coercion<CriteriaQuery, GridDataSource> {

    private EntityManager em;
    private static volatile long aliasCount = 0;

    public Criteria2GridDataSourceCoercion(EntityManager em) {
        this.em = em;
    }

    public GridDataSource coerce(final CriteriaQuery criteriaQuery) {
        return new GridDataSource() {

            private List<?> cache;
            private int startIndex;

            public int getAvailableRows() {
                return count(criteriaQuery).intValue();
            }

            public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public Object getRowValue(int index) {
                return cache.get(index - startIndex);
            }

            public Class getRowType() {
                if (cache == null)
                {
//                    setupCriteria(null, 0, 1);
//                    criteria.setResultTransformer(Criteria.ROOT_ENTITY);
//                    cache = criteria.list();
                }

                if (cache.size() != 0) {
                    return cache.get(0).getClass();
                }

                return null;
            }


            private <T> Long count(CriteriaQuery<T> criteria) {
                return em.createQuery(countCriteria(criteria)).getSingleResult();
            }

            /**
             * Create a row count CriteriaQuery from a CriteriaQuery
             * @param criteria source criteria
             * @return row coutnt CriteriaQuery
             */
            private <T> CriteriaQuery<Long> countCriteria(CriteriaQuery<T> criteria) {
                CriteriaBuilder builder = em.getCriteriaBuilder();
                CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
                copyCriteriaNoSelection(criteria, countCriteria);
                countCriteria.select(builder.count(findRoot(countCriteria, criteria.getResultType())));

                return countCriteria;
            }

            /**
             * Find the Root with type class on CriteriaQuery Root Set
             * @param <T> root type
             * @param query criteria query
             * @param clazz root type
             * @return Root<T> of null if none
             */
            private <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {

                for (Root<?> r : query.getRoots()) {
                    if (clazz.equals(r.getJavaType())) {
                        return (Root<T>) r.as(clazz);
                    }
                }
                return null;
            }

            /**
             * Copy Criteria without Selection
             * @param from source Criteria
             * @param to destination Criteria
             */
            private void  copyCriteriaNoSelection(CriteriaQuery<?> from, CriteriaQuery<?> to) {

                // Copy Roots
                for (Root<?> root : from.getRoots()) {
                    Root<?> dest = to.from(root.getJavaType());
                    dest.alias(getOrCreateAlias(root));
                    copyJoins(root, dest);
                }

                to.groupBy(from.getGroupList());
                to.distinct(from.isDistinct());
                to.having(from.getGroupRestriction());
                to.where(from.getRestriction());
                to.orderBy(from.getOrderList());
            }

            /**
             * Gets The result alias, if none set a default one and return it
             * @param selection
             * @return root alias or generated one
             */
            private synchronized <T> String getOrCreateAlias(Selection<T> selection) {
                // reset alias count
                if (aliasCount > 1000)
                    aliasCount = 0;

                String alias = selection.getAlias();
                if (alias == null) {
                    alias = "JDAL_generatedAlias" + aliasCount++;
                    selection.alias(alias);
                }
                return alias;
            }
            /**
             * Copy Joins
             * @param from source Join
             * @param to destination Join
             */
            private void copyJoins(From<?, ?> from, From<?, ?> to) {
                for (Join<?, ?> j : from.getJoins()) {
                    Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
                    toJoin.alias(getOrCreateAlias(j));

                    copyJoins(j, toJoin);
                }

                for (Fetch<?, ?> f : from.getFetches()) {
                    Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
                    copyFetches(f, toFetch);

                }
            }

            /**
             * Copy Fetches
             * @param from source Fetch
             * @param to dest Fetch
             */
             private void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
                for (Fetch<?, ?> f : from.getFetches()) {
                    Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
                    // recursively copy fetches
                    copyFetches(f, toFetch);
                }
             }
        };
    }
}
