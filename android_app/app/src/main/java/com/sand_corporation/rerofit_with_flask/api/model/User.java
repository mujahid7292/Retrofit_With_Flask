package com.sand_corporation.rerofit_with_flask.api.model;

public class User {

    private Integer id; //As this field is nullable.
    private  String name, email;
    private int age;
    private String[] topics;

    public User(String name, String email, int age, String[] topics) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.topics = topics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }
}
