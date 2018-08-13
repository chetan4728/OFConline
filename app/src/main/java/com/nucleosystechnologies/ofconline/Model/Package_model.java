package com.nucleosystechnologies.ofconline.Model;

import org.json.JSONArray;

public class Package_model {

  int pkg_id;
  String pkg_name;
  String pkg_price;
  String pkg_validity;
  String pkg_limit;
  JSONArray pkg_detail;

    public int getPkg_id() {
        return pkg_id;
    }

    public void setPkg_id(int pkg_id) {
        this.pkg_id = pkg_id;
    }

    public String getPkg_name() {
        return pkg_name;
    }

    public void setPkg_name(String pkg_name) {
        this.pkg_name = pkg_name;
    }

    public String getPkg_price() {
        return pkg_price;
    }

    public void setPkg_price(String pkg_price) {
        this.pkg_price = pkg_price;
    }

    public String getPkg_validity() {
        return pkg_validity;
    }

    public void setPkg_validity(String pkg_validity) {
        this.pkg_validity = pkg_validity;
    }

    public String getPkg_limit() {
        return pkg_limit;
    }

    public void setPkg_limit(String pkg_limit) {
        this.pkg_limit = pkg_limit;
    }

    public JSONArray getPkg_detail() {
        return pkg_detail;
    }

    public void setPkg_detail(JSONArray pkg_detail) {
        this.pkg_detail = pkg_detail;
    }
}
