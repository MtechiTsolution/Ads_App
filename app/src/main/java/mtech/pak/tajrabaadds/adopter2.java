package mtech.pak.tajrabaadds;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.os.ConfigurationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class adopter2 extends RecyclerView.Adapter<adopter2.newviewholder> {
    static Context context;
    static List<Showuserdata> data;
    static float m = 0;

    FirebaseAuth mauth;
    FirebaseUser user;
    FirebaseDatabase fb;
    static String sp;
    static DatabaseReference dr;
    String countryName;


    public adopter2(Context context, List<Showuserdata> data, int m) {
        this.context = context;
        this.data = data;
        this.m = m;
    }




    @Override
    public newviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_userdata, null);
        Log.d("TAG", "reach1");
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();



        return new newviewholder(view);
    }

    @Override
    public void onBindViewHolder(final newviewholder holder, final int position) {
        final Showuserdata dm = data.get(position);

        m=0;
        sp="0";

        Picasso.get().load(dm.getImages()).placeholder( R.drawable.man ).into(holder.c3);
        holder.name.setText(dm.getName());

        Locale locale = ConfigurationCompat.getLocales( Resources.getSystem().getConfiguration()).get(0);
        holder.nmber.setText(locale.getCountry());

        holder.balance.setText(dm.getEarn());



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
//            final Showuserdata dm = data.get(getPosition());
//
//
//            AlertDialog.Builder alertdialg=new AlertDialog.Builder( context );
//            alertdialg.setTitle( "Remove " );
//            alertdialg.setMessage( "are you sure want to to remove this...." );
//            alertdialg.setPositiveButton( "yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    m=m+Float.parseFloat( sp );
//                    dr.child("user profile").child(dm.getId()).child("earn").setValue(String.valueOf(m));
//                    dr.child("user profile").child(dm.getId()).child("balance").setValue("0");
//                    dr.child("user profile").child(dm.getId()).child("drawmoney").setValue("0");
//                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    database.getReference("Withdraw")
//                            .child(dm.getId())
//                            .removeValue();
//                    data.remove(getPosition());
//                    notifyDataSetChanged();
//                }
//            } );
//            AlertDialog alertDialog=alertdialg.create();
//            alertdialg.show();




            return false;
        }


    }



}