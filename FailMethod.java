// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Built based on the Strategy design pattern.
// An interface that allows for different algorithms representing router failure to be implemented.
public interface FailMethod {

    public boolean failStep(); // Returns if the router has failed
    
};