package comassignment.lab7;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private DBManager dbManager;

    private ListView listView;
    Fragment fragment = new DetailsFragment();

   myadapter ad = new myadapter();
    private SQLiteDatabase database;
    Button msg_sent, msg_receive;
    EditText msgtext;
    AlertDialog alertDialog;

    ArrayList<Message> arrayList = new ArrayList<>();
    FrameLayout frame1;
    boolean is_phn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_room);

        msg_sent = findViewById(R.id.msg_sent);
        msg_receive = findViewById(R.id.msg_receive);
        msgtext = findViewById(R.id.msgtext);
        frame1 = findViewById(R.id.frame1);
        Log.d("mymsg",frame1+"");
        if(frame1 == null){
            is_phn = true;
        }
        else {
            is_phn = false;
        }
        //chck phn or tablet

        dbManager = new DBManager(this);
        fetch_messages();

        dbManager.open();
        Cursor cursor1 = dbManager.fetch();
        int version = dbManager.versions();
        dbManager.printCursor(cursor1,version);



        msg_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = msgtext.getText().toString();

                dbManager.insert(message, true);
                Toast.makeText(ChatRoomActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                msgtext.setText("");
                fetch_messages();
            }
        });
        msg_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = msgtext.getText().toString();

                dbManager.insert(message, false);
                Toast.makeText(ChatRoomActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                msgtext.setText("");
                fetch_messages();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

           Toast.makeText(getApplicationContext(),"Settings", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    class myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Message obj = arrayList.get(position);


            if(obj.isSent){
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                convertView = inflater.inflate(R.layout.design2,parent,false);

                LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout);
                LinearLayout parent_layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout_parent);

                TextView tvid = convertView.findViewById(R.id.id);
                TextView tvmessage = convertView.findViewById(R.id.message);
                TextView tvsentorreceive = convertView.findViewById(R.id.sentorreceive);
                ImageView imv1 = convertView.findViewById(R.id.imv1);
                tvsentorreceive.setVisibility(View.GONE);



                tvid.setText(obj.getDb_id()+"");
                tvmessage.setText(obj.getMessage());
                tvsentorreceive.setText(obj.isSent()+"");
                parent_layout.setGravity(Gravity.RIGHT);
                imv1.setImageResource(R.drawable.girl);
                final String message = "ID :- "+obj.getDb_id()+"\n Message :- "+obj.getMessage()+"\n" +
                        "If you want to delete the message ?";

                parent_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomActivity.this);
                        alertDialogBuilder.setMessage(message);
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        dbManager.delete(obj.getDb_id());
                                        Toast.makeText(ChatRoomActivity.this, "MSG DELETED", Toast.LENGTH_SHORT).show();
                                        fetch_messages();
                                        if(!is_phn){
                                            removeYourFragment();
                                        }

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("no",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });



                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        return true;
                    }
                });


                parent_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(is_phn){
                            Intent in = new Intent(getApplicationContext(),EmptyActivity.class);
                            in.putExtra("msg",obj.getMessage());
                            in.putExtra("id",obj.getDb_id());
                            in.putExtra("issent",obj.isSent);

                            startActivity(in);

                        }else {
                            fragment = new DetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", obj.getMessage());
                            bundle.putLong("id", obj.getDb_id());
                            bundle.putBoolean("issent", obj.isSent);
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
                        }
                    }
                });

            }
            else {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                convertView = inflater.inflate(R.layout.design3,parent,false);

                LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout);
                LinearLayout parent_layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout_parent);

                TextView tvid = convertView.findViewById(R.id.id);
                TextView tvmessage = convertView.findViewById(R.id.message);
                TextView tvsentorreceive = convertView.findViewById(R.id.sentorreceive);
                tvsentorreceive.setVisibility(View.GONE);
                ImageView imv1 = convertView.findViewById(R.id.imv1);



                tvid.setText(obj.getDb_id()+"");
                tvmessage.setText(obj.getMessage());
                tvsentorreceive.setText(obj.isSent()+"");
                parent_layout.setGravity(Gravity.RIGHT);
                imv1.setImageResource(R.drawable.one);


                final String message = "ID :- "+obj.getDb_id()+"\n Message :- "+obj.getMessage()+"\n" +
                        "If you want to delete the message ?";

                parent_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomActivity.this);
                        alertDialogBuilder.setMessage(message);
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        dbManager.delete(obj.getDb_id());
                                        Toast.makeText(ChatRoomActivity.this, "MSG DELETED", Toast.LENGTH_SHORT).show();
                                        fetch_messages();
                                       if(!is_phn){
                                           removeYourFragment();
                                       }

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("no",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });



                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        return true;
                    }
                });
                parent_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(is_phn){
                            Intent in = new Intent(getApplicationContext(),EmptyActivity.class);
                            in.putExtra("msg",obj.getMessage());
                            in.putExtra("id",obj.getDb_id());
                            in.putExtra("issent",obj.isSent);

                            startActivity(in);

                        }else {
                            fragment = new DetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", obj.getMessage());
                            bundle.putLong("id", obj.getDb_id());
                            bundle.putBoolean("issent", obj.isSent);
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
                        }
                    }
                });

            }



            return convertView;
        }
    }

    public void fetch_messages(){
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);

boolean sent = false;
        listView.setAdapter(ad);
        if (cursor.moveToFirst()){
            arrayList.clear();

            while(!cursor.isAfterLast()){

                long id = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID)));
                int id2 = (int) Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.sentorreceive)));
                String message = cursor.getString(cursor.getColumnIndex(DatabaseHelper.message));

                if(id2 == 0){
                    sent = false;
                }
                else {
                    sent = true;
                }

                arrayList.add(new Message(id,message,sent));
                cursor.moveToNext();

            }
        }
        ad.notifyDataSetChanged();
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame1, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void removeYourFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
    }
}