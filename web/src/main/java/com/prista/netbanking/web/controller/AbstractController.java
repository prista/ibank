package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.AbstractFilter;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.list.SortDTO;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController<DTO> {

    protected ListDTO<DTO> getListDTO(final HttpServletRequest req) {
        final String sessionModelName = getClass().getSimpleName() + "_LIST_MODEL";

        ListDTO<DTO> listModel = (ListDTO<DTO>) req.getSession().getAttribute(sessionModelName);
        if (listModel == null) {
            listModel = new ListDTO<>();
            req.getSession().setAttribute(sessionModelName, listModel);
        }
        return listModel;
    }

    // build filter
    protected void applySortAndOrder2Filter(ListDTO<DTO> listDTO, AbstractFilter filter) {
        filter.setLimit(listDTO.getItemsPerPage()); // it is 5 by default

        int offset = listDTO.getItemsPerPage() * (listDTO.getPage() - 1);
        filter.setOffset(listDTO.getTotalCount() < offset ? 0 : offset);

        final SortDTO sortModel = listDTO.getSort();
        if (sortModel != null) {
            filter.setSortColumn(sortModel.getColumn());
            filter.setSortOrder(sortModel.isAscending());
        }
    }

}