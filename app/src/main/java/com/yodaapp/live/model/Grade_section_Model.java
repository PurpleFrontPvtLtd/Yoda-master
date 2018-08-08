package com.yodaapp.live.model;


public class Grade_section_Model {

    String section_head_id;
    String section_head_name;
    String branch_name;
    String grade_name;
    String school_name;
    String subjects;
    String students;

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSection_head_id() {
        return section_head_id;
    }

    public void setSection_head_id(String section_head_id) {
        this.section_head_id = section_head_id;
    }

    public String getSection_head_name() {
        return section_head_name;
    }

    public void setSection_head_name(String section_head_name) {
        this.section_head_name = section_head_name;
    }

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }
}
