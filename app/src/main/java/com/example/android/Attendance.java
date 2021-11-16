package com.example.android;

import java.io.File;

public class Attendance {
    private final String id;
    private final String fingerprint;
    private final String fingerprint2;
    private final String fingerprint3;
    private final String fingerprint4;
    private final String fingerprint5;
    private final String fingerprint6;
    private final String fingerprint7;
    private final String fingerprint8;
    private final String fingerprint9;
    private final String fingerprint10;
    private final String time_in;
    private final String time_out;
    private final String date;
    private final String num_hr;
    private final String employee_id;
    private final String contact_info;

    private String[] fingerprints;

    public Attendance(String id, String fingerprint, String fingerprint2, String fingerprint3, String fingerprint4, String fingerprint5, String fingerprint6, String fingerprint7, String fingerprint8, String fingerprint9, String fingerprint10, String time_in, String time_out, String date, String num_hr, String employee_id, String contact_info, String[] fingerprints) {
        this.id = id;
        this.fingerprint = fingerprint;
        this.fingerprint2 = fingerprint2;
        this.fingerprint3 = fingerprint3;
        this.fingerprint4 = fingerprint4;
        this.fingerprint5 = fingerprint5;
        this.fingerprint6 = fingerprint6;
        this.fingerprint7 = fingerprint7;
        this.fingerprint8 = fingerprint8;
        this.fingerprint9 = fingerprint9;
        this.fingerprint10 = fingerprint10;
        this.time_in = time_in;
        this.time_out = time_out;
        this.date = date;
        this.num_hr = num_hr;
        this.employee_id = employee_id;
        this.contact_info = contact_info;
        this.fingerprints = fingerprints;
    }

    public String getId() {
        return id;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getFingerprint2() {
        return fingerprint2;
    }

    public String getFingerprint3() {
        return fingerprint3;
    }

    public String getFingerprint4() {
        return fingerprint4;
    }

    public String getFingerprint5() {
        return fingerprint5;
    }

    public String getFingerprint6() {
        return fingerprint6;
    }

    public String getFingerprint7() {
        return fingerprint7;
    }

    public String getFingerprint8() {
        return fingerprint8;
    }

    public String getFingerprint9() {
        return fingerprint9;
    }

    public String getFingerprint10() {
        return fingerprint10;
    }

    public String gettimein() {
        return time_in;
    }

    public String gettimeout() {
        return time_out;
    }

    public String getdate() {
        return date;
    }

    public String getnumberofhour() {
        return num_hr;
    }

    public String getemployee() {
        return employee_id;
    }

    public String getContact_info() {
        return contact_info;
    }

    public String[] getFingerprints() {
        return fingerprints;
    }

    public void updateArray() {
        fingerprints = new String[10];

        fingerprints[0] = this.fingerprint;
        fingerprints[1] = this.fingerprint2;
        fingerprints[2] = this.fingerprint3;
        fingerprints[3] = this.fingerprint4;
        fingerprints[4] = this.fingerprint5;
        fingerprints[5] = this.fingerprint6;
        fingerprints[6] = this.fingerprint7;
        fingerprints[7] = this.fingerprint8;
        fingerprints[8] = this.fingerprint9;
        fingerprints[9] = this.fingerprint10;
    }

}
