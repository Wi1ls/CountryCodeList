package com.bread.countrycodelist;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: shenwei@bongmi.com
 */

public class CountryListView extends LinearLayout {
  public static final String EN_FILE="en_country.js";
  public static final String CN_FILE="zh_country.js";
  private Context context;
  //国家代码list 加右侧快捷定位 list
  private View mainContent;
  //固定头部的国家代码
  private PinnedSectionListView pinnedSectionListView;
  private PinnedAdapter pinnedAdapter;
  //右侧快捷定位
  private WaveSideBar waveSideBar;
  //国家首字母
  private String[] countriesLetter;
  //国家首字母节点在 pinnedSectionListView 的位置
  private int[] letterToIndex;
  //包含节点和子，节点是 String，子是CountryList.DataBean.ChildrenBean
  private List<Object> allDataContainsHeaderAndChild = new ArrayList<>();
  //不含首字母的国家集合
  private List<CountryList.DataBean.ChildrenBean> countryChildList =new ArrayList<>();
  //搜索结果的 ListView
  private List<CountryList.DataBean.ChildrenBean> searchChildResult = new ArrayList<>();
  //搜索栏
  private View countrySearch;
  //搜索的 EditTextView
  private EditText searchEditText;
  //搜索的时候出来的黑色浮层
  private View bg;
  //搜索结果
  private RecyclerView rvSearch;
  private RvSearchAdapter searchAdapter;
  //取消按钮
  private View searchCancel;
  private ImageView deleteTextView;
  //防止重复搜索，上次搜索关键词
  private String lastSearch;

  public CountryListView(Context context) {
    this(context, null);
  }

  public CountryListView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CountryListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    setOrientation(VERTICAL);
    initCountryJson();
    initCountrySearch();
    initCountryMain();
    addListener();

  }

  //解析本地国家代码 js
  private void initCountryJson() {
    try {
      String fileName;
      String language=Locale.getDefault().getLanguage();
      if("zh".equals(language)){
        fileName=CN_FILE;
      }else{
        fileName=EN_FILE;
      }
      InputStreamReader isr = new InputStreamReader(context.getAssets().open(fileName), "UTF-8");
      BufferedReader br = new BufferedReader(isr);
      String line;
      StringBuilder builder = new StringBuilder();
      while ((line = br.readLine()) != null) {
        builder.append(line);
      }
      br.close();
      isr.close();
      Gson gson = new Gson();
      CountryList country = gson.fromJson(builder.toString(), CountryList.class);
      countriesLetter = new String[country.getData().size()];
      letterToIndex=new int[countriesLetter.length];
      for (int index = 0; index < countriesLetter.length; index++) {
        letterToIndex[index]=allDataContainsHeaderAndChild.size();
        countriesLetter[index] = country.getData().get(index).getText();
        allDataContainsHeaderAndChild.add(countriesLetter[index]);
        for (int i = 0; i < country.getData().get(index).getChildren().size(); i++) {
          allDataContainsHeaderAndChild.add(country.getData().get(index).getChildren().get(i));
          countryChildList.add(country.getData().get(index).getChildren().get(i));
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //初始化搜索
  private void initCountrySearch() {
    countrySearch = LayoutInflater.from(context).inflate(R.layout.country_search, null, false);
    searchEditText = (EditText) countrySearch.findViewById(R.id.et_search);
    searchCancel=countrySearch.findViewById(R.id.cancel_search);
    deleteTextView= (ImageView) countrySearch.findViewById(R.id.iv_et_delete);
    addView(countrySearch);
  }

  //初始化主界面
  private void initCountryMain() {
    View countryMain = LayoutInflater.from(context).inflate(R.layout.country_main, null, false);
    mainContent=countryMain.findViewById(R.id.rl_main);
    pinnedSectionListView= (PinnedSectionListView) countryMain.findViewById(R.id.pinnedSectionListView);
    waveSideBar= (WaveSideBar) countryMain.findViewById(R.id.waveSideBar);
    bg = countryMain.findViewById(R.id.bg);
    rvSearch = (RecyclerView) countryMain.findViewById(R.id.rv_search);
    rvSearch.setLayoutManager(new LinearLayoutManager(context));
    searchAdapter = new RvSearchAdapter( context);
    rvSearch.setAdapter(searchAdapter);
    rvSearch.addItemDecoration(new DividerItemDecoration(context,VERTICAL));
    addView(countryMain);
    waveSideBar.setIndexItems(countriesLetter);
    pinnedAdapter=new PinnedAdapter();
    pinnedSectionListView.setAdapter(pinnedAdapter);
    waveSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
      @Override
      public void onSelectIndexItem(String index) {
        pinnedSectionListView.setSelection(letterToIndex[getLetterIndex(index)]);
      }
    });
  }

  //根据右侧快捷索引String返回所谓位
  private int getLetterIndex(String index){
    for(int i=0;i<countriesLetter.length;i++){
      if (index.equals(countriesLetter[i]))
        return i;
    }
    return 0;
  }

  private void addListener() {
    bg.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        cancelSearch();
      }
    });

    searchCancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        cancelSearch();
      }
    });
    searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          bg.setVisibility(VISIBLE);
          searchCancel.setVisibility(VISIBLE);
          deleteTextView.setVisibility(VISIBLE);
        }else{
          bg.setVisibility(INVISIBLE);
          rvSearch.setVisibility(INVISIBLE);
          mainContent.setVisibility(VISIBLE);
          deleteTextView.setVisibility(INVISIBLE);
        }
      }
    });
    searchEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().equals(lastSearch)) {
          return;
        }
        if(lastSearch!=null&& TextUtils.isEmpty(s.toString().trim())){
          searchChildResult.clear();
          searchAdapter.refresh(searchChildResult);
          return;
        }
        lastSearch = s.toString();
        searchChildResult.clear();
        for (int i = 0; i < countryChildList.size(); i++) {
          if (TextUtils.isEmpty(countryChildList.get(i).getText().trim()))
            continue;
          if (countryChildList.get(i).getText().contains(s.toString()))
            searchChildResult.add(countryChildList.get(i));
        }
        if (rvSearch.getVisibility() == INVISIBLE) {
          rvSearch.setVisibility(VISIBLE);
          bg.setVisibility(INVISIBLE);
          mainContent.setVisibility(INVISIBLE);
        }
        searchAdapter.refresh(searchChildResult);
      }
    });

    deleteTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        searchEditText.setText(null);
      }
    });
  }

  //取消搜索的
  private void cancelSearch(){
    searchEditText.setText(null);
    rvSearch.setVisibility(INVISIBLE);
    bg.setVisibility(INVISIBLE);
    searchCancel.setVisibility(GONE);
    searchAdapter.clear();
    mainContent.requestFocus();
    deleteTextView.setVisibility(GONE);
  }
  public interface OnCountryItemClickListener {
    void onClick(View item, CountryList.DataBean.ChildrenBean childrenBean);
  }

  public class RvSearchAdapter extends RecyclerView.Adapter<RvSearchAdapter.MyViewHolder> {
    private CountryListView.OnCountryItemClickListener onCountryItemClickListener;
    private List<CountryList.DataBean.ChildrenBean> data;
    private Context context;

    public RvSearchAdapter( Context context) {
      this.data = new ArrayList<>();
      this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.country_item, null, false);
      return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      final CountryList.DataBean.ChildrenBean child = data.get(position);
      holder.countryName.setText(child.getText());
      holder.countryCode.setText(String.format("%s %s", "+", child.getCode()));
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (onCountryItemClickListener != null) {
            onCountryItemClickListener.onClick(v, child);
            cancelSearch();
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return this.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
      TextView countryName;
      TextView countryCode;

      public MyViewHolder(View itemView) {
        super(itemView);
        countryName = (TextView) itemView.findViewById(R.id.text_country_name);
        countryCode = (TextView) itemView.findViewById(R.id.text_country_code);
      }
    }

    public void refresh(List<CountryList.DataBean.ChildrenBean> data) {
      this.data.clear();
      this.data.addAll(data);
      notifyDataSetChanged();
    }

    public void clear(){
      this.data.clear();
      notifyDataSetChanged();
    }

    private void setOnCountryItemClickListener(OnCountryItemClickListener listener){
     onCountryItemClickListener=listener;
    }
  }

  class PinnedAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter{
    private static final int TYPE_COUNT=2;
    private static final int TYPE_HEAD=0;
    private static final int TYPE_CHILD=1;
    private OnCountryItemClickListener onCountryItemClickListener;

    @Override
    public int getViewTypeCount() {
      return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
      return allDataContainsHeaderAndChild.get(position) instanceof CountryList.DataBean.ChildrenBean?
          TYPE_CHILD:TYPE_HEAD;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
      return viewType==TYPE_HEAD;
    }

    @Override
    public int getCount() {
      return allDataContainsHeaderAndChild.size() ;
    }

    @Override
    public Object getItem(int position) {
      return allDataContainsHeaderAndChild.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      Holder holder;
      if(convertView==null){
        if(getItemViewType(position)==TYPE_CHILD){
          convertView=getChild();
        }else{
          convertView=getHeader();
        }
      }
      holder= (Holder) convertView.getTag();
      if(getItemViewType(position)==TYPE_CHILD){
        final CountryList.DataBean.ChildrenBean childrenBean= (CountryList.DataBean.ChildrenBean) allDataContainsHeaderAndChild.get(position);
        holder.countryName.setText(childrenBean.getText());
        holder.countryCode.setText(String.format("%s %s","+",childrenBean.getCode()));
        if(onCountryItemClickListener!=null){
          convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              onCountryItemClickListener.onClick(v,childrenBean);
            }
          });

        }
      }else{
        String header= (String) allDataContainsHeaderAndChild.get(position);
        holder.header.setText(header);
        convertView.setOnClickListener(null);
      }

      return convertView;
    }

    class Holder{
      TextView header;
      TextView countryName;
      TextView countryCode;
    }

    private TextView getHeader(){
      TextView header= (TextView) LayoutInflater.from(context).inflate(R.layout.country_header,null,false);
      Holder holder=new Holder();
      holder.header=header;
      header.setTag(holder);
      return header;
    }

    private View getChild(){
      View child=LayoutInflater.from(context).inflate(R.layout.country_item,null,false);
      Holder holder=new Holder();
      holder.countryCode= (TextView) child.findViewById(R.id.text_country_code);
      holder.countryName= (TextView) child.findViewById(R.id.text_country_name);
      child.setTag(holder);
      return child;
    }

    private void setOnCountryItemClickListener(OnCountryItemClickListener listener){
      onCountryItemClickListener=listener;
    }
  }

  public void setCountryItemClistener(OnCountryItemClickListener listener){
    pinnedAdapter.setOnCountryItemClickListener(listener);
    searchAdapter.setOnCountryItemClickListener(listener);
  }

}
