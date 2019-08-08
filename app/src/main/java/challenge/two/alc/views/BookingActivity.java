package challenge.two.alc.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import challenge.two.alc.R;

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
        firstName = findViewById(R.id.firstname_tv);
        surName = findViewById(R.id.surname_tv);
        nationality = findViewById(R.id.nationality_user);
        email = findViewById(R.id.email);




        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(BookingActivity.this,"Booking Successful", Toast.LENGTH_SHORT).show();
                finish();
                backToTour();

            }
        });

    }



    private void backToTour(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
