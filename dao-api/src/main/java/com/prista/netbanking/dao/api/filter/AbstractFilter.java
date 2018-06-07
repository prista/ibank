package com.prista.netbanking.dao.api.filter;

public abstract class AbstractFilter {

    private Boolean sortOrder;

    private String sortColumn;

    private Integer limit;

    private Integer offset;

    public Boolean getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(final Boolean sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(final String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(final Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(final Integer offset) {
        this.offset = offset;
    }
}
