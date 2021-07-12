package mtech.pak.tajrabaadds;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adopter extends RecyclerView.Adapter<adopter.newviewholder> {
    static Context context;
    static List<withdrwadata> data;
    static float m = 0;

    FirebaseAuth mauth;
    FirebaseUser user;
    FirebaseDatabase fb;
    static String sp;
    static DatabaseReference dr;


    public adopter(Context context, List<withdrwadata> data, int m) {
        this.context = context;
        this.data = data;
        this.m = m;
    }




    @Override
    public newviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemdesign, null);
        Log.d("TAG", "reach1");
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();



        return new newviewholder(view);
    }

    @Override
    public void onBindViewHolder(final newviewholder holder, final int position) {
        final withdrwadata dm = data.get(position);

        m=0;
        sp="0";
             holder.name.setText(dm.getName());
             holder.nmber.setText(dm.getNumber());
             dr.child("user profile").child(dm.getId()).addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(dataSnapshot.hasChild("images"))
                     {
                         Picasso.get().load(dataSnapshot.child("images").getValue().toString()).placeholder( R.drawable.man ).into(holder.c3);
                     }

                     sp=dataSnapshot.child("balance").getValue().toString();
                     holder.balance.setText(sp);
                     if(dataSnapshot.hasChild("earn"))
                     {
                         m=Float.parseFloat(dataSnapshot.child("earn").getValue().toString());
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });







    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class newviewholder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView name, nmber, balance;
        ImageView c3;



        public newviewholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nmus);
            nmber = itemView.findViewById(R.id.stats);
          balance = itemView.findViewById(R.id.statsp);
            c3 = itemView.findViewById(R.id.userpk);

            itemView.setOnLongClickListener(this);
        }



        @Override
        public boolean onLongClick(View v) {
            final withdrwadata dm = data.get(getPosition());


            AlertDialog.Builder alertdialg=new AlertDialog.Builder( context );
            alertdialg.setTitle( "Remove " );
            alertdialg.setMessage( "are you sure want to to remove this...." );
            alertdialg.setPositiveButton( "yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m=m+Float.parseFloat( sp );
                    dr.child("user profile").child(dm.getId()).child("earn").setValue(String.valueOf(m));
                    dr.child("user profile").child(dm.getId()).child("balance").setValue("0");
                    dr.child("user profile").child(dm.getId()).child("drawmoney").setValue("0");
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference("Withdraw")
                            .child(dm.getId())
                            .removeValue();
                    data.remove(getPosition());
                    notifyDataSetChanged();
                }
            } );
            AlertDialog alertDialog=alertdialg.create();
            alertdialg.show();




            return false;
        }
    }

}