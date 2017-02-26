package com.bread.countrycode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bread.countrycodelist.CountryList;
import com.bread.countrycodelist.CountryListView;

public class MainActivity extends AppCompatActivity {
  private CountryListView countryListView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    countryListView= (CountryListView) findViewById(R.id.clv);
    countryListView.setCountryItemClistener(new CountryListView.OnCountryItemClickListener() {
      @Override
      public void onClick(View item, CountryList.DataBean.ChildrenBean childrenBean) {
        Toast.makeText(MainActivity.this,childrenBean.getText(),Toast.LENGTH_SHORT).show();
      }
    });
  }
}
