package comassignment.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class EmptyActivity extends AppCompatActivity {
    long db_id;
    String message;
    boolean isSent;

//    FrameLayout frame123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
//        frame123 = findViewById(R.id.frame1);
        Intent in = getIntent();

       message = in.getStringExtra("msg");
       db_id = in.getLongExtra("id",12);
       isSent = in.getBooleanExtra("issent",false);

        Fragment fragment = new DetailsFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("msg", message);
        bundle2.putLong("id", db_id);
        bundle2.putBoolean("issent", isSent);
        fragment.setArguments(bundle2);
        loadFragment(fragment);
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame1, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}