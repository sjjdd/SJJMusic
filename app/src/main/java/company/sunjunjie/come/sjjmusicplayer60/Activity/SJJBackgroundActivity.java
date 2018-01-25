package company.sunjunjie.come.sjjmusicplayer60.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import company.sunjunjie.come.sjjmusicplayer60.Adapter.BackgroundAdapter;
import company.sunjunjie.come.sjjmusicplayer60.DefineItem.Background;
import company.sunjunjie.come.sjjmusicplayer60.R;

public class SJJBackgroundActivity extends AppCompatActivity {
    private LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
    private IntentFilter intentFilter;
    private DrawerLayout drawerLayout;
    private Background[] backgrounds={
            new Background(R.drawable.playerbackground,"蝴蝶之梦"), new Background(R.drawable.background1,"紫雪长道"),
            new Background(R.drawable.background2,"街角绿荫"), new Background(R.drawable.background3,"冬日月光"),
            new Background(R.drawable.background4,"粉红少女")
    };
    private List<Background> backgroundList=new ArrayList<>();
    private BackgroundAdapter backgroundAdapter;
   // private ImageView playerBackground,background1,background2,background3,background4;
    int resId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjjbackground);
       /* intentFilter=new IntentFilter();
        intentFilter.addAction("company.sunjunjie.come.sjjmusicplayer60.CHANGE_BACKGROUND");
        changeBackgroundReceiver=new ChangeBackgroundReceiver();
        localBroadcastManager.registerReceiver(changeBackgroundReceiver,intentFilter);*/
      /* playerBackground=(ImageView) findViewById(R.id.playerbackground);
       background1=(ImageView) findViewById(R.id.background1);
       background2=(ImageView) findViewById(R.id.background2);
       background3=(ImageView) findViewById(R.id.background3);
       background4=(ImageView) findViewById(R.id.background4);
       playerBackground.setOnClickListener(this);
       background1.setOnClickListener(this);
       background2.setOnClickListener(this);
       background3.setOnClickListener(this);
       background4.setOnClickListener(this);*/
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.littleback);
        }
       initBackgrounds();
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this ,2);
        recyclerView.setLayoutManager(layoutManager);
        backgroundAdapter=new BackgroundAdapter(backgroundList);
        recyclerView.setAdapter(backgroundAdapter);
        backgroundAdapter.setOnRecyclerViewListtener(new BackgroundAdapter.OnRecyclerViewListtener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(SJJBackgroundActivity.this, "更换主题为"+backgrounds[position].getBackgroundName(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                intent.putExtra("changebackground",position);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }


    private void initBackgrounds(){
        backgroundList.clear();
        for(int i=0;i<5;i++){
            backgroundList.add(backgrounds[i]);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar2,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
               SJJBackgroundActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}
