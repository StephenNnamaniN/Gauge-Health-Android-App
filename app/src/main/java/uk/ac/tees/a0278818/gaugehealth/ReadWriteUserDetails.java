package uk.ac.tees.a0278818.gaugehealth;

public class ReadWriteUserDetails {
    public String doB, gender, mobile;

    public ReadWriteUserDetails(){}

    public ReadWriteUserDetails(String doBTxt, String genderTxt, String mobileTxt) {
        this.doB = doBTxt;
        this.gender = genderTxt;
        this.mobile = mobileTxt;
    }
}
