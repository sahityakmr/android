package com.example.android;

public class Biometric {
    private String id;
    private String fingerprint;
    private String fingerprint2;
    private String fingerprint3;
    private String fingerprint4;
    private String fingerprint5;
    private String fingerprint6;
    private String fingerprint7;
    private String fingerprint8;
    private String fingerprint9;
    private String fingerprint10;

    private String[] fingerprints;

    public Biometric(String image_name, String fingerprint, String fingerprint2, String fingerprint3, String fingerprint4, String fingerprint5, String fingerprint6, String fingerprint7, String fingerprint8, String fingerprint9, String fingerprint10) {
        this.id = image_name;
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
        updateArray();
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

    public String[] getFingerprints() {
        return fingerprints;
    }
}
