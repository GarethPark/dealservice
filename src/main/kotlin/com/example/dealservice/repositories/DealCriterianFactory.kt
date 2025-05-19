interface DealCriteriaFactory {
    fun gethPath(field: FilterField, root: Root<Deal>): Path<*>

    fun getPredicate(
        field: FilterField,
        cb: CriteriaBuilder,
        path: Path<*>,
        operator: ComparisonOperator
        value: Any?    
    ): Predicate
}