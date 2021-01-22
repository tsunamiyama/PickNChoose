package kristsuchiyama.picknchoose;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class CustomList extends AppCompatActivity {
    private RecyclerView recyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public TextView addButton;
    public String inputText;
    public Button rollButton;
    public List<String> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);
        recyclerview = (RecyclerView) findViewById(R.id.customlistrecyclerview);
        addButton = (TextView) findViewById(R.id.addButton);
        rollButton = (Button) findViewById(R.id.rollButton);
        recyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter2(options);
        recyclerview.setAdapter(mAdapter);

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomList.this);
                builder.setTitle("Add an Option");

                final EditText input = new EditText(CustomList.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText  = input.getText().toString();
                        options.add(inputText);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(options.size() > 0) {
                    startActivity(new Intent(CustomList.this, HenryAnimation.class).putExtra("menuChoice", getChoice()));
                }
            }
        });

        final Button backBtn = (Button) findViewById(R.id.btoSelection);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public String getChoice(){
        int length = options.size();
        Random rand = new Random();
        int foodChoice = rand.nextInt(length);
        return options.get(foodChoice);
    }
}