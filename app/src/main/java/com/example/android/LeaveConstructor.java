package com.example.android;

public class LeaveConstructor {

    private final String employee_id;
    private final String status;
    private final String start;
    private final String end;
    private final String reason;
    private final String name;
    private final String id;
    private final String DATEDIFF;

    public LeaveConstructor(String id, String employee_id, String status, String start, String end, String reason, String name, String DATEDIFF) {
        this.id = id;
        this.employee_id = employee_id;
        this.status = status;
        this.start = start;
        this.end = end;
        this.reason = reason;
        this.name = name;
        this.DATEDIFF = DATEDIFF;
    }

    public String getId() {
        return id;
    }

    public String getemployee() {
        return employee_id;
    }

    public String getstatus() {
        return status;
    }

    public String getstart() {
        return start;
    }

    public String getend() {
        return end;
    }

    public String getreason() {
        return reason;
    }

    public String getname() {
        return name;
    }

    public String getdays() {
        return DATEDIFF;
    }


}