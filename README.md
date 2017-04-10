# countryCodeList
添加依赖 compile 'com.wi11s:countrycodelist:$versionName'

versionName：1.0.1
	修改一些 ui，增加搜索结果空时的显示

versionName号：1.0.0 
	第一个版本

####简单介绍
* 支持中英文支持，根据本地语言；
* 只支持对于条目的click 监听，ChildrenBean 包含一个 id，code，text;
* 并未加入自定义样式，后期根据需求再进行修改;
* 主体用的是 PinnedSectionListView
* 右侧导航用的是WaveSideBar
* 搜索结果用的是 RecycleView

####api
* CountryListView：
	 countryListView.setCountryItemClistener(new CountryListView.OnCountryItemClickListener() {  
  @Override  
  public void onClick(View item, CountryList.DataBean.ChildrenBean childrenBean) {  
      
  }
});

	
