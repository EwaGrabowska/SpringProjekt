package com.spring.start.springProjekt.netcdfFfile.vo;

public class ArgoFileSourceId {

    private String id;

    protected ArgoFileSourceId() {
    }

    public ArgoFileSourceId(final Long id) {
        this.id = String.valueOf(id);
    }

    public String getId() {
        return id;
    }
}
