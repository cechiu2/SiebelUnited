package siebelunited.cs465.illinois.edu.focus;
import java.io.Serializable;
public class Task implements java.io.Serializable {
    String task_name;
    int is_finished;

    public Task(String set_name, int set_finished) {
        task_name = set_name;
        is_finished = set_finished;
    }
}
