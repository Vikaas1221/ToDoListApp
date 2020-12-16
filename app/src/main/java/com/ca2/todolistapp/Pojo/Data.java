package com.ca2.todolistapp.Pojo;

public class Data
{
    private String topic;
    private String description;
    private int id;

    public Data(String topic, String description)
    {
        this.topic = topic;
        this.description = description;
    }

    public Data(String topic, String description, int id)
    {
        this.topic = topic;
        this.description = description;
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getTopic()
    {
        return topic;
    }

    public String getDescription()
    {
        return description;
    }
}
