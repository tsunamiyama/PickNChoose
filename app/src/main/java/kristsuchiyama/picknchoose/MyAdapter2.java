package kristsuchiyama.picknchoose;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Vector;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder>{
    private List<String> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtHeader;
        public View layout;
        public TextView remBut;

        public ViewHolder(View v){
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.customListTextOne);
            remBut = (TextView) v.findViewById(R.id.removeButton);
        }
    }
    public void add(int position, String item){
        values.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }
    public void removed(int position){
        values.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
    public MyAdapter2(List<String> myDataset){
        values = myDataset;
    }
    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.customlistcard, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        final String name = values.get(position);
        holder.txtHeader.setText(name);
        holder.remBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removed(position);
            }
        });
    }
    @Override
    public int getItemCount(){
        return values.size();
    }
}
