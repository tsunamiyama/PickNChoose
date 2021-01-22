package kristsuchiyama.picknchoose;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    private static final int WindowManager = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button btn = (Button)findViewById(R.id.cuisine_button);
        //btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MenuActivity.this , HenryAnimation.class).putExtra("menuChoice","cuisine"));
            }
        });

        Button btn2 = (Button)findViewById(R.id.food_button);
        btn2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MenuActivity.this, HenryAnimation.class).putExtra("menuChoice","food"));
            }
        });

        Button btn3 = (Button)findViewById(R.id.drink_button);
        btn3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MenuActivity.this, HenryAnimation.class).putExtra("menuChoice","drink"));
            }
        });

        Button btn4 = (Button)findViewById(R.id.custom_button);
        btn4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MenuActivity.this, CustomList.class));
            }
        });
    }

}
