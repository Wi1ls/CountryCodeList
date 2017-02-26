package com.bread.countrycodelist;

import java.util.List;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: shenwei@bongmi.com
 */

public class CountryList {
  private List<DataBean> data;

  public List<DataBean> getData() {
    return data;
  }

  public void setData(List<DataBean> data) {
    this.data = data;
  }

  public static class DataBean {
    /**
     * text : A
     * children : [{"id":"AF","code":"93","text":"阿富汗 (افغانستان)"},{"id":"AL","code":"355","text":"阿尔巴尼亚 (Shqipëri)"},{"id":"DZ","code":"213","text":"阿尔及利亚 (Algeria)"},{"id":"AS","code":"1684","text":"美属萨摩亚 (American Samoa)"},{"id":"AD","code":"376","text":"安道尔 (Andorra)"},{"id":"AO","code":"244","text":"安哥拉 (Angola)"},{"id":"AI","code":"1264","text":"安圭拉 (Anguilla)"},{"id":"AG","code":"1268","text":"安提瓜和巴布达 (Antigua & Barbuda)"},{"id":"AR","code":"54","text":"阿根廷 (Argentina)"},{"id":"AM","code":"374","text":"亚美尼亚 (Հայաստան)"},{"id":"AW","code":"297","text":"阿鲁巴 (Aruba)"},{"id":"AC","code":"247","text":"阿森松岛 (Ascension Island)"},{"id":"AU","code":"61","text":"澳大利亚 (Australia)"},{"id":"AT","code":"43","text":"奥地利 (Österreich)"},{"id":"AZ","code":"994","text":"阿塞拜疆 (Azərbaycan)"}]
     */

    private String text;
    private List<ChildrenBean> children;

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public List<ChildrenBean> getChildren() {
      return children;
    }

    public void setChildren(List<ChildrenBean> children) {
      this.children = children;
    }

    @Override
    public String toString() {
      return "DataBean{" +
          "children=" + children +
          ", text='" + text + '\'' +
          '}';
    }

    public static class ChildrenBean {
      /**
       * id : AF
       * code : 93
       * text : 阿富汗 (افغانستان)
       */

      private String id;
      private String code;
      private String text;

      public String getId() {
        return id;
      }

      public void setId(String id) {
        this.id = id;
      }

      public String getCode() {
        return code;
      }

      public void setCode(String code) {
        this.code = code;
      }

      public String getText() {
        return text;
      }

      public void setText(String text) {
        this.text = text;
      }

      @Override
      public String toString() {
        return "ChildrenBean{" +
            "code='" + code + '\'' +
            ", id='" + id + '\'' +
            ", text='" + text + '\'' +
            '}';
      }
    }
  }

  @Override
  public String toString() {
    return "Country{" +
        "data=" + data +
        '}';
  }
}
