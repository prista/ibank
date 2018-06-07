package com.prista.netbanking.web.dto.address;

public class RegionDTO {
    private Integer id;
    private String title;

    public RegionDTO(final Integer id, final String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

}
