package comassignment.lab7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsFragment extends Fragment {
    TextView tv1, tv2;
    CheckBox checkBox;
    long db_id;
    String message;
    boolean isSent;
    Button hide;

    public DetailsFragment() {
        // Required empty public constructor


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        tv1 = v.findViewById(R.id.tv1);
        tv2 = v.findViewById(R.id.tv2);
        hide = v.findViewById(R.id.hide);
        checkBox = v.findViewById(R.id.checkBox);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             message = bundle.getString("msg", "test");
             db_id = bundle.getLong("id", 123);
             isSent = bundle.getBoolean("issent", false);

        }
        else {
            Toast.makeText(getContext(), "Bundle Error", Toast.LENGTH_SHORT).show();

        }

        tv1.setText("Message Here :- " + message);
        tv2.setText("ID = " + db_id);
        if (isSent) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                remove_frag(DetailsFragment.this);
            }
        });

        return v;
    }


    private void remove_frag(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

    }
}