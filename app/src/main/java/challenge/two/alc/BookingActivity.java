package challenge.two.alc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * this class is just to mimic booking
 */

public class BookingActivity extends AppCompatActivity {
    private Button submitBtn;
    private EditText firstName, surName, nationality, email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        submitBtn = findViewById(R.id.submit_btn);




        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookingActivity.this, "Booking Successful", Toast.LENGTH_SHORT).show();
                backToTour();

            }
        });

    }



    private void backToTour(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
