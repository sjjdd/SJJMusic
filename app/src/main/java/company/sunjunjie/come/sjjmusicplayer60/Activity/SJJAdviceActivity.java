package company.sunjunjie.come.sjjmusicplayer60.Activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;

import company.sunjunjie.come.sjjmusicplayer60.Fragment.AdviceErrorFragment;
import company.sunjunjie.come.sjjmusicplayer60.Fragment.LoseSongFragment;
import company.sunjunjie.come.sjjmusicplayer60.LocalMusicFragment;
import company.sunjunjie.come.sjjmusicplayer60.R;

public class SJJAdviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjjadvice);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.littleback);
        }
        Button submit=(Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SJJAdviceActivity.this, "我们已经收到您宝贵的意见，我们会尽快处理并改善的！", Toast.LENGTH_SHORT).show();
            }
        });
        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.chooseAdvice);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.advicetopro:
                    case R.id.code_error:
                        replaceFragment(new AdviceErrorFragment());
                        break;
                    case R.id.lose_song:
                        replaceFragment(new LoseSongFragment());
                        break;
                        default:
                            break;
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar2,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                SJJAdviceActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.adviceFragment,fragment);
        transaction.commit();
    }
}
