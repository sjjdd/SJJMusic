package company.sunjunjie.come.sjjmusicplayer60.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.leon.lib.settingview.LSettingItem;

import java.io.InputStream;

import company.sunjunjie.come.sjjmusicplayer60.R;

public class SJJSettingActivity extends AppCompatActivity {
    private LSettingItem changeBackground,givenAdvice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjjsetting);
        changeBackground=(LSettingItem) findViewById(R.id.changebackground);
        changeBackground.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent=new Intent(SJJSettingActivity.this,SJJBackgroundActivity.class);
                startActivity(intent);
            }
        });
        givenAdvice=(LSettingItem) findViewById(R.id.advice);
        givenAdvice.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
              Intent intent=new Intent(SJJSettingActivity.this,SJJAdviceActivity.class);
              startActivity(intent);
            }
        });
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.littleback);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar2,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                SJJSettingActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }

}
