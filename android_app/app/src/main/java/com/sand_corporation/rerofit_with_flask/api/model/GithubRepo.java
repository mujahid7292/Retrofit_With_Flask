package com.sand_corporation.rerofit_with_flask.api.model;

public class GithubRepo {
    private int id;
    private String name;

    public GithubRepo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
